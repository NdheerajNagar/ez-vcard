package ezvcard.io.json;

import static ezvcard.util.IOUtils.utf8Writer;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardDataType;
import ezvcard.VCardVersion;
import ezvcard.io.EmbeddedVCardException;
import ezvcard.io.SkipMeException;
import ezvcard.io.scribe.ScribeIndex;
import ezvcard.io.scribe.VCardPropertyScribe;
import ezvcard.parameter.VCardParameters;
import ezvcard.property.ProductId;
import ezvcard.property.VCardProperty;

/*
 Copyright (c) 2013, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * <p>
 * Writes {@link VCard} objects to a JSON data stream (jCard format).
 * </p>
 * <p>
 * <b>Example:</b>
 * 
 * <pre class="brush:java">
 * VCard vcard1 = ...
 * VCard vcard2 = ...
 * 
 * File file = new File("vcard.json");
 * JCardWriter jcardWriter = new JCardWriter(file);
 * jcardWriter.write(vcard1);
 * jcardWriter.write(vcard2);
 * jcardWriter.close(); //"close()" must be called in order to terminate the JSON object
 * </pre>
 * 
 * </p>
 * @author Michael Angstadt
 * @see <a href="http://tools.ietf.org/html/rfc7095">RFC 7095</a>
 */
public class JCardWriter implements Closeable, Flushable {
	private ScribeIndex index = new ScribeIndex();
	private final JCardRawWriter writer;
	private final VCardVersion targetVersion = VCardVersion.V4_0;
	private boolean addProdId = true;
	private boolean versionStrict = true;

	/**
	 * Creates a writer that writes jCards to an output stream (UTF-8 encoding
	 * will be used).
	 * @param out the output stream to write the vCard to
	 */
	public JCardWriter(OutputStream out) {
		this(utf8Writer(out));
	}

	/**
	 * Creates a writer that writes jCards to an output stream (UTF-8 encoding
	 * will be used).
	 * @param out the output stream to write the vCard to
	 * @param wrapInArray true to enclose all written vCards in a JSON array,
	 * false not to
	 */
	public JCardWriter(OutputStream out, boolean wrapInArray) {
		this(utf8Writer(out), wrapInArray);
	}

	/**
	 * Creates a writer that writes jCards to a file (UTF-8 encoding will be
	 * used).
	 * @param file the file to write the vCard to
	 * @throws IOException if there's a problem opening the file
	 */
	public JCardWriter(File file) throws IOException {
		this(utf8Writer(file));
	}

	/**
	 * Creates a writer that writes jCards to a file (UTF-8 encoding will be
	 * used).
	 * @param file the file to write the vCard to
	 * @param wrapInArray true to enclose all written vCards in a JSON array,
	 * false not to
	 * @throws IOException if there's a problem opening the file
	 */
	public JCardWriter(File file, boolean wrapInArray) throws IOException {
		this(utf8Writer(file), wrapInArray);
	}

	/**
	 * Creates a writer that writes jCards to a writer.
	 * @param writer the writer to write the vCard to
	 */
	public JCardWriter(Writer writer) {
		this(writer, false);
	}

	/**
	 * Creates a writer that writes jCards to a writer.
	 * @param writer the writer to write the vCard to
	 * @param wrapInArray true to enclose all written vCards in a JSON array,
	 * false not to
	 */
	public JCardWriter(Writer writer, boolean wrapInArray) {
		this.writer = new JCardRawWriter(writer, wrapInArray);
	}

