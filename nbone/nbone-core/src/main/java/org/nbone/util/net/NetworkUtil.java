/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.StringTokenizer;
/**
 * 网络工具类
 */
public final class NetworkUtil {
	/**
	 * 获取系统的MAC地址
	 * @return 系统的MAC地址
	 * @throws IOException 其它系统异常
	 * @throws ParseException 解析异常
	 * @throws InterruptedException 截断异常
	 */
	public static String getMacAddress() throws IOException, ParseException, InterruptedException {
		String os = System.getProperty("os.name");

		if (os.startsWith("Windows")) {
			return windowsParseMacAddress(windowsRunIpConfigCommand());
		} else if (os.startsWith("Linux")) {
			return linuxParseMacAddress(linuxRunIfConfigCommand());
		} else if (os.startsWith("AIX")) {
			return getMacOnAix();
		} else if (os.startsWith("Unix")) {
			return getMacOnUnix();
		} else {
			throw new IOException("unknown operating system: " + os);
		}
	}

	/**
	 * 获取linux系统mac地址
	 * @param ipConfigResponse 运行ifconfig结果
	 * @return linux系统mac地址
	 * @throws ParseException 解析异常
	 * @throws UnknownHostException host地址异常
	 * @throws UnsupportedEncodingException 不支持编码异常
	 */
	public static String linuxParseMacAddress(String ipConfigResponse)throws ParseException, UnknownHostException, UnsupportedEncodingException {
		String localHost = null;
		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			throw ex;
		}

		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;

		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			boolean containsLocalHost = line.indexOf(localHost) >= 0;

			// see if line contains IP address
			if (containsLocalHost && lastMacAddress != null) {
				return lastMacAddress;
			}

			int pianyi=0;
			int macAddressPosition=-1;
			// see if line contains MAC address
			if(line.indexOf("HWaddr")>0){
				macAddressPosition = line.indexOf("HWaddr");
				pianyi=6;
			}else{
				try {
					line=new String(line.getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					throw e;
				}
				macAddressPosition = line.indexOf("硬件地址");
				pianyi=5;
			}
			
			if (macAddressPosition <= 0){
			    continue;				
			}

			String macAddressCandidate = line.substring(macAddressPosition + pianyi)
					.trim().replace(':', '-');
			if (linuxIsMacAddress(macAddressCandidate)) {
				lastMacAddress = macAddressCandidate;
				if(line.indexOf("硬件地址")>0){
					return lastMacAddress;
				}
				continue;
			}
		}

		ParseException ex = new ParseException("cannot read MAC address for " + localHost + " from [" + ipConfigResponse + "]", 0);
		throw ex;
	}
	
	/**
	 * 判断是否为linux系统mac地址
	 * @param macAddressCandidate mac地址
	 * @return 是否为mac地址
	 */
	private static boolean linuxIsMacAddress(String macAddressCandidate) {
		// TODO: use a smart regular expression
		if (macAddressCandidate.length() != 17){
		    return false;
		}
		return true;
	}
	
	/**
	 * 获取linux系统运行ifconfig命令结果
	 * @return linux系统运行ifconfig命令结果
	 * @throws IOException io读取异常
	 */
	private static String linuxRunIfConfigCommand() throws IOException {
		Process p = Runtime.getRuntime().exec("ifconfig");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
	
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1){
			    break;
			}
			buffer.append((char) c);
		}
		String outputText = buffer.toString();

		stdoutStream.close();
		return outputText;
	}

	/**
	 * 获取windows系统mac地址
	 * @param ipConfigResponse 运行ipcofig命令结果
	 * @return windows系统mac地址
	 * @throws UnknownHostException host地址异常
	 * @throws ParseException 解析异常
	 */
	private static String windowsParseMacAddress(String ipConfigResponse) throws UnknownHostException, ParseException{
		String localHost = null;
		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			throw ex;
		}

		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;

		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();

			// see if line contains IP address
			//if (line.endsWith(localHost) && lastMacAddress != null) {
			//为了适应windows 7 将endsWith修改为contains 2009-12-31 gaoLi 修改
			if (line.contains(localHost) && lastMacAddress != null) {
				return lastMacAddress;
			}

			// see if line contains MAC address
			int macAddressPosition = line.indexOf(":");
			if (macAddressPosition <= 0){
			    continue;
			}
			String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
			if (windowsIsMacAddress(macAddressCandidate)) {
				lastMacAddress = macAddressCandidate;
				continue;
			}
		}

		ParseException ex = new ParseException("cannot read MAC address from ["	+ ipConfigResponse + "]", 0);
		throw ex;
	}
	
	/**
	 * 判断是否为window系统mac地址
	 * @param macAddressCandidate mac地址
	 * @return 是否为window系统mac地址
	 */
	private static boolean windowsIsMacAddress(String macAddressCandidate) {
		// TODO: use a smart regular expression
		if (macAddressCandidate.length() != 17){
		    return false;
		}
		return true;
	}
	
	/**
	 * 获取window系统运行ipconfig /all命令结果
	 * @return window系统运行ipconfig /all命令结果
	 * @throws IOException io读取异常
	 */
	private static String windowsRunIpConfigCommand() throws IOException {
		Process p = Runtime.getRuntime().exec("ipconfig /all");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1){
			    break;
			}
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		stdoutStream.close();
		return outputText;
	}
	
	/**
	 * 获取aix系统mac地址
	 * @return aix系统mac地址
	 * @throws IOException io读取异常
     * @throws InterruptedException 截断异常
	 */
    private static String getMacOnAix() throws IOException, InterruptedException {
        String s = "";
        
        String s1 = "netstat -i";
        Process process = Runtime.getRuntime().exec(s1);
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = bufferedreader.readLine();
        if (line != null) {
            String nextLine = bufferedreader.readLine();
            if (nextLine.indexOf("link#2") > 0){
                String[] datas = nextLine.split(" ");
                if(datas[11].length()==16){
                	s = "0" + datas[11];
                }else{
                	s=datas[11];
                }
            }
        }
        bufferedreader.close();
        process.waitFor();
        
        s=s.trim().replace('.', '-');
        return s;
    }
	
    /**
     * 获取window系统mac地址
     * @return window系统mac地址
     * @throws IOException io读取异常
     * @throws InterruptedException 截断异常
     */
    private static String getMacOnWindow() throws IOException, InterruptedException {
        String s = "";
        
        String s1 = "ipconfig /all";
        Process process = Runtime.getRuntime().exec(s1);
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = bufferedreader.readLine();
        do {
            if (line == null){
                break;
            }
            String nextLine = bufferedreader.readLine();
            if (line.indexOf("Physical Address") > 0) {
                int i = line.indexOf("Physical Address") + 36;
                s = line.substring(i);
                break;
            }
            line = nextLine;
        } while (true);
        bufferedreader.close();
        process.waitFor();
        return s.trim();
    }
    
    /**
     * 获取unix系统mac地址
     * @return unix系统mac地址
     * @throws IOException io读取异常
     * @throws InterruptedException 截断异常
     */
    private static String getMacOnUnix() throws IOException, InterruptedException {
        String s = "";
        String s1 = "ifconfig";
        Process process = Runtime.getRuntime().exec(s1);
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = bufferedreader.readLine();
        do {
            if (line == null){
                break;
            }
            String nextLine = bufferedreader.readLine();
            if (line.indexOf("HWaddr") > 0) {
                int i = line.indexOf("HWaddr") + 7;
                s = line.substring(i);
                break;
            }
            line = nextLine;
        } while (true);
        bufferedreader.close();
        process.waitFor();
        return s.trim().replace(':', '-');
    }    
}
