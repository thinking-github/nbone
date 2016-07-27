package org.nbone.persistence.jdbc.id;

public interface IincrementKey {
	
	  public abstract String getQuery();

	  public abstract String getUpdate();

	  public abstract String getKeyname();


}
