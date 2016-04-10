/**
 * 项目名称:东辆设备管理系统
 * 类名：IdUtil.java 
 * 包名：com.johe.ems.framework.util 
 * 履历：
 * 日期          修改人          内容
 * 2010-7-17	wangcg		   创建                  
 *
 * Copyright (c) 2010 西安聚合软件有限公司
 */

package org.nbone.util;

import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

/**
 * id 工具类(调用hibernate uuid)
 * @author wangcg
 */

public class IdUtil {
	
	private static final IdentifierGenerator gen = new UUIDHexGenerator();
	
	public static String getId(){
		return (String) gen.generate(null, null);
	}
	
	public static void main(String[] s){
		for(int i=0;i<100;i++){
			String id = getId();
			System.out.println(id);
		}
	}

}
