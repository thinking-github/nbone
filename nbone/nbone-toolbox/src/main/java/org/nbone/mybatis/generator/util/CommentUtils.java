package org.nbone.mybatis.generator.util;

import java.sql.Date;

import org.mybatis.generator.api.dom.java.JavaElement;

/**
 * @author thinking
 * @version 1.0 
 */
public class CommentUtils {
	
	public static void addClassComment(JavaElement element,String innerString){
		element.addJavaDocLine("/**"); 
		if(innerString != null){
			element.addJavaDocLine(innerString);
		}
        element.addJavaDocLine(" * @author " + System.getProperty("user.name","thinking9"));
        element.addJavaDocLine(" * @since " + new Date(System.currentTimeMillis()));

        //addJavadocTag(innerClass, markAsDoNotDelete);

        element.addJavaDocLine(" */"); //$NON-NLS-1$
		
	}
	
	
	
	

}
