package ezvcard.types;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import ezvcard.parameters.ImageTypeParameter;

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
 * Represents a vCard type that stores image data (for example,
 * {@link PhotoType}).
 * @author Michael Angstadt
 */
public class ImageType extends BinaryType<ImageTypeParameter> {
	/**
	 * Creates an image property.
	 * @param url the URL to the image
	 * @param type the content type (e.g. JPEG)
	 */
	public ImageType(String url, ImageTypeParameter type) {
		super(url, type);
	}

	/**
	 * Creates an image property.
	 * @param data the binary data of the image
	 * @param type the content type (e.g. JPEG)
	 */
	public ImageType(byte[] data, ImageTypeParameter type) {
		super(data, type);
	}

	/**
	 * Creates an image property.
	 * @param in an input stream to the binary data (will be closed)
	 * @param type the content type (e.g. JPEG)
	 * @throws IOException if there's a problem reading from the input stream
	 */
	public ImageType(InputStream in, ImageTypeParameter type) throws IOException {
		super(in, type);
	}

	/**
	 * Creates an image property.
	 * @param file the image file
	 * @param type the content type (e.g. JPEG)
	 * @throws IOException if there's a problem reading from the file
	 */
	public ImageType(File file, ImageTypeParameter type) throws IOException {
		super(file, type);
	}
}
