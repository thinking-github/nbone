package org.nbone.web.support;

import java.util.ArrayList;
import java.util.List;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 * @since 2013-08-12
 */
public class WrapedItems {
	
	  private List<Object> items = new ArrayList<Object>();

	  private int itemCount = 0;

	  private List<DicItems> dicts = new ArrayList<DicItems>();

	  public List<DicItems> getDicts() {
	    return this.dicts;
	  }

	  public void setDicts(List<DicItems> dicts) {
	    this.dicts.addAll(dicts);
	  }

	  public void addItem(Object item) {
	    this.items.add(item);
	  }

	  public void addItems(List<Object> items) {
	    this.items.addAll(items);
	  }

	  public List<?> getItems() {
	    return this.items;
	  }

	  public void setItems(List<Object> items) {
	    this.items = items;
	  }

	  public int getItemCount() {
	    return this.itemCount;
	  }

	  public void setItemCount(int itemCount) {
	    this.itemCount = itemCount;
	  }

	  public String toString()
	  {
	    return "WrapedItems [items=" + this.items + ",itemCount = " + this.itemCount + "]";
	  }

}
