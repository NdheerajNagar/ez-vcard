package ezvcard.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import ezvcard.VCard;
import ezvcard.VCardSubTypes;
import ezvcard.VCardVersion;
import ezvcard.io.CompatibilityMode;
import ezvcard.parameters.EncodingParameter;
import ezvcard.parameters.ImageTypeParameter;
import ezvcard.parameters.ValueParameter;

/*
 Copyright (c) 2012, Michael Angstadt
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
 * @author Michael Angstadt
 */
public class BinaryTypeTest {
	private byte[] dummyData = "dummy data".getBytes();

	@Test
	public void marshal() throws Exception {
		VCardVersion version;
		List<String> warnings = new ArrayList<String>();
		CompatibilityMode compatibilityMode = CompatibilityMode.RFC2426;
		BinaryTypeImpl t;
		String expectedValue, actualValue;
		VCardSubTypes subTypes;

		//URL v2.1
		version = VCardVersion.V2_1;
		t = new BinaryTypeImpl();
		t.setUrl("http://example.com/image.jpg", ImageTypeParameter.JPEG);
		expectedValue = "http://example.com/image.jpg";
		actualValue = t.marshalValue(version, warnings, compatibilityMode);
		subTypes = t.marshalSubTypes(version, warnings, compatibilityMode, new VCard());
		assertEquals(expectedValue, actualValue);
		assertEquals(ValueParameter.URL, subTypes.getValue());
		assertNull(subTypes.getMediaType());

		//URL v3.0
		version = VCardVersion.V3_0;
		t = new BinaryTypeImpl();
		t.setUrl("http://example.com/image.jpg", ImageTypeParameter.JPEG);
		expectedValue = "http://example.com/image.jpg";
		actualValue = t.marshalValue(version, warnings, compatibilityMode);
		subTypes = t.marshalSubTypes(version, warnings, compatibilityMode, new VCard());
		assertEquals(expectedValue, actualValue);
		assertEquals(ValueParameter.URI, subTypes.getValue());
		assertNull(subTypes.getMediaType());

		//URL v4.0
		version = VCardVersion.V4_0;
		t = new BinaryTypeImpl();
		t.setUrl("http://example.com/image.jpg", ImageTypeParameter.JPEG);
		expectedValue = "http://example.com/image.jpg";
		actualValue = t.marshalValue(version, warnings, compatibilityMode);
		subTypes = t.marshalSubTypes(version, warnings, compatibilityMode, new VCard());
		assertEquals(expectedValue, actualValue);
		assertEquals(ValueParameter.URI, subTypes.getValue());
		assertEquals("image/jpeg", subTypes.getMediaType());

		//base64 data v2.1
		version = VCardVersion.V2_1;
		t = new BinaryTypeImpl();
		t.setData(dummyData, ImageTypeParameter.JPEG);
		expectedValue = Base64.encodeBase64String(dummyData);
		actualValue = t.marshalValue(version, warnings, compatibilityMode);
		subTypes = t.marshalSubTypes(version, warnings, compatibilityMode, new VCard());
		assertEquals(expectedValue, actualValue);
		assertEquals(EncodingParameter.BASE64, subTypes.getEncoding());
		assertNull(subTypes.getValue());
		assertEquals(ImageTypeParameter.JPEG.getValue(), subTypes.getType());

		//base64 data v3.0
		version = VCardVersion.V3_0;
		t = new BinaryTypeImpl();
		t.setData(dummyData, ImageTypeParameter.JPEG);
		expectedValue = Base64.encodeBase64String(dummyData);
		actualValue = t.marshalValue(version, warnings, compatibilityMode);
		subTypes = t.marshalSubTypes(version, warnings, compatibilityMode, new VCard());
		assertEquals(expectedValue, actualValue);
		assertEquals(EncodingParameter.B, subTypes.getEncoding());
		assertNull(subTypes.getValue());
		assertEquals(ImageTypeParameter.JPEG.getValue(), subTypes.getType());

		//base64 data v4.0
		version = VCardVersion.V4_0;
		t = new BinaryTypeImpl();
		t.setData(dummyData, ImageTypeParameter.JPEG);
		expectedValue = "data:image/jpeg;base64," + Base64.encodeBase64String(dummyData);
		actualValue = t.marshalValue(version, warnings, compatibilityMode);
		subTypes = t.marshalSubTypes(version, warnings, compatibilityMode, new VCard());
		assertEquals(expectedValue, actualValue);
		assertNull(subTypes.getEncoding());
		assertEquals(ValueParameter.URI, subTypes.getValue());
		assertNull(subTypes.getType());
	}

