package org.nbone.persistence.criterion;

public class QueryCustomAbstract implements IQueryCustom{
	private String custom;

    public void appendCustom(String custom)
    {
        if(this.custom == null)
            this.custom = "";
        StringBuffer sb = new StringBuffer(this.custom);
        sb.append(custom);
        this.custom = sb.toString();
    }

    public IQueryCustom setCustom(String custom)
    {
        this.custom = custom;
        return this;
    }

    public String getCustom()
    {
        return custom;
    }

   

}
