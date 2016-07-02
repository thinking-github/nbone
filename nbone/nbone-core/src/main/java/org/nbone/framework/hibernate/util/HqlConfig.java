package org.nbone.framework.hibernate.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nbone.persistence.SqlConfig;
/**
 * 
 * @author thinking
 * @version 1.0 
 */
@SuppressWarnings("rawtypes")
public class HqlConfig  extends SqlConfig{
	
	    
		private String aliasName = " tempA";  
		
		
		/**
		 * 高级时使用
		 */
	    private Map<String,HqlPropertyDescriptor> hqlPds = new HashMap<String, HqlPropertyDescriptor>();
	    /**
	     * 此实体Bean 引用其他的实体Bean列表
	     */
	    private List<Class<?>> pojoRefs;
	    /**
	     * 默认为初级
	     */
	    private int hqlMode = PrimaryMode;
	   
	    /**
	     * 中级时使用
	     */
	    //number String key/value
	    private Map<String,String> inNumStrMap;
	    private Map<String,String> notinNumStrMap;
	    //String key
	    private String[] inStringFields;
	    private String[] notinStringFields;
	    private Map<String,String> inStringFieldsMap;
	    private Map<String,String> notinStringFieldsMap;
	    
	   
		private List dtField;
        /**
         * 初中高级通用
         */
	    private String[] orderFieldASC;
	    private String[] orderFieldDESC;
    
	    public HqlConfig(){
	    	this.hqlMode = PrimaryMode;
	    }
	    
	    public HqlConfig(String aliasName) {
			this.aliasName = aliasName;
			this.hqlMode = PrimaryMode;
		}
	    
	    public HqlConfig(int hqlMode) {
			this.hqlMode = hqlMode;
		}

		/**
	     *  MiddleMode Constructor
	     * @param inStringFields
	     * @param notinStringFields
	     */
        public HqlConfig(String[] inStringFields, String[] notinStringFields) {
			this.inStringFields = inStringFields;
			this.notinStringFields = notinStringFields;
			
			this.inStringFieldsMap = stringArray2Map(inStringFields);
			this.notinStringFieldsMap = stringArray2Map(notinStringFields);
			
			this.hqlMode = MiddleMode;
		}

		/**
         * HighMode Constructor
         * @param hqlPds
         */
		public HqlConfig(Map<String,HqlPropertyDescriptor> hqlPds){
	    	this.addHqlPds(hqlPds);
	    	this.hqlMode = HighMode;
	    }
		//-------------------------------------------------------------------------
		//factory method 
		//-------------------------------------------------------------------------
		public static HqlConfig getHqlConfig(int mode){
			return new HqlConfig(mode);
		}
		
		public static HqlConfig getHighMode(){
			return new HqlConfig((Map<String,HqlPropertyDescriptor>)null);
		}
		
		public static HqlConfig getMiddleMode(String[] inStringFields, String[] notinStringFields){
			return new HqlConfig(inStringFields, notinStringFields);
		}
		

		//-------------------------------------------------------------------------
		//特殊方法
		//-------------------------------------------------------------------------
		public Map<String,String> stringArray2Map(String[] strArray){
			Map<String,String>   resultMap = new HashMap<String, String>();
			if(strArray != null){
				for (int i = 0; i < strArray.length; i++) {
					String did = strArray[i];
					resultMap.put(did, did);
				}
			}
			//XXX to MiddleMode
			this.hqlMode = MiddleMode;
			return resultMap;
		}

		public void addHqlPd(HqlPropertyDescriptor hqlPd) {
			if(hqlPd == null){
				return;
			}
			String key = hqlPd.getFieldName();
			this.hqlPds.put(key, hqlPd);
		}
		
		public void addHqlPds(HqlPropertyDescriptor[] hqlPds) {
			if(hqlPds == null){
				return;
			}
			for (int i = 0; i < hqlPds.length; i++) {
				addHqlPd(hqlPds[i]);
			}
		}
		
		public void addHqlPds(Map<String,HqlPropertyDescriptor> hqlPds) {
			if(hqlPds == null){
				return;
			}
			this.hqlPds.putAll(hqlPds);
		}
		public void removeHqlPd(HqlPropertyDescriptor hqlPd) {
			if(hqlPd == null){
				return;
			}
			String key = hqlPd.getFieldName();
			this.hqlPds.remove(key);
		}
		
		//-------------------------------------------------------------------------
		//seter / geter
		//-------------------------------------------------------------------------
		public String getAliasName() {
			return aliasName;
		}

		public void setAliasName(String aliasName) {
			this.aliasName = aliasName;
		}


		public Map<String,HqlPropertyDescriptor> getHqlPds() {
			return hqlPds;
		}

		public List<Class<?>> getPojoRefs() {
			return pojoRefs;
		}

		public void setPojoRefs(List<Class<?>> pojoRefs) {
			this.pojoRefs = pojoRefs;
		}


		public int getHqlMode() {
			return hqlMode;
		}

		public void setHqlMode(int hqlMode) {
			this.hqlMode = hqlMode;
		}


		public Map<String, String> getInNumStrMap() {
			return inNumStrMap;
		}

		public void setInNumStrMap(Map<String, String> inNumStrMap) {
			this.inNumStrMap = inNumStrMap;
		}

		public Map<String, String> getNotinNumStrMap() {
			return notinNumStrMap;
		}

		public void setNotinNumStrMap(Map<String, String> notinNumStrMap) {
			this.notinNumStrMap = notinNumStrMap;
		}

		public String[] getInStringFields() {
			return inStringFields;
		}

		public void setInStringFields(String[] inStringFields) {
			this.inStringFields = inStringFields;
			//XXX: stringArray2Map
			this.inStringFieldsMap = this.stringArray2Map(inStringFields);
			this.setHqlMode(MiddleMode);
		}

		public String[] getNotinStringFields() {
			return notinStringFields;
		}

		public void setNotinStringFields(String[] notinStringFields) {
			this.notinStringFields = notinStringFields;
			//XXX: stringArray2Map
			this.notinStringFieldsMap = this.stringArray2Map(notinStringFields);
			this.setHqlMode(MiddleMode);
		}

		public Map<String, String> getInStringFieldsMap() {
			return inStringFieldsMap;
		}

		public void setInStringFieldsMap(Map<String, String> inStringFieldsMap) {
			this.inStringFieldsMap = inStringFieldsMap;
		}

		public Map<String, String> getNotinStringFieldsMap() {
			return notinStringFieldsMap;
		}

		public void setNotinStringFieldsMap(Map<String, String> notinStringFieldsMap) {
			this.notinStringFieldsMap = notinStringFieldsMap;
		}

		public List getDtField() {
			return dtField;
		}

		public void setDtField(List dtField) {
			this.dtField = dtField;
		}

		public String[] getOrderFieldASC() {
			return orderFieldASC;
		}

		public void setOrderFieldASC(String[] orderFieldASC) {
			this.orderFieldASC = orderFieldASC;
		}

		public String[] getOrderFieldDESC() {
			return orderFieldDESC;
		}

		public void setOrderFieldDESC(String[] orderFieldDESC) {
			this.orderFieldDESC = orderFieldDESC;
		}

	    
		
	


}
