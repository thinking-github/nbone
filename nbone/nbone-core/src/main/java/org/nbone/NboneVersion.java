package org.nbone;
/**
 * 
 * @author thinking
 * @version 1.0
 * @since 2014-02-14
 *
 */
public class NboneVersion {
	/**
	 * 2016-09-02
	 */
	public final static String version  = "1.0.2.GA";
	/**
	 * 2016-07-02
	 */
	public final static String version101  = "1.0.1.GA";
	/**
	 * 2014-02-14
	 */
	public final static String version100  = "1.0.0.GA";
	
	
	
	public static String getVersion() {
		Package pkg = NboneVersion.class.getPackage();
		return (pkg != null ? pkg.getImplementationVersion() : version);
	}
	
	
	public static String getVersion(String defversion) {
		String  version  = getVersion();
        return version != null ? version : defversion;  		
		
	}
	
	
	public static void main(String[] args) {
		System.out.println(getVersion(version));
	}

}
