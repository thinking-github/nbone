package org.nbone.framework.mybatis.generator;

import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

public class CustomIntrospectedTable extends IntrospectedTableMyBatis3Impl {

	@Override
	protected void calculateJavaClientAttributes() {
		
	  if (context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }
	  StringBuilder sb = new StringBuilder();
	  sb.setLength(0);
      sb.append(calculateJavaClientInterfacePackage());
      sb.append('.');
      sb.append(fullyQualifiedTable.getDomainObjectName());
      sb.append("Dao"); //$NON-NLS-1$
      setMyBatis3JavaMapperType(sb.toString());
	}



}
