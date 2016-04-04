/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件基本操作类
 * 
 */
public class FileUtil {
	
	/**
	 * 根据文件length计算size
	 * 
	 * @param length 文件长度
	 * @return 文件大小
	 */
	public static String getFileLength(long length) {
		double f = Math.round((length / 1024D) * 100D) / 100D;
		if (f < 1024) {
			return f + "k";
		} else {
			f = Math.round((f / 1024D) * 100D) / 100D;
		}
		return f + "M";
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param name 文件名称
	 * @return 文件后缀名
	 */
	public static String getExtension(String name) {
		int i = name.lastIndexOf('.');
		return i >= 0 ? name.substring(i + 1).toLowerCase() : "";
	}

	/**
	 * 判断给定的路径名是否为目录
	 * 
	 * @param name 文件路径
	 * @return true=目录 false=非目录
	 */
	public boolean isDir(String name) {
		return (new File(name)).isDirectory();
	}

	/**
	 * 获取给定路径名的文件名
	 * 
	 * @param path 文件路径
	 * @return 文件名
	 */
	public static String getFileName(String path) {
		try {
			char separatorChar = '/';
			int index = path.lastIndexOf(separatorChar);
			if (index < 0) {
				separatorChar = '\\';
				index = path.lastIndexOf(separatorChar);
			}
			return path.substring(index + 1);
		} catch (Exception exception) {
			return "Unknown";
		}
	}

	/**
	 * 将给定的字符串转换成系统文件分隔符
	 * 
	 * @param path 文件路径
	 * @return 系统文件分隔符的文件路径
	 */
	public static String formatPath(String path) {
		return path.replace("\\\\", File.separator).replace("/", File.separator);
	}

	/**
	 * 删除给定文件
	 * 
	 * @param dir 文件
	 */
	public static void delete(File dir){
		if (dir.isDirectory()) {
			File[]  files = dir.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++){
				    delete(files[i]);
				}

			}
			dir.delete();
		} else {
			dir.delete();
		}
	}
	
	 /** 
     * 复制单个文件 
     * @param oldPath 原文件路径 如：c:/fqf.txt 
     * @param newPath 复制后路径 如：f:/fqf.txt 
	 * @throws IOException 输入输出异常
     */ 
   public static void copy(String oldPath, String newPath) throws IOException {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPath);
		// 文件存在时
		if (oldfile.exists()) { 
			InputStream in = null;
			FileOutputStream out = null;
			try {
			        // 读入原文件
				in = new FileInputStream(oldPath); 
				out = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = in.read(buffer)) != -1) {
				        // 字节数 文件大小
					bytesum += byteread; 
					out.write(buffer, 0, byteread);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	} 

	/**
	 * 复制文件
	 * 
	 * @param file 源文件
	 * @param target 目录文件
	 * @throws IOException 输入输出异常
	 */
	public static void copy(File file, File target) throws IOException {
		if (file.isDirectory()){
		    return;
		}
		FileInputStream fi = null;
		FileOutputStream fo = null;
		try {
			fi = new FileInputStream(file);
			fo = new FileOutputStream(target);
			byte[] buff = new byte[1024];
			for (int n = 0; (n = fi.read(buff)) != -1;){
			    fo.write(buff, 0, n);
			}
		} finally {
			if (fi != null) {
				try {
					fi.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fo != null) {
				try {
					fo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 字符串输出位置
	 * 
	 * @param str 需输出的字符串
	 * @param config 0不输出，1输出到控制台，2输出到文件,3都输出
	 * @param filepath 输出的文件路径
	 */
	public static void cout(String str, int config, String filepath) {
		switch (config) {
		case 1:
			System.out.println(str);
			break;
		case 2:
			if (makefile(filepath)) {
				try {
					// true表示可以追加输出到文件
					FileOutputStream fout = new FileOutputStream(filepath, true);
					// 输出到文件的每一行都另起一行
					String tmp = "\r\n" + str;
					fout.write(tmp.getBytes());
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		case 3:
			System.out.println(str);
			if (makefile(filepath)) {
				try {
					// true表示可以追加输出到文件
					FileOutputStream fout = new FileOutputStream(filepath, true);
					// 输出到文件的每一行都另起一行
					String tmp = "\r\n" + str;
					fout.write(tmp.getBytes());
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 一般的输出到文件log.txt放在downPath文件夹下
	 * 
	 * @param str 字符串
	 */
	public static void cout(String str) {
		cout(str, 2, "log.txt");
	}

	/**
	 * 查找文件，如果不存在，就创建
	 * 
	 * @param path 指定的文件路径
	 * @return true=创建文件成功 false=创建文件失败
	 */
	public static boolean makefile(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		} else {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	/**
	 * 查找目录，如果不存在，就创建
	 * 
	 * @param path 指定的目录路径
	 * @return true=创建文件成功 false=创建文件失败
	 */
	public static boolean makePath(String path) {
		File dirFile = new File(path);
		if (dirFile.exists()){
		    return true;
		}
		else {
			return dirFile.mkdirs(); 
		}
	}

	/**
	 * 拷贝文件夹及其所有文件,将file2所有文件拷贝到file1
	 * 
	 * @param file1 目标文件
	 * @param file2 源文件
	 * @return true=拷贝成功 false=拷贝失败
	 * @throws IOException 输入输出异常
	 */
	public static boolean copyDir(String file1, String file2)
			throws IOException {
		boolean ret = false;
		(new File(file1)).mkdirs();
		File[] file = (new File(file2)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				FileInputStream input = new FileInputStream(file[i]);
				FileOutputStream output = new FileOutputStream(file1 + "/"
						+ file[i].getName());
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			}
			if (file[i].isDirectory()) {
				copyDir(file1 + "/" + file[i].getName(), file2 + "/"
						+ file[i].getName());
			}
			ret = true;
		}
		return ret;
	}

	/**
	 * 返回匹配的字符串
	 * 
	 * @param regex 匹配的正则表达式
	 * @param source 源字符串
	 * @return 匹配字符串
	 */
	public static String getRegex(String regex, String source) {
		String des = "";
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(source);
		if (m.find()) {
			des = m.group();
		}
		return des;
	}

	/**
	 * 返回匹配的字符隔开的字符串
	 * 
	 * @param regex 匹配的正则模式
	 * @param source 源字符串
	 * @return 匹配结果13601454642
	 */
	public static String[] splitRegex(String regex, String source) {
		String[] des = null;
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		des = p.split(source);
		return des;
	}

	/**
	 * 返回匹配的字符串数组
	 * 
	 * @param regex 匹配的正则模式
	 * @param source 源字符串
	 * @return 匹配的字符串数组
	 */
	public static String[] getRegexM(String regex, String source) {
		String[] des = {};
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(source);

		int n = 0;
		while (m.find()) {
			n++;
		}
		des = new String[n];
		int i = 0;
		m.find(0);
		do {
			des[i] = m.group();
			i++;
		} while (m.find());
		
		return des;
	}

	/**
	 * 替换匹配的字符串为空字符
	 * 
	 * @param regex 匹配的正则模式
	 * @param source 源字符串
	 * @return 删除匹配后的字符串 
	 */
	public static String delRegex(String regex, String source) {
		String des = null;
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(source);
		if (!m.find()){
		    des = source;
		}
		else {
			des = m.replaceAll("");
		}
		return des;
	}
	
	/**
	 * 遍历文件夹
	 *  
	 * @param strPath   文件夹路径
	 * @return  文件中文件名字 ,隔开
	 */
	public static String refreshFileList(String strPath) {
		String fileNames = "";
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null){
		    return "";
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				refreshFileList(files[i].getAbsolutePath());
			} else {
				String strFileName = files[i].getAbsolutePath().toLowerCase();
				fileNames += files[i].getName()+",";
			}
		}
		if(fileNames.indexOf(",")>-1){
			int index = fileNames.lastIndexOf(",");
			fileNames = fileNames.substring(0,index);
		}
		return fileNames;
	}
	
	/**
	 * 读取properties文件中的中文键值
	 * @param propertiesFileName 属性文件
	 * @param key 键名
	 * @return 中文键值
	 */
	public static String readCnProperties(String propertiesFileName,String key) {
		File file = new File(propertiesFileName);
		BufferedReader input;
		String value="";
		try {
			input = new BufferedReader(new FileReader(file));
			String text;
			while ((text = input.readLine()) != null){
				if(text.indexOf("=")>-1){
					String[] textArr=text.split("=");				
					if(key.equals(textArr[0].trim())){
						value=new String(textArr[1].trim().getBytes(),"UTF-8");
						break;
					}
				}
			}
			input.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 读取properties文件中的中文键值
	 * @param propertiesInputStream 属性文件流
	 * @param key 键名
	 * @return 中文键值
	 */
	public static String readCnProperties(InputStream propertiesInputStream,String key) {
		BufferedReader input;
		String value="";
		try {
			input = new BufferedReader(new InputStreamReader(propertiesInputStream));
			String text;
			while ((text = input.readLine()) != null){
				if(text.indexOf("=")>-1){
					String[] textArr=text.split("=");				
					if(key.equals(textArr[0].trim())){
						value=new String(textArr[1].trim().getBytes(),"UTF-8");
						break;
					}
				}
			}
			input.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 解压zip文件
	 * 
	 * @param sourcefiles 源文件
	 * @param baseDir 基路径
	 * @throws IOException 输入输出异常
	 */
	public static void unzip(String[] sourcefiles, String baseDir)
			throws IOException {
		if (sourcefiles == null){
		    return;
		}
		ZipInputStream zis = null;
		FileInputStream fis = null;
		CheckedInputStream cis = null;
		byte[] buf = new byte[1024];
		FileOutputStream out = null;
		try {
			File base = new File(baseDir);
			if (!base.isDirectory()){
			    base.mkdir();
			}
			for (int i = 0; i < sourcefiles.length; i++) {
				fis = new FileInputStream(sourcefiles[i]);
				cis = new CheckedInputStream(fis, new Adler32());
				zis = new ZipInputStream(cis);
				for (ZipEntry entry = null; (entry = zis.getNextEntry()) != null;) {
					File f = new File(base, entry.getName());
					if (entry.isDirectory()) {
						if (!f.mkdirs()){
						    throw new IllegalArgumentException("Can't make dir:" + f);
						}
					} else {
						if (!f.getParentFile().exists()){
						    f.getParentFile().mkdirs();
						}
						out = new FileOutputStream(f);
						for (int n = 0; (n = zis.read(buf)) != -1;){
						    out.write(buf, 0, n);
						}
						out.close();
					}
				}

			}

		} finally {
			try {
				if (out != null){
				    out.close();
				}
			} catch (Exception exception1) {
			    exception1.printStackTrace();
			}
			try {
				if (zis != null){
				    zis.close();
				}
				if (cis != null){
				    cis.close();
				}
				fis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * 增量打包
	 * 
	 * @param zos ZIP输出流
	 * @param buf 增量打包字节数组
	 * @param baseDir 基本文件夹
	 * @param f 入口名称
	 * @throws IOException 输入输出异常
	 */
	private static void increasedZip(ZipOutputStream zos, byte[] buf,
			String baseDir, String f) throws IOException {
		File file = new File(baseDir + "/" + f);
		if (!file.exists()){
		    return;
		}
		if (file.isDirectory()) {
			String[] children = file.list();
			if (children != null) {
				for (int i = 0; i < children.length; i++){
				    increasedZip(zos, buf, baseDir, f + "/" + children[i]);
				}
			}
		} else {
			ZipEntry zipentry = new ZipEntry(f);
			zos.putNextEntry(zipentry);
			FileInputStream fis = new FileInputStream(file);
			int j;
			while ((j = fis.read(buf)) > -1){
			    zos.write(buf, 0, j);
			}
			fis.close();
			zos.flush();
		}
	}
	/**
	 * 打包文件
	 * 
	 * @param outFileName 输出文件夹名称
	 * @param baseDir 基文件夹
	 * @param sourcefiles 源文件路径
	 */
	public static void zip(String outFileName, String baseDir,
			String[] sourcefiles) {
		ZipOutputStream zos = null;
		FileOutputStream fos = null;
		CheckedOutputStream cos = null;
		byte[] buf = new byte[1024];
		try {
			fos = new FileOutputStream(outFileName);
			cos = new CheckedOutputStream(fos, new Adler32());
			zos = new ZipOutputStream(cos);
			for (int i = 0; i < sourcefiles.length; i++){
			    increasedZip(zos, buf, baseDir, sourcefiles[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zos != null){
				    zos.close();
				}
				if (cos != null){
				    cos.close();
				}
				fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 下载文件
	 * 
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param downLoadFiles 需要下载的文件集合
	 * @throws IOException 输入输出异常
	 */
	public static void downLoadFile(HttpServletRequest request,
			HttpServletResponse response, Vector downLoadFiles) throws IOException {
		String filename = "";
		for (int i = 0; i < downLoadFiles.size(); i++) {
			String filePath = (String) downLoadFiles.elementAt(i);
			int index = filePath.lastIndexOf("\\");
			filename = filePath.substring(index + 1);
			
			FileInputStream in = null;
			OutputStream out = null;
			try {
				in = new FileInputStream(filePath);
				// 解决中文文件名乱码问题
				if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){
				    // firefox浏览器
				    filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
				}else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){
				    // IE浏览器
				    filename = URLEncoder.encode(filename, "UTF-8");
				}
				if (!response.isCommitted()){
				    response.reset();
				}
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition", "attachment;filename=" + filename);
				int len = 0;
				byte[] bb = new byte[1024];
				out = response.getOutputStream();
				while ((len = in.read(bb)) != -1) {
					out.write(bb, 0, len);
				}
				
			} catch (IOException e) {
				throw e;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}