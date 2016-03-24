package org.nbone.util.security;


/**
 * @author Thinking  2014-04-26
 * 
 */

public class Base64UtilForUniCode extends AbstractASCII {
	private static final byte[] encodingTable = BASE64_EncodingTable;
	private static final byte[] decodingTable = BASE64_DecodingTable;

	public static byte[] encode(byte[] data, int offset) {

		byte[] bytes;

		int realCount = data.length - offset;

		int modulus = realCount % 3;

		if (modulus == 0) {

			bytes = new byte[(4 * realCount) / 3];

		} else {

			bytes = new byte[4 * ((realCount / 3) + 1)];

		}

		int dataLength = (data.length - modulus);

		int a1;

		int a2;

		int a3;

		for (int i = offset, j = 0; i < dataLength; i += 3, j += 4) {

			a1 = data[i] & 0xff;

			a2 = data[i + 1] & 0xff;

			a3 = data[i + 2] & 0xff;

			bytes[j] = encodingTable[(a1 >>> 2) & 0x3f];

			bytes[j + 1] = encodingTable[((a1 << 4) | (a2 >>> 4)) & 0x3f];

			bytes[j + 2] = encodingTable[((a2 << 2) | (a3 >>> 6)) & 0x3f];

			bytes[j + 3] = encodingTable[a3 & 0x3f];

		}

		int b1;

		int b2;

		int b3;

		int d1;

		int d2;

		switch (modulus) {

		case 0: /* nothing left to do */

			break;

		case 1:

			d1 = data[data.length - 1] & 0xff;

			b1 = (d1 >>> 2) & 0x3f;

			b2 = (d1 << 4) & 0x3f;

			bytes[bytes.length - 4] = encodingTable[b1];

			bytes[bytes.length - 3] = encodingTable[b2];

			bytes[bytes.length - 2] = (byte) '=';

			bytes[bytes.length - 1] = (byte) '=';

			break;

		case 2:

			d1 = data[data.length - 2] & 0xff;

			d2 = data[data.length - 1] & 0xff;

			b1 = (d1 >>> 2) & 0x3f;

			b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;

			b3 = (d2 << 2) & 0x3f;

			bytes[bytes.length - 4] = encodingTable[b1];

			bytes[bytes.length - 3] = encodingTable[b2];

			bytes[bytes.length - 2] = encodingTable[b3];

			bytes[bytes.length - 1] = (byte) '=';

			break;

		}

		return bytes;

	}

