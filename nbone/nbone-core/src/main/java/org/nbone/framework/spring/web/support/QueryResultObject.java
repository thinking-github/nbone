package org.nbone.framework.spring.web.support;

import java.util.ArrayList;
import java.util.List;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 * @since 2013-08-12
 */
public class QueryResultObject {
	
	  private int itemCount = 0;
	  private List<?> items;
	  private List<DicItems> dicItems = new ArrayList<DicItems>();

	  public List<DicItems> getDicItems() {
	    return this.dicItems;
	  }

	  public void setDicItems(List<DicItems> dicItems) {
	    this.dicItems = dicItems;
	  }

	  public int getItemCount() {
	    return this.itemCount;
	  }

	  public void setItemCount(int itemCount) {
	    this.itemCount = itemCount;
	  }

	  public List<?> getItems() {
	    return this.items;
	  }

	  public void setItems(List<?> items) {
	    this.items = items;
	  }

	  public QueryResultObject addDicItems(DicItems dicItems) {
	    this.dicItems.add(dicItems);
	    return this;
	  }

	  public QueryResultObject addDicItems(List<DicItems> dicItems) {
	    this.dicItems.addAll(dicItems);
	    return this;
	  }

	
}
