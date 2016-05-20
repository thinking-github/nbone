package com.google.common.collect;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Sets.SetView;

public class SetsTest {

	public static void main(String[] args) {
		
		guava();
		apacheCommon();

	}
	
	public static void guava() {
		
		HashSet<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5);
		HashSet<Integer> setB = Sets.newHashSet(4, 5, 6, 7, 8);
		 /**
		  * 合集
		  */
		SetView<Integer> union = Sets.union(setA, setB);
		System.out.println("union:" + union);
		 
		 /**
		  * 取第二个参数的补集
		  */
		SetView<Integer> difference = Sets.difference(setA, setB);
		System.out.println("difference:" + difference);
		
		/**
		 * 取第二个参数的补集
		 */
		SetView<Integer> difference1 = Sets.difference(setB, setA);
		System.out.println("difference1:" + difference1);
		 
		/**
		 * 交集
		 */
		SetView<Integer> intersection = Sets.intersection(setA, setB);
		System.out.println("intersection:" +intersection);
		
		/**
		 * 交集的补集
		 */
		SetView<Integer> differences = Sets.symmetricDifference(setA, setB);
		System.out.println("differences:" +differences);
		
		
	}
	
	public static void apacheCommon() {
		System.out.println("---------------------------------------------");
		HashSet<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5);
		HashSet<Integer> setB = Sets.newHashSet(4, 5, 6, 7, 8);
		System.out.println("union:" + CollectionUtils.union(setA, setB));
		
		/**
		 * 交集
		 */
		System.out.println("intersection:" + CollectionUtils.intersection(setA, setB));
		/**
		 * 交集的补集
		 */
		Collection<Integer> difference12 = CollectionUtils.disjunction(setA, setB);
		System.out.println("difference12:" + difference12);
		
		/**
		 * 第二个参数的补集
		 */
		System.out.println("subtract:" + CollectionUtils.subtract(setA, setB));
		System.out.println("subtract:" + CollectionUtils.subtract(setB, setA));
	}

}
