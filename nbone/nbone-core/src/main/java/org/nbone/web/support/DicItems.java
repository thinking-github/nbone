package org.nbone.web.support;

import java.util.List;
import java.util.Map;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 * @since 2013-08-12
 */
public class DicItems {
	
  private String name;
  private List<Map<String, String>> values;

  public String getName()
  {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Map<String, String>> getValues() {
    return this.values;
  }

  public void setValues(List<Map<String, String>> values) {
    this.values = values;
  }

}
