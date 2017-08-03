package org.nbone.mx.datacontrols.graph;

import java.io.Serializable;

import org.nbone.mx.datacontrols.Node;

/**
 * 用于数据结构图 关系边 的存储模型
 * @author thinking
 *
 */
public class GraphEdgeModel implements GraphEdge<Node>,Serializable {

	private static final long serialVersionUID = 592236191455420570L;
	
	private String id;
	private String name;
	
	private Node from;
	private Node to;
	
	private String color;
	private int lineWidth;
	
	
	public GraphEdgeModel() {
	}

	public GraphEdgeModel(Node from, Node to) {
		this.from = from;
		this.to = to;
	}
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Node getFrom() {
		return from;
	}
	public void setFrom(Node from) {
		this.from = from;
	}
	public Node getTo() {
		return to;
	}
	public void setTo(Node to) {
		this.to = to;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(from).append("------->").append(to).toString();
	}
	
	

}
