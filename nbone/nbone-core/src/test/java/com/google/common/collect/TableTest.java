package com.google.common.collect;
/**
 * 表结构类型
 * @author thinking
 * @version 1.0 
 */
public class TableTest {

	public static void main(String[] args) {
		
		testHashBasedTable();
		
		

	}
	
	public static void testHashBasedTable(){
		
		Table<String,Integer, Double> weightedGraph = HashBasedTable.create();
		weightedGraph.put("r1",1, 4D);
		weightedGraph.put("r1",2, 5D);
		weightedGraph.put("r2",1, 20D);
		weightedGraph.put("r3",1, 5D);

		weightedGraph.row("r1"); // returns a Map mapping v2 to 4, v3 to 20
		weightedGraph.column(1); // returns a Map mapping v1 to 20, v2 to 5
		
		
		System.out.println(weightedGraph.row("r1"));
		System.out.println(weightedGraph.row("r1").get(1));
		System.out.println(weightedGraph.get("r1", 1));
		System.out.println(weightedGraph.column(1));
		
		
		System.out.println(weightedGraph.rowMap());
		System.out.println(weightedGraph.columnMap());
		
	}
	
	public static void testArrayTable(){
		
		Table<String,Integer, Double> weightedGraph = HashBasedTable.create();
		
		
	}
	
	
	
	

}
