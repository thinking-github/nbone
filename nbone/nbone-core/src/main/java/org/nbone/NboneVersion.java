package org.nbone;

public class NboneVersion {
	
	public final static String version  = "1.0.0.GA";
	
	public static String getVersion() {
		Package pkg = NboneVersion.class.getPackage();
		return (pkg != null ? pkg.getImplementationVersion() : version);
	}
	
	
	public static String getVersion(String defversion) {
		String  version  = getVersion();
        return version != null ? version : defversion;  		
		
	}
	
	
	public static void main(String[] args) {
		System.out.println(getVersion());
	}

}