	@Test
	public void unmarshal() throws Exception {
		VCardVersion version = VCardVersion.V2_1;
		List<String> warnings = new ArrayList<String>();
		CompatibilityMode compatibilityMode = CompatibilityMode.RFC2426;
		VCardSubTypes subTypes;
		BinaryTypeImpl t;

		t = new BinaryTypeImpl();
		subTypes = new VCardSubTypes();
		subTypes.setValue(ValueParameter.URL);
		t.unmarshalValue(subTypes, "http://example.com/image.jpg", version, warnings, compatibilityMode);
		assertEquals("http://example.com/image.jpg", t.getUrl());
		assertNull(t.getData());
		assertNull(t.getType());

		//no VALUE
		t = new BinaryTypeImpl();
		subTypes = new VCardSubTypes();
		t.unmarshalValue(subTypes, "http://example.com/image.jpg", version, warnings, compatibilityMode);
		assertEquals("http://example.com/image.jpg", t.getUrl());
		assertNull(t.getData());
		assertNull(t.getType());

		//"B" encoding
		t = new BinaryTypeImpl();
		subTypes = new VCardSubTypes();
		subTypes.setType(ImageTypeParameter.JPEG.getValue());
		subTypes.setEncoding(EncodingParameter.B);
		t.unmarshalValue(subTypes, Base64.encodeBase64String(dummyData), version, warnings, compatibilityMode);
		assertNull(t.getUrl());
		assertArrayEquals(dummyData, t.getData());
		assertEquals(ImageTypeParameter.JPEG, t.getType());

		//"BASE64" encoding
		t = new BinaryTypeImpl();
		subTypes = new VCardSubTypes();
		subTypes.setType(ImageTypeParameter.JPEG.getValue());
		subTypes.setEncoding(EncodingParameter.BASE64);
		t.unmarshalValue(subTypes, Base64.encodeBase64String(dummyData), version, warnings, compatibilityMode);
		assertNull(t.getUrl());
		assertArrayEquals(dummyData, t.getData());
		assertEquals(ImageTypeParameter.JPEG, t.getType());

		//no encoding
		//no type
		t = new BinaryTypeImpl();
		subTypes = new VCardSubTypes();
		t.unmarshalValue(subTypes, Base64.encodeBase64String(dummyData), version, warnings, compatibilityMode);
		assertNull(t.getUrl());
		assertArrayEquals(dummyData, t.getData());
		assertNull(t.getType());

		//4.0 URL
		version = VCardVersion.V4_0;
		t = new BinaryTypeImpl();
		subTypes = new VCardSubTypes();
		subTypes.setMediaType("image/jpeg");
		t.unmarshalValue(subTypes, "http://example.com/image.jpg", version, warnings, compatibilityMode);
		assertEquals("http://example.com/image.jpg", t.getUrl());
		assertNull(t.getData());
		assertEquals(ImageTypeParameter.JPEG, t.getType());

		//4.0 data URI
		version = VCardVersion.V4_0;
		t = new BinaryTypeImpl();
		subTypes = new VCardSubTypes();
		t.unmarshalValue(subTypes, "data:image/jpeg;base64," + Base64.encodeBase64String(dummyData), version, warnings, compatibilityMode);
		assertNull(t.getUrl());
		assertArrayEquals(dummyData, t.getData());
		assertEquals(ImageTypeParameter.JPEG, t.getType());
	}

	/**
	 * Test implementation.
	 */
	private class BinaryTypeImpl extends BinaryType<ImageTypeParameter> {
		public BinaryTypeImpl() {
			super("NAME");
		}

		@Override
		protected ImageTypeParameter buildTypeObj(String type) {
			ImageTypeParameter param = ImageTypeParameter.valueOf(type);
			if (param == null) {
				param = new ImageTypeParameter(type, null, null);
			}
			return param;
		}

		@Override
		protected ImageTypeParameter unmarshalMediaType(String mediaType) {
			ImageTypeParameter p = ImageTypeParameter.findByMediaType(mediaType);
			if (p == null) {
				int slashPos = mediaType.indexOf('/');
				String type;
				if (slashPos == -1 || slashPos < mediaType.length() - 1) {
					type = "";
				} else {
					type = mediaType.substring(slashPos + 1);
				}
				p = new ImageTypeParameter(type, mediaType, null);
			}
			return p;
		}
	}
}
