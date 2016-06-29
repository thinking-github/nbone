/**
 * Copyright 2011-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */	  

package org.nbone.component.logger;


/**
 * 操作类型定义常量类
 * <p/>
 * 
 * @author Thinking  2014-8-8
 * @see OperateType
 */

public class LogOperateType {
	/**
	 * 查询
	 */
	public static final int QUERY = 0;
	public static final String QUERY_TEXT = "查询";
	/**
	 * 创建
	 */
    public static final int CREATE = 1;
    public static final String CREATE_TEXT = "创建";
    /**
     * 删除
     */
    public static final int DELETE = 2;
    public static final String DELETE_TEXT = "删除";
    /**
     * 修改
     */
    public static final int UPDATE = 3;
    public static final String UPDATE_TEXT = "修改";
    
    public static String getOperateTypeText(int operTypeId) {
    	String resultStr = "";
    	switch (operTypeId) {
		case QUERY:
			resultStr = QUERY_TEXT;
			break;
		case CREATE:
			resultStr = CREATE_TEXT;
			break;
		case DELETE:
			resultStr = DELETE_TEXT;
			break;
		case UPDATE:
			resultStr = UPDATE_TEXT;
			break;
		default:
			resultStr = "未知";
			break;
		}
    	return resultStr;
    }
}
