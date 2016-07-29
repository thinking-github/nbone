package org.nbone.mybatis.generator;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.nbone.mybatis.generator.util.CommentUtils;

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
	
	 private Properties properties;
	 private boolean suppressDate;
	 private boolean suppressAllComments;
	

	public CustomCommentGenerator() {
		 properties = new Properties();
	     suppressDate = false;
	     suppressAllComments = false;
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		
	        StringBuilder sb = new StringBuilder();

	        field.addJavaDocLine("/**"); //$NON-NLS-1$
	        
	        //数据库字段注释
	        sb.append(" * ");
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
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		addClassComment(innerClass, introspectedTable,false);
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		 	
			StringBuilder sb = new StringBuilder();
	        sb.append(" * This class  was database table entity mapping (ORM)"); //$NON-NLS-1$
	        sb.append(introspectedTable.getFullyQualifiedTable());

	        //addJavadocTag(innerClass, markAsDoNotDelete);

	        CommentUtils.addClassComment(innerClass, sb.toString());
	}

	@Override
	public void addConfigurationProperties(Properties properties) {
		
		this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
        
        suppressAllComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
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
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
	}

	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		
		if(compilationUnit instanceof TopLevelClass){
			TopLevelClass topLevelClass = (TopLevelClass) compilationUnit;
			StringBuilder sb = new StringBuilder();
	        sb.append(" * This class  was database table entity mapping (ORM)"); 
			CommentUtils.addClassComment(topLevelClass,sb.toString());
		}else if(compilationUnit instanceof Interface){
			Interface interface1 = (Interface) compilationUnit;
			StringBuilder sb = new StringBuilder();
	        sb.append(" * This class  was data asscss object (DAO)"); 
			CommentUtils.addClassComment(interface1,sb.toString());
			
		}
		
	}

	@Override
	public void addComment(XmlElement xmlElement) {
	}

	@Override
	public void addRootComment(XmlElement rootElement) {
	}


	
	
	
	
	
	
	

}
