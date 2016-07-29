package org.nbone.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.plugins.SerializablePlugin;


/**
 * mybatis 生成工具扩展
 * @author thinking
 * @version 1.0 
 * @see SerializablePlugin
 */
public class GeneratorExtPlugin extends PluginAdapter  {
	
	private FullyQualifiedJavaType returnList; 
	
	public GeneratorExtPlugin() {
		returnList = new FullyQualifiedJavaType("java.util.List");
		
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		
		 String recordType  = introspectedTable.getBaseRecordType();
		 FullyQualifiedJavaType  type = new FullyQualifiedJavaType(recordType) ;
		 
		 if(recordType == null || recordType.trim().equals("")){
			 List<Method>  methodList = interfaze.getMethods();
			 for (Method method : methodList) {
				 
				String methodName   =  method.getName();
				
				if("insert".equals(methodName)){
					List<Parameter> parameters = method.getParameters();
					type  = parameters.get(0).getType();
					
					if(type != null){
						break;
					}else{
						if("selectByPrimaryKey".equals(methodName)){
							type = method.getReturnType();
						}
				   }
				}
		    }
			 
		 }
		
		
		 if(type == null){
			 return false;
		 }
		
		returnList = new FullyQualifiedJavaType("java.util.List<"+ type.getShortName()+">");
		Method method = new Method("queryForList");
		method.setReturnType(returnList);
		Parameter parameter = new Parameter(type, "entity");
		method.addParameter(parameter);
		
		interfaze.addMethod(method);
		interfaze.addImportedType(returnList);
		
		
		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		
		    XmlElement parentElement = document.getRootElement();  
		    List<IntrospectedColumn> cols =  introspectedTable.getBaseColumns();
		  
	        // 产生 sql where 
	        XmlElement where = new XmlElement("sql");  
	        where.addAttribute(new Attribute("id",  "where"));  
	        where.addElement(new TextElement(" where 1=1 "));
	        
	        //条件
	        for (IntrospectedColumn introspectedColumn : cols) {
	        	String columnName  = introspectedColumn.getActualColumnName();
	        	String propertyName = introspectedColumn.getJavaProperty();
	        	//FullyQualifiedJavaType javaType =  introspectedColumn.getFullyQualifiedJavaType();
	        	String jdbcType = introspectedColumn.getJdbcTypeName();
	        	
	            XmlElement pageStart = new XmlElement("if");  
		        pageStart.addAttribute(new Attribute("test", propertyName + " != null "));  
		        StringBuilder sb = new StringBuilder(" and ");
		        sb.append(columnName).append(" = ").append("#{").append(columnName).append(",jdbcType=").append(jdbcType).append("}");
		        pageStart.addElement(new TextElement(sb.toString()));  
		        where.addElement(pageStart);  
			}
	    
	        parentElement.addElement(3,where);  
		
		return true;
	}

	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		return true;
	}
	
	
	
	
	
	
	
	
	
	

}
