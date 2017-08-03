package org.nbone.framework.hibernate.util;

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
