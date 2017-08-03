/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 * 文件上传类(http方式)
 *
 */
public class HttpFileUpload {
	/**
	 * 类型集合
	 */
	private List types=new ArrayList();
	
	/**
	 * 文件最大大小
	 */
	private long sizeMax;
	
	/**
	 * 极限大小
	 */
	private int sizeThreshold;
	
	/**
	 * 字符编码 
	 */
	private String encoding;
	
	/**
	 * 存放目录 
	 */
	private String repositoryPath;
	
	/**
	 * 文件名 
	 */
	private String fileName;
	
	/**
	 * 获得限制上传和下载的文件类型
	 * 
	 * @return 文件类型
	 */
	public List getTypes() {
		return types;
	}
	
	/**
	 * 设置限制上传和下载的文件类型
	 * 
	 * @param types 文件类型
	 */
	public void setTypes(List types) {
		this.types = types;
	}
	
	/**
	 * 获得请求消息实体内容的最大允许大小
	 * 
	 * @return the 文件最大允许大小
	 */
	public long getSizeMax() {
		return sizeMax;
	}
	
	/**
	 * 设置请求消息实体内容的最大允许大小(配置允许用户上传文档大小，单位：字节)
	 * 
	 * @param sizeMax 文件最大允许大小
	 */
	public void setSizeMax(long sizeMax) {
		this.sizeMax = sizeMax;
	}
	
	/**
	 * 获得使用临时文件保存解析出的数据的那个临界值
	 * 
	 * @return 极限字节值
	 */
	public int getSizeThreshold() {
		return sizeThreshold;
	}
	
	/**
	 * 设置使用临时文件保存解析出的数据的那个临界值
	 * 配置最多只允许在内存中存储的数据，单位：字节）
	 * 
	 * @param sizeThreshold 极限字节值
	 */
	public void setSizeThreshold(int sizeThreshold) {
		this.sizeThreshold = sizeThreshold;
	}
	
	/**
	 * 获得字符集
	 * 
	 * @return 字符集
	 */
	public String getEncoding() {
		return encoding;
	}
	
	/**
	 * 设置字符集
	 * 
	 * @param encoding 字符集
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	/**
	 * 获得临时文件的存放目录
	 * @return 文件目录
	 */
	public String getRepositoryPath() {
		return repositoryPath;
	}
	
	/**
	 * 设置临时文件的存放目录
	 * 
	 * @param repositoryPath 文件目录
	 */
	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}
	
	/**
	 * 获得设置的指定文件名称
	 * 
	 * @return 文件名称
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * 设置指定文件名称
	 * 
	 * @param fileName 文件名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 将上传的文件放到当前目录下
	 *
	 * @param request HttpServletRequest对象
	 * @param fileName 指定文件名（数组） 
	 * @return List 返回文件名集合
	 * @throws Exception 抛出异常
	 */
	public List uploadFile2Folder(HttpServletRequest request,String[] fileName) throws Exception {
		return uploadFile2Folder(request,"",fileName);
	}
	
	/**
	 * 将上传的文件放到指定的目录下
	 * @param request HttpServletRequest对象
	 * @param sourceFolder 指定文件夹
	 * @param fileName 指定文件名,包含扩展名（数组）
	 * @return List 返回文件名集合
	 * @throws Exception 抛出异常
	 */
	public List uploadFile2Folder(HttpServletRequest request,String sourceFolder,String[] fileName)throws Exception{
		DiskFileItemFactory factory = new DiskFileItemFactory();
		if(sizeThreshold!=0){
			factory.setSizeThreshold(sizeThreshold);
		}
		if(repositoryPath!=null){
			factory.setRepository(new File(repositoryPath));
		}
		ServletFileUpload upload = new ServletFileUpload(factory);
		if(sourceFolder==null){
		    sourceFolder="";
		}
		if(sizeMax!=0){
			upload.setSizeMax(sizeMax);
		}
		if(encoding!=null){
			upload.setHeaderEncoding(encoding);
		}
		
		//若指定文件加不存在,则创建
		List list = new ArrayList();
		File file=new File(sourceFolder);
		if(!file.exists()){
			file.mkdir();
		}
		//开始读取上传信息 
		List fileItems = upload.parseRequest(request); 
		if(fileItems!=null&&fileItems.size()!=0){
			Iterator it=fileItems.iterator();
			int i=0;
			while(it.hasNext()){
				FileItem fi=(FileItem)it.next();
				if(!fi.isFormField()){  
				        //忽略其他不是文档域的任何表单信息
					String name=fi.getName();
					String nameDir=name;
					if(name.lastIndexOf("\\")>=0){
						nameDir=name.substring(name.lastIndexOf("\\"));
					}
					String type=null;
					if(nameDir.lastIndexOf(".")>=0){
						type=nameDir.substring(nameDir.lastIndexOf(".")+1);
					}
					//自己指定文件名 
					if(fileName!=null){
						if(fileName[i]!=null){
						    nameDir=fileName[i];
						}
						i++;
					}
					//判断是否设定了上传的文件类型
					if(type!=null&&!isHave(types,type)){
						File f=new File(sourceFolder+nameDir);
						fi.write(f);
						list.add(sourceFolder+nameDir);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 判断上传文件的类型
	 * 
	 * @param types 文件类型集合
	 * @param type 类型
	 * @return true=存在文件类型 false=不存在的文件类型
	 */
	public boolean isHave(List types,String type){
		if(types==null||types.size()==0){
		    return false;
		}
		Iterator it=types.iterator();
		while(it.hasNext()){
			String ty=(String)it.next();
			if(ty.equalsIgnoreCase(type)){
			    return true;
			}
		}
		return false;
	}
	
	/**
	 * 删除磁盘上指定的文件
	 * 
	 * @param filename 指定文件
	 */
	public void deleteFileInDisk(String filename)
	{
		File f=new File(filename);
		if(f.exists()){
		    f.delete();
		}
	}
}
