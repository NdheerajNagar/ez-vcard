package ezvcard.parameter;

import java.util.Collection;

import ezvcard.VCardVersion;
import ezvcard.property.Telephone;

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
 * Represents the TYPE parameter of the {@link Telephone} type.
 * <p>
 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
 * </p>
 * @author Michael Angstadt
 */
public class TelephoneType extends VersionedVCardParameter {
	private static final VCardParameterCaseClasses<TelephoneType> enums = new VCardParameterCaseClasses<TelephoneType>(TelephoneType.class);

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0}
	 */
	public static final TelephoneType BBS = new TelephoneType("bbs", VCardVersion.V2_1, VCardVersion.V3_0);

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0}
	 */
	public static final TelephoneType CAR = new TelephoneType("car", VCardVersion.V2_1, VCardVersion.V3_0);

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
	 */
	public static final TelephoneType CELL = new TelephoneType("cell");

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
	 */
	public static final TelephoneType FAX = new TelephoneType("fax");

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
	 */
	public static final TelephoneType HOME = new TelephoneType("home");

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0}
	 */
	public static final TelephoneType ISDN = new TelephoneType("isdn", VCardVersion.V2_1, VCardVersion.V3_0);

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0}
	 */
	public static final TelephoneType MODEM = new TelephoneType("modem", VCardVersion.V2_1, VCardVersion.V3_0);

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0}
	 */
	public static final TelephoneType MSG = new TelephoneType("msg", VCardVersion.V2_1, VCardVersion.V3_0);

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
	 */
	public static final TelephoneType PAGER = new TelephoneType("pager");

	/**
	 * <b>Supported versions:</b> {@code 3.0}
	 */
	public static final TelephoneType PCS = new TelephoneType("pcs", VCardVersion.V3_0);

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0}
	 */
	public static final TelephoneType PREF = new TelephoneType("pref", VCardVersion.V2_1, VCardVersion.V3_0);

	/**
	 * <b>Supported versions:</b> {@code 4.0}
	 */
	public static final TelephoneType TEXT = new TelephoneType("text", VCardVersion.V4_0);

	/**
	 * <b>Supported versions:</b> {@code 4.0}
	 */
	public static final TelephoneType TEXTPHONE = new TelephoneType("textphone", VCardVersion.V4_0);

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
	 */
	public static final TelephoneType VIDEO = new TelephoneType("video");

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
	 */
	public static final TelephoneType VOICE = new TelephoneType("voice");

	/**
	 * <b>Supported versions:</b> {@code 2.1, 3.0, 4.0}
	 */
	public static final TelephoneType WORK = new TelephoneType("work");

	private TelephoneType(String value, VCardVersion... supportedVersions) {
		super(value, supportedVersions);
	}

	/**
	 * Searches for a parameter value that is defined as a static constant in
	 * this class.
	 * @param value the parameter value
	 * @return the object or null if not found
	 */
	public static TelephoneType find(String value) {
		return enums.find(value);
	}

	/**
	 * Searches for a parameter value and creates one if it cannot be found. All
	 * objects are guaranteed to be unique, so they can be compared with
	 * {@code ==} equality.
	 * @param value the parameter value
	 * @return the object
	 */
	public static TelephoneType get(String value) {
		return enums.get(value);
	}

	/**
	 * Gets all of the parameter values that are defined as static constants in
	 * this class.
	 * @return the parameter values
	 */
	public static Collection<TelephoneType> all() {
		return enums.all();
	}
}