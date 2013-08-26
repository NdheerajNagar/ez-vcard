package ezvcard.types;

import java.util.List;

import ezvcard.VCardVersion;
import ezvcard.io.CompatibilityMode;
import ezvcard.util.JCardDataType;
import ezvcard.util.JCardValue;
import ezvcard.util.XCardElement;

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
 * A language that the person speaks.
 * 
 * <p>
 * <b>Code sample</b>
 * </p>
 * 
 * <pre>
 * VCard vcard = new VCard();
 * LanguageType lang = new LanguageType(&quot;en&quot;);
 * lang.setPref(1); //most preferred
 * vcard.addLanguage(lang);
 * lang = new LanguageType(&quot;fr&quot;);
 * lang.setPref(2); //second-most preferred
 * vcard.addLanguage(lang);
 * </pre>
 * 
 * <p>
 * <b>Property name:</b> <code>LANG</code>
 * </p>
 * <p>
 * <b>Supported versions:</b> <code>4.0</code>
 * </p>
 * 
 * @author Michael Angstadt
 */
public class LanguageType extends TextType implements HasAltId {
	public static final String NAME = "LANG";

	/**
	 * Creates an empty language property.
	 */
	public LanguageType() {
		super(NAME);
	}

	/**
	 * Creates a language property.
	 * @param language the language (e.g. "en-ca")
	 */
	public LanguageType(String language) {
		super(NAME, language);
	}

	/**
	 * Gets the TYPE parameter.
	 * <p>
	 * <b>Supported versions:</b> <code>4.0</code>
	 * </p>
	 * @return the TYPE value (typically, this will be either "work" or "home")
	 * or null if it doesn't exist
	 */
	public String getType() {
		return subTypes.getType();
	}

	/**
	 * Sets the TYPE parameter.
	 * <p>
	 * <b>Supported versions:</b> <code>4.0</code>
	 * </p>
	 * @param type the TYPE value (this should be either "work" or "home") or
	 * null to remove
	 */
	public void setType(String type) {
		subTypes.setType(type);
	}

	@Override
	public List<Integer[]> getPids() {
		return super.getPids();
	}

	@Override
	public void addPid(int localId, int clientPidMapRef) {
		super.addPid(localId, clientPidMapRef);
	}

	@Override
	public void removePids() {
		super.removePids();
	}

	@Override
	public Integer getPref() {
		return super.getPref();
	}

	@Override
	public void setPref(Integer pref) {
		super.setPref(pref);
	}

	//@Override
	public String getAltId() {
		return subTypes.getAltId();
	}

	//@Override
	public void setAltId(String altId) {
		subTypes.setAltId(altId);
	}

	@Override
	public VCardVersion[] getSupportedVersions() {
		return new VCardVersion[] { VCardVersion.V4_0 };
	}

	@Override
	protected void doMarshalXml(XCardElement parent, List<String> warnings, CompatibilityMode compatibilityMode) {
		parent.append("language-tag", getValue());
	}

	@Override
	protected void doUnmarshalXml(XCardElement element, List<String> warnings, CompatibilityMode compatibilityMode) {
		setValue(element.get("language-tag"));
	}

	@Override
	protected JCardValue doMarshalJson(VCardVersion version, List<String> warnings) {
		return JCardValue.single(JCardDataType.LANGUAGE_TAG, value);
	}
}