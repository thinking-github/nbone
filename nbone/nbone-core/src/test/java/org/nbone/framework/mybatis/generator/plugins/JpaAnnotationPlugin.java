package org.nbone.framework.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.plugins.SerializablePlugin;

/**
 * Java Jpa 实体生成工具
 * @author thinking
 * @version 1.0 
 * @see SerializablePlugin
 */
public class JpaAnnotationPlugin extends PluginAdapter {

	private FullyQualifiedJavaType jpaEntity;
	private FullyQualifiedJavaType jpaTable;
	private FullyQualifiedJavaType jpaColumn;
	private FullyQualifiedJavaType jpaId;
	private FullyQualifiedJavaType jpaGeneratedValue;
	private FullyQualifiedJavaType jpaSequenceGenerator;
	
	
	public JpaAnnotationPlugin() {
		jpaEntity 		     = new FullyQualifiedJavaType("javax.persistence.Entity");
		jpaTable  		     = new FullyQualifiedJavaType("javax.persistence.Table");
		jpaColumn 		     = new FullyQualifiedJavaType("javax.persistence.Column");
		jpaId    		     = new FullyQualifiedJavaType("javax.persistence.Id");
		jpaGeneratedValue    = new FullyQualifiedJavaType("javax.persistence.GeneratedValue");
		jpaSequenceGenerator = new FullyQualifiedJavaType("javax.persistence.SequenceGenerator");
		
	}


	@Override
	public boolean validate(List<String> warnings) {
		 // this plugin is always valid
		return true;
	}
	

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		makeClass(topLevelClass, introspectedTable);
		return true;
	}


	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		makeClass(topLevelClass, introspectedTable);
		return true;
	}


	@Override
	public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,IntrospectedTable introspectedTable) {
		makeClass(topLevelClass, introspectedTable);
		return true;
	}


	@Override
	public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		String columnName = introspectedColumn.getActualColumnName();
		String columnNameStr = new StringBuilder("\"").append(columnName).append("\"").toString();
		
		TableConfiguration tabConfig  = introspectedTable.getTableConfiguration();
		
		//primaryKey
		List<IntrospectedColumn> primaryKeys =  introspectedTable.getPrimaryKeyColumns();
		for (IntrospectedColumn primaryColumn : primaryKeys) {
			if(primaryColumn.equals(introspectedColumn)){
				field.addAnnotation("@Id");
				field.addAnnotation("@GeneratedValue(generator="+columnNameStr+")");
				GeneratedKey genKey = tabConfig.getGeneratedKey();
				
				if(introspectedColumn.isIdentity()){
					
				}else if(introspectedColumn.isSequenceColumn()){
					field.addAnnotation("@SequenceGenerator(name="+columnNameStr+",sequenceName=\"" + genKey.getRuntimeSqlStatement() + "\")");
				}
				
				break;
			}
		}
		
		//Column mapping
		field.addAnnotation("@Column(name="+columnNameStr+")");
		
		return true;
	}
	
	
	 protected void makeClass(TopLevelClass topLevelClass,
	            IntrospectedTable introspectedTable) {
		 
		    String tableName = introspectedTable.getTableConfiguration().getTableName();
		    String tableNameStr = new StringBuilder("\"").append(tableName).append("\"").toString();
		    topLevelClass.addImportedType(jpaEntity);
			topLevelClass.addImportedType(jpaTable);
			topLevelClass.addImportedType(jpaColumn);
			topLevelClass.addImportedType(jpaId);
			topLevelClass.addImportedType(jpaGeneratedValue);
			
			topLevelClass.addAnnotation("@Entity");
			topLevelClass.addAnnotation("@Table(name="+tableNameStr+")");
		 
	 }
	
	

}