	/**
	 * Writes a vCard to the stream.
	 * @param vcard the vCard to write
	 * @throws IOException if there's a problem writing to the output stream
	 * @throws IllegalArgumentException if a scribe hasn't been registered for a
	 * custom property class (see: {@link #registerScribe})
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void write(VCard vcard) throws IOException {
		List<VCardProperty> propertiesToAdd = new ArrayList<VCardProperty>();
		for (VCardProperty property : vcard) {
			if (addProdId && property instanceof ProductId) {
				//do not add the PRODID in the vCard if "addProdId" is true
				continue;
			}

			if (versionStrict && !property.getSupportedVersions().contains(targetVersion)) {
				//do not add the property to the vCard if it is not supported by the target version
				continue;
			}

			//check for scribes before writing anything to the stream
			if (!index.hasPropertyScribe(property)) {
				throw new IllegalArgumentException("No scribe found for property class \"" + property.getClass().getName() + "\".");
			}

			propertiesToAdd.add(property);
		}

		if (addProdId) {
			VCardProperty prodId = new ProductId("ezvcard " + Ezvcard.VERSION);
			propertiesToAdd.add(prodId);
		}

		writer.writeStartVCard();
		writer.writeProperty("version", VCardDataType.TEXT, JCardValue.single(targetVersion.getVersion()));

		for (VCardProperty property : propertiesToAdd) {
			VCardPropertyScribe scribe = index.getPropertyScribe(property);

			//marshal the value
			JCardValue value;
			try {
				value = scribe.writeJson(property);
			} catch (SkipMeException e) {
				//property has requested not to be written
				continue;
			} catch (EmbeddedVCardException e) {
				//don't write because jCard does not support embedded vCards
				continue;
			}

			String group = property.getGroup();
			String name = scribe.getPropertyName().toLowerCase();
			VCardParameters parameters = scribe.prepareParameters(property, targetVersion, vcard);
			VCardDataType dataType = scribe.dataType(property, targetVersion);

			writer.writeProperty(group, name, parameters, dataType, value);
		}

		writer.writeEndVCard();
	}

	/**
	 * Gets whether or not a "PRODID" property will be added to each vCard,
	 * saying that the vCard was generated by this library.
	 * @return true if this property will be added, false if not (defaults to
	 * true)
	 */
	public boolean isAddProdId() {
		return addProdId;
	}

	/**
	 * Sets whether or not to add a "PRODID" property to each vCard, saying that
	 * the vCard was generated by this library.
	 * @param addProdId true to add this property, false not to (defaults to
	 * true)
	 */
	public void setAddProdId(boolean addProdId) {
		this.addProdId = addProdId;
	}

	/**
	 * Gets whether properties that do not support jCard (vCard version 4.0)
	 * will be excluded from the written vCard.
	 * @return true to exclude properties that do not support jCard, false to
	 * include them anyway (defaults to true)
	 */
	public boolean isVersionStrict() {
		return versionStrict;
	}

	/**
	 * Sets whether properties that do not support jCard (vCard version 4.0)
	 * will be excluded from the written vCard.
	 * @param versionStrict true to exclude properties that do not support
	 * jCard, false to include them anyway (defaults to true)
	 */
	public void setVersionStrict(boolean versionStrict) {
		this.versionStrict = versionStrict;
	}

	/**
	 * Gets whether or not the JSON will be pretty-printed.
	 * @return true if it will be pretty-printed, false if not (defaults to
	 * false)
	 */
	public boolean isIndent() {
		return writer.isIndent();
	}

	/**
	 * Sets whether or not to pretty-print the JSON.
	 * @param indent true to pretty-print it, false not to (defaults to false)
	 */
	public void setIndent(boolean indent) {
		writer.setIndent(indent);
	}

	/**
	 * <p>
	 * Registers a property scribe. This is the same as calling:
	 * </p>
	 * <p>
	 * {@code getScribeIndex().register(scribe)}
	 * </p>
	 * @param scribe the scribe to register
	 */
	public void registerScribe(VCardPropertyScribe<? extends VCardProperty> scribe) {
		index.register(scribe);
	}

	/**
	 * Gets the scribe index.
	 * @return the scribe index
	 */
	public ScribeIndex getScribeIndex() {
		return index;
	}

	/**
	 * Sets the scribe index.
	 * @param index the scribe index
	 */
	public void setScribeIndex(ScribeIndex index) {
		this.index = index;
	}

	/**
	 * Flushes the jCard data stream.
	 * @throws IOException if there's a problem flushing the stream
	 */
	public void flush() throws IOException {
		writer.flush();
	}

	/**
	 * Ends the jCard data stream, but does not close the underlying writer.
	 * @throws IOException if there's a problem closing the stream
	 */
	public void closeJsonStream() throws IOException {
		writer.closeJsonStream();
	}

	/**
	 * Ends the jCard data stream and closes the underlying writer.
	 * @throws IOException if there's a problem closing the stream
	 */
	public void close() throws IOException {
		writer.close();
	}
}
