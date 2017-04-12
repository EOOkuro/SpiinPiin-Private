/**
 * 
 */
package com.gotanyalo.spiinpiin.core.data;

// TODO: Auto-generated Javadoc
/**
 * The Enum Gender.
 *
 * @author otkoth
 */
public enum Gender {

	/** The MALE. */
	MALE,
	
	/** The FEMALE. */
	FEMALE,
	
	/** The BOTH. */
	BOTH,
	
	/** The NA. */
	NA {
		@Override
		public String toString(){
			return "NA";
		}
	}
}
