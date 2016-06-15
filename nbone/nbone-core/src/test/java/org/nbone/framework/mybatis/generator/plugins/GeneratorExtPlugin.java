package org.nbone.framework.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
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
		
		 FullyQualifiedJavaType  type = null ;
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
		
		
		
		return true;
	}

	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		return true;
	}
	
	
	
	
	
	
	
	
	
	

}
