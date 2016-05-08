package com.google.common;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class RangeTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		Range range = Range.open(0, 5);
		System.out.println(range);
		System.out.println(range.contains(2));
		
		ContiguousSet dd =  ContiguousSet.create(range, DiscreteDomain.integers());
		
		System.out.println(dd);
		
		
		
		System.out.println(Range.openClosed(0, 5));
		System.out.println(Range.closedOpen(0, 5));
		System.out.println(Range.closed(0, 5));
		
		

	}

}
