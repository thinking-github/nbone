package org.nbone.mx.datacontrols.graph;

/**
 * 数据结构边抽象接口
 * @author thinking
 *
 * @param <T>
 */
public interface GraphEdge<T> {
	
	
	public T getFrom();
	
	public T getTo();

}