	public static byte[] decode(byte[] data) {

		byte[] bytes;

		byte b1;

		byte b2;

		byte b3;

		byte b4;

		data = discardNonBase64Bytes(data);

		if (data[data.length - 2] == '=') {

			bytes = new byte[(((data.length / 4) - 1) * 3) + 1];

		} else if (data[data.length - 1] == '=') {

			bytes = new byte[(((data.length / 4) - 1) * 3) + 2];

		} else {

			bytes = new byte[((data.length / 4) * 3)];

		}

		for (int i = 0, j = 0; i < (data.length - 4); i += 4, j += 3) {

			b1 = decodingTable[data[i]];

			b2 = decodingTable[data[i + 1]];

			b3 = decodingTable[data[i + 2]];

			b4 = decodingTable[data[i + 3]];

			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));

			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));

			bytes[j + 2] = (byte) ((b3 << 6) | b4);

		}

		if (data[data.length - 2] == '=') {

			b1 = decodingTable[data[data.length - 4]];

			b2 = decodingTable[data[data.length - 3]];

			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));

		} else if (data[data.length - 1] == '=') {

			b1 = decodingTable[data[data.length - 4]];

			b2 = decodingTable[data[data.length - 3]];

			b3 = decodingTable[data[data.length - 2]];

			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));

			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));

		} else {

			b1 = decodingTable[data[data.length - 4]];

			b2 = decodingTable[data[data.length - 3]];

			b3 = decodingTable[data[data.length - 2]];

			b4 = decodingTable[data[data.length - 1]];

			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));

			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));

			bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);

		}

		return bytes;

	}

	public static byte[] decode(String data) {

		byte[] bytes;

		byte b1;

		byte b2;

		byte b3;

		byte b4;

		data = discardNonBase64Chars(data);

		if (data.charAt(data.length() - 2) == '=') {

			bytes = new byte[(((data.length() / 4) - 1) * 3) + 1];

		} else if (data.charAt(data.length() - 1) == '=') {

			bytes = new byte[(((data.length() / 4) - 1) * 3) + 2];

		} else {

			bytes = new byte[((data.length() / 4) * 3)];

		}

		for (int i = 0, j = 0; i < (data.length() - 4); i += 4, j += 3) {

			b1 = decodingTable[data.charAt(i)];

			b2 = decodingTable[data.charAt(i + 1)];

			b3 = decodingTable[data.charAt(i + 2)];

			b4 = decodingTable[data.charAt(i + 3)];

			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));

			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));

			bytes[j + 2] = (byte) ((b3 << 6) | b4);

		}

		if (data.charAt(data.length() - 2) == '=') {

			b1 = decodingTable[data.charAt(data.length() - 4)];

			b2 = decodingTable[data.charAt(data.length() - 3)];

			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));

		} else if (data.charAt(data.length() - 1) == '=') {

			b1 = decodingTable[data.charAt(data.length() - 4)];

			b2 = decodingTable[data.charAt(data.length() - 3)];

			b3 = decodingTable[data.charAt(data.length() - 2)];

			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));

			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));

		} else {

			b1 = decodingTable[data.charAt(data.length() - 4)];

			b2 = decodingTable[data.charAt(data.length() - 3)];

			b3 = decodingTable[data.charAt(data.length() - 2)];

			b4 = decodingTable[data.charAt(data.length() - 1)];

			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));

			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));

			bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);

		}

		return bytes;

	}

	private static byte[] discardNonBase64Bytes(byte[] data) {

		byte[] temp = new byte[data.length];

		int bytesCopied = 0;

		for (int i = 0; i < data.length; i++) {

			if (isValidBase64Byte(data[i])) {

				temp[bytesCopied++] = data[i];

			}

		}

		byte[] newData = new byte[bytesCopied];

		System.arraycopy(temp, 0, newData, 0, bytesCopied);

		return newData;

	}

	private static String discardNonBase64Chars(String data) {

		StringBuffer sb = new StringBuffer();

		int length = data.length();

		for (int i = 0; i < length; i++) {

			if (isValidBase64Byte((byte) (data.charAt(i)))) {

				sb.append(data.charAt(i));

			}

		}

		return sb.toString();

	}

	private static boolean isValidBase64Byte(byte b) {

		if (b == '=') {

			return true;

		} else if ((b < 0) || (b >= 128)) {

			return false;

		} else if (decodingTable[b] == -1) {

			return false;

		}

		return true;

	}

	public static String encode(String data, String charset) throws Exception

	{

		// byte[] result = (data.getBytes("Unicode"));

		if (data == null || data.length() == 0)
			return data;

		int offset = 0;

		// getBytes("unicode")转完后会在前头加上两字节”FE“

		byte[] result = encode(data.getBytes(charset), offset);

		StringBuffer sb = new StringBuffer(result.length);

		for (int i = 0; i < result.length; i++)
			sb.append((char) result[i]);

		return sb.toString();

	}

	public static String decode(String data, String charset) throws Exception

	{

		if (data == null || data.length() == 0)
			return data;

		return new String(decode(data), charset);

	}

	public static void main(String[] args) throws Exception {

		String data = "我们";

		String data1 = encode(data, "Unicode");

		String data2 = decode(data1, "Unicode");

		System.out.println(data);

		System.out.println(data1);

		System.out.println(data2);

	}

}
