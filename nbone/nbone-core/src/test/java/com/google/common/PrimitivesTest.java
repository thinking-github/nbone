package com.google.common;

import java.util.List;

import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInts;


public class PrimitivesTest {

	public static void main(String[] args) {
		
		
		List<Integer> ints = Ints.asList(1);
		
		System.out.println(ints);
		
		
		int ss = Ints.stringConverter().convert("-7");
		
		ss= Ints.tryParse("8");
		int [] int1 ={1,2,3};
		int [] int2 ={5,6,7};
		int[] concat = Ints.concat(int1,int2);
		
		
		boolean boo = Ints.contains(int1, 1);
		
		String test = Ints.join("-", int1);
		
		System.out.println(test);
		
		int kk = UnsignedInts.parseUnsignedInt("89");
		System.out.println(kk);
		
		
		
		
		
		

	}

}
