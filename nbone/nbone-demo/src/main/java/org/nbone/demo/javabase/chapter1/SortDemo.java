package org.nbone.demo.javabase.chapter1;
/**
 * 
 * @author thinking
 *
 */
public class SortDemo {

	public static void main(String[] args) {
		
		SortDemo sort  = new SortDemo();
		int[] arr  = {4,3,2,5,1,0};
		sort.sort1(arr);
       // print(arr);
	}
	
	/**
	 * 选择排序算法
	 * @param arr
	 */
	public void sort(int[] arr){
		for (int i = 0; i < arr.length-1; i++) {
			for (int j = i+1; j < arr.length; j++) {
				if(arr[i] > arr[j]){
					
					int temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp ;
				}
				
			}
			print(arr);
			System.out.println();
		}
	}
	
	/**
	 * 冒泡排序
	 * @param arr
	 */
	public void sort1(int[] arr){
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length-1; j++) {
				if(arr[j] > arr[j+1]){
					
					int temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp ;
				}
				
			}
			print(arr);
			System.out.println();
		}
	}
	
	public static void print(int[] arr){
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + ",");
		}
	}

}
