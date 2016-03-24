package org.nbone.mx.datacontrols.datatree;

import java.io.Serializable;

/**
 * @author thinking 2012-9-1
 */
public class ResourceZtreeNode extends ZtreeNode implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    private String busicode;
    
    private String typeName;
    
    private String type;


    /**
     * get busicode
     * @return busicode
     */
    public String getBusicode() {
        return busicode;
    }

    /** 
     * set busicode
     * @param busicode the busicode to set
     */
    public void setBusicode(String busicode) {
        this.busicode = busicode;
    }

    /**
     * get typeName
     * @return typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /** 
     * set typeName
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * get type
     * @return type
     */
    public String getType() {
        return type;
    }

    /** 
     * set type
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
}
