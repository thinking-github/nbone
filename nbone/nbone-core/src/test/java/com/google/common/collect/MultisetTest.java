package com.google.common.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 　Multiset是什么？顾名思义，Multiset和Set的区别就是可以保存多个相同的对象。
 * 在JDK中，List和Set有一个基本的区别，就是List可以包含多个相同对象，且是有顺序的，
 * 而Set不能有重复，且不保证顺序（有些实现有顺序，例如LinkedHashSet和SortedSet等）
 * 所以Multiset占据了List和Set之间的一个灰色地带：允许重复，但是不保证顺序。 
 * 常见使用场景：Multiset有一个有用的功能，就是跟踪每种对象的数量，所以你可以用来进行数字统计。 常见的普通实现方式如下
 * 
 */
public class MultisetTest {

	public static void main(String[] args) {
//		/testWordCount();
		testMultsetWordCount();
	}
	
	/**
	 * 原始实现
	 */
	 public static  void testWordCount(){
	        String strWorld="wer|dffd|ddsa|dfd|dreg|de|dr|ce|ghrt|cf|gt|ser|tg|ghrt|cf|gt|" +
	                "ser|tg|gt|kldf|dfg|vcd|fg|gt|ls|lser|dfr|wer|dffd|ddsa|dfd|dreg|de|dr|" +
	                "ce|ghrt|cf|gt|ser|tg|gt|kldf|dfg|vcd|fg|gt|ls|lser|dfr";
	        String[] words=strWorld.split("\\|");
	        Map<String, Integer> countMap = new HashMap<String, Integer>();
	        for (String word : words) {
	            Integer count = countMap.get(word);
	            if (count == null) { 
	                countMap.put(word, 1); 
	            }
	            else { 
	                countMap.put(word, count + 1); 
	            }
	        }        
	        System.out.println("countMap：");
	        for(String key:countMap.keySet()){
	            System.out.println(key+" count："+countMap.get(key));
	        }
	    }
	 
	 public static void testMultsetWordCount(){
	        String strWorld="wer|dfd|dd|dfd|dda|de|dr";
	        String[] words=strWorld.split("\\|");
	        List<String> wordList=new ArrayList<String>();
	        for (String word : words) {
	            wordList.add(word);
	        }
	        
	        
	        
	        
	        Multiset<String> wordsMultiset = HashMultiset.create();
	        wordsMultiset.addAll(wordList);
	        
	        
	        System.out.println(wordsMultiset.size());
	        System.out.println(wordsMultiset.elementSet().size());
	        System.out.println(wordsMultiset.entrySet().size());
	     
	        for(String key:wordsMultiset.elementSet()){
	            System.out.println(key+" count："+wordsMultiset.count(key));
	        }
	    }

}
