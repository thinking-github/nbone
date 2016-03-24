package org.nbone.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Thinking  2014-10-24
 *
 */
public abstract class IOUtils {
	
	/**
	 * 将输入流转化字节数组
	 * @param ins
	 * @return
	 */
	public static byte[] getByteArray(InputStream ins){
		if(ins == null){
			
			return null;
		}
	   List<Integer> byteList = new ArrayList<Integer>(); 
	   byte[] result = null;
	   Integer offset;
		try {
			while ((offset=ins.read()) != -1) {
				byteList.add(offset);
			}
			 result = new byte[byteList.size()];
			 
		    for (int i = 0; i < byteList.size(); i++) {
				int temp  = byteList.get(i);
				result[i] = (byte)temp;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(ins);
		}
		return result;
	}
	
	public static byte[] getStreamAsByteArray(InputStream ins){
		return getStreamAsByteArray(ins,4096);
	}
	/**
	 * 更新转化能力
	 * @param ins
	 * @return
	 */
	public static byte[] getStreamAsByteArray(InputStream ins,int blength){
		int bufferlength = 4096;
		if(ins == null){
			return null;
		}
		if(blength < 0){
			bufferlength = 4096;
		}else{
			bufferlength = blength;
		}
		
	   byte[] result = null;
	   ByteArrayOutputStream outstream = new ByteArrayOutputStream(bufferlength);
	   byte buffer[] = new byte[bufferlength];
	   int len;
		try {
			while ((len = ins.read(buffer)) != -1) {
				outstream.write(buffer,0, len);
			}
			result = outstream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(ins);
			close(outstream);
		}
		return result;
	}
	
	/**
	 * 将输入流 输出 且关闭资源
	 * @param ins 
	 * @param os
	 */
	public static void saveWrite(InputStream ins,OutputStream os){
		if(ins == null ){
			throw new NullPointerException("InputStream[ins] is not  null.");
		}
		if(os == null ){
			throw new NullPointerException("OutputStream[os] is not  null.");
		}
		Integer offset;
		try {
			while ((offset=ins.read()) != -1) {
				    os.write(offset);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			close(os);
			close(ins);
		}	
		
	}
	
	/**
	 * 兼容以前老版本
	 * @param io
	 */
	public static void close(Object  io){
		if(io instanceof Closeable){
			Closeable closeIO = (Closeable) io;
			close(closeIO);
		}
	}
	/**
	 * 关闭 输入输出流
	 * @param io InputStream  OutputStream
	 */ 
	public static void close(Closeable io){
		if(io == null){
			return;
		}
		
		try {
			if(io instanceof InputStream){
				InputStream ins = (InputStream) io;
				ins.close();
			}else if(io instanceof OutputStream){
				OutputStream os = (OutputStream) io;
				os.flush();
				os.close();
			}else{
				io.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
