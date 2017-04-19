package org.apache.velocity.demo;

import org.apache.commons.lang.StringUtils;

public enum OrderBusinessType{
	RDPS("RDPS","RDC配送"),
	ZFSJ("ZFSJ","直发"),
	GXDB("GXDB","干线调拨"),
	TCDB("TCDB","同城调拨"),
	SFDB("SFDB","商返调拨"),
	CRDCPS("CRDCPS","CRDC配送");
	
	private String key;
	private String value;
	
	private OrderBusinessType(String key,String value) {
		this.key = key;
		this.value = value;
	}

	public static OrderBusinessType getByCode(String key){
		if(StringUtils.isNotBlank(key)){
			for (OrderBusinessType obj:OrderBusinessType.values()) {
				if(key.equals(obj.getKey())){
					return obj;
				}
			}
		}
		return null;
	}
	
	public static String getNameByCode(String key){
		OrderBusinessType en = getByCode(key);
		if(en == null){
			return null;
		}
		return en.getValue();
	}
	
	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
