package org.nbone.framework.mybatis.generator;

import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultCommentGenerator;

/**
 * 
 * <p>Description: mybatis Generator Comment </p>
 * @author  thinking
 * @version 1.0 
 * @since  2016年3月30日
 * @see DefaultCommentGenerator
 * @see CommentGenerator
 */
public class CustomCommentGenerator implements CommentGenerator {

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		
	        StringBuilder sb = new StringBuilder();

	        field.addJavaDocLine("/**"); //$NON-NLS-1$
	        
	        //数据库字段注释
	        sb.append(" * 数据库字段 ");
	        sb.append(introspectedTable.getFullyQualifiedTable());
	        sb.append('.');
	        sb.append(introspectedColumn.getActualColumnName());
	        
	        String remarks  = introspectedColumn.getRemarks();
	        if(remarks != null && remarks.trim().length() > 0){
	        	sb.append(" ");
	        	sb.append(remarks);
	        }
	        
	        field.addJavaDocLine(sb.toString());
	      
	        field.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
	}

	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
	}

	@Override
	public void addConfigurationProperties(Properties properties) {
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
	}

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
	}

	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
	}

	@Override
	public void addComment(XmlElement xmlElement) {
	}

	@Override
	public void addRootComment(XmlElement rootElement) {
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
