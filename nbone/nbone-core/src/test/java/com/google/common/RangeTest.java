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
		
		System.out.println(Range.openClosed(0, 5));
		System.out.println(Range.closedOpen(0, 5));
		System.out.println(Range.closed(0, 5));
		
		System.out.println(Range.greaterThan(3));
		System.out.println(Range.lessThan(1));
		System.out.println(Range.atLeast(3));
		System.out.println(Range.atMost(1));
		System.out.println(Range.all());
		
		
		
		
		ContiguousSet dd =  ContiguousSet.create(range, DiscreteDomain.integers());
		
		System.out.println(dd);
		
		
		

		
		

	}

}
