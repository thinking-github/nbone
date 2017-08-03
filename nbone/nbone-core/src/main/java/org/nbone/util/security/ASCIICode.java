package org.nbone.util.security;

public interface ASCIICode {
	
	/**
	 * byte
	 */
	public static final byte[] BASE64_EncodingTable = { (byte) 'A', (byte) 'B',
		(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
		(byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
		(byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
		(byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
		(byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
		(byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
		(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k',
		(byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
		(byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
		(byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
		(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
		(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
		(byte) '+', (byte) '/' };
	/**
	 * char
	 */
	public static final char BASE64_EncodingArray[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
        '8', '9', '+', '/'
    };
	
	//public static final byte[] decodingTable = {};
	
	public static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };
	 
	
	

}
