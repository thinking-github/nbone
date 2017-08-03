package org.nbone.web.support;

import java.util.List;

public class ListWebResultWrapper extends WebResultWrapper {
	
	private List resultValueList;

	
	
	public ListWebResultWrapper() {
	}


	public ListWebResultWrapper(boolean isSuccess, List data, String tip,String errorPage, String type) {
		super(isSuccess, data, tip, errorPage, type);
		super.setResultValue(null);
		this.setResultValueList(data);
		
	}


	public static ListWebResultWrapper successResultWraped(List data) {
		return new ListWebResultWrapper(true, data, "","","");
	}
	
	
	public List getResultValueList() {
		return resultValueList;
	}

	public void setResultValueList(List resultValueList) {
		this.resultValueList = resultValueList;
	}
	
	
	

}
