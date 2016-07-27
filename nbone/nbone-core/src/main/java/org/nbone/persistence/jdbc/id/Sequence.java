package org.nbone.persistence.jdbc.id;

import org.nbone.persistence.exception.PersistenceIllegalArgumentException;

/**
 * @author uap
 * @author thinking
 * @version 1.0 
 */
public class Sequence {
	
	  private String sequenceName;
	  private String query;

	  public String getSequenceName()
	  {
	    return this.sequenceName;
	  }

	  public void setSequenceName(String sequenceName) {
	    this.sequenceName = sequenceName;
	  }

	  public String toString() {
	    if (this.sequenceName == null) {
	      throw new PersistenceIllegalArgumentException("序列名不能为空！");
	    }
	    this.query = ("select " + this.sequenceName + ".nextval from dual");
	    return this.query;
	  }

}
