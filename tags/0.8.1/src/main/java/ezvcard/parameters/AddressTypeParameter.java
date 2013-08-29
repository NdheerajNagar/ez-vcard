package ezvcard.parameters;

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
 * Represents the TYPE parameter of the ADR and LABEL types.
 * <p>
 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
 * </p>
 * @author Michael Angstadt
 */
public class AddressTypeParameter extends TypeParameter {
	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final AddressTypeParameter HOME = new AddressTypeParameter("home");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0, 4.0</code>
	 */
	public static final AddressTypeParameter WORK = new AddressTypeParameter("work");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final AddressTypeParameter DOM = new AddressTypeParameter("dom");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final AddressTypeParameter INTL = new AddressTypeParameter("intl");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final AddressTypeParameter POSTAL = new AddressTypeParameter("postal");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final AddressTypeParameter PARCEL = new AddressTypeParameter("parcel");

	/**
	 * <b>Supported versions:</b> <code>2.1, 3.0</code>
	 */
	public static final AddressTypeParameter PREF = new AddressTypeParameter("pref");

	/**
	 * Use of this constructor is discouraged and should only be used for
	 * defining non-standard TYPEs. Please use one of the predefined static
	 * objects.
	 * @param value the type value (e.g. "home")
	 */
	public AddressTypeParameter(String value) {
		super(value);
	}

	/**
	 * Searches the static objects in this class for one that has a certain type
	 * value.
	 * @param value the type value to search for (e.g. "home")
	 * @return the object or null if not found
	 */
	public static AddressTypeParameter valueOf(String value) {
		return findByValue(value, AddressTypeParameter.class);
	}
}