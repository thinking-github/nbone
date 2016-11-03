package org.nbone.mvc.domain;

import java.io.Serializable;

import org.nbone.util.lang.ToStringUtils;

public class BaseDomain implements Serializable {

	private static final long serialVersionUID = -4034829373147888054L;
	
	public String toString() {
        return ToStringUtils.toStringMultiLine(this);
    }
	//方法有问题导致 one to many 加载时比较set里的hashcode()，先注释掉
    /*public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }*/

}
