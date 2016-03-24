package org.nbone.util.security;

public abstract class AbstractASCII implements ASCIICode {
	
	public static final byte[] BASE64_DecodingTable;
	
	private static final byte[] pem_array = BASE64_EncodingTable;
	
	static {
		BASE64_DecodingTable = new byte[128];
		for (int i = 0; i < 128; i++) {
			BASE64_DecodingTable[i] = (byte) -1;
		}
		
		/*for (int i = 'A'; i <= 'Z'; i++) {
			BASE64_DecodingTable[i] = (byte) (i - 'A');
		}
		for (int i = 'a'; i <= 'z'; i++) {
			BASE64_DecodingTable[i] = (byte) (i - 'a' + 26);
		}
		for (int i = '0'; i <= '9'; i++) {
			BASE64_DecodingTable[i] = (byte) (i - '0' + 52);
		}
		BASE64_DecodingTable['+'] = 62;
		BASE64_DecodingTable['/'] = 63;*/
		
		
		for(int j = 0; j < pem_array.length; j++)
			BASE64_DecodingTable[pem_array[j]] = (byte)j;
		 
	}
	
	

}
