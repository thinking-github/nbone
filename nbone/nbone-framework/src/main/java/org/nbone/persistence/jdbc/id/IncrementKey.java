package org.nbone.persistence.jdbc.id;

import org.nbone.persistence.exception.PersistenceIllegalArgumentException;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 */
public class IncrementKey implements IincrementKey {

	 private String keyname;
	  private String query;
	  private String update;

	  public IncrementKey(String keyname)
	  {
	    if (keyname == null) {
	      throw new PersistenceIllegalArgumentException("keyname不能为空");
	    }
	    this.keyname = keyname;
	    this.query = ("select keyvalue from keyfactory where keyname = '" + keyname + "'");

	    this.update = "update keyfactory set keyvalue = ? where keyname = ?";
	  }

	  public String getKeyname()
	  {
	    return this.keyname;
	  }

	  public void setKeyname(String keyname) {
	    this.keyname = keyname;
	  }

	  public String getQuery() {
	    return this.query;
	  }

	  public String getUpdate() {
	    return this.update;
	  }

}
