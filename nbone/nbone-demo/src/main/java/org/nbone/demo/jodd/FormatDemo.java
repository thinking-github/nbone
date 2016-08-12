package org.nbone.demo.jodd;

import jodd.format.Printf;

public class FormatDemo {
	
	public static void main(String[] args) {
		Printf.str("%+i", 173);     // +173
	    Printf.str("%04d", 1);      // 0001
	    Printf.str("%f", 1.7);      // 1.700000
	    Printf.str("%1.1f", 1.7);   // 1.7
	    Printf.str("%.4e", 100.1e10);   // 1.0010e+012
	    Printf.str("%G", 1.1e13);   // 1.1E+013
	    Printf.str("%l", true);     // true
	    Printf.str("%L", 123);      // TRUE
	    Printf.str("%b", 13);       // 1101
	    Printf.str("%,b", -13);     // 11111111 11111111 11111111 11110011
	    Printf.str("%#X", 173);     // 0XAD
	    Printf.str("%,x", -1);      // ffff ffff
	    //Printf.str("%s %s", new String[]{"one", "two"});    // one two
	}

}
