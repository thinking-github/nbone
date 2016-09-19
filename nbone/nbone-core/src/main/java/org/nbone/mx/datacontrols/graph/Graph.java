package org.nbone.mx.datacontrols.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nbone.mx.datacontrols.Node;


/**
 *    <pre>     
          var graphModel = {
				nodes:[  {id:1,name:"mall"},
				         {id:2,name:"market-sdk"},
				         {id:3,name:"order-sdk"},
				         {id:10,name:"vm-sdk"},
				         {id:11,name:"vm1-sdk"}
				       ],
				         
				relationEdges:[ {from:1,to:10},
				        		{from:1,to:3},
				        		{from:2,to:10},
				        		{from:3,to:11},
				        		{from:10,to:11}
				        ]
	
	};
 *    
 *    </pre>
 *    
 *    
  
 * 图的数据结构 
 * @author thinking
 *
 * @param <R>
 */
public abstract class Graph<R> implements Serializable {
	
	private static final long serialVersionUID = 7939302796946853658L;
    /**
     * 图的节点集合
     */
	private Set<Node> nodes;
	/**
	 * 图的边集合
	 */
	private List<R> relationEdges;
	

	public Graph() {
	}

	public Graph(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Graph(Set<Node> nodes, List<R> relationEdges) {
		this.nodes = nodes;
		this.relationEdges = relationEdges;
	}



	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}
	
	
	public List<R> getRelationEdges() {
		return relationEdges;
	}

	public void setRelationEdges(List<R> relationEdges) {
		this.relationEdges = relationEdges;
	}
	
	public Graph<R> addNode(Node node){
		if(nodes == null){
			nodes = new HashSet<Node>();
		}
		nodes.add(node);
		return this;
	}
	public Graph<R> addRelationEdge(R relationEdge) {
		if(relationEdges == null){
			relationEdges = new ArrayList<R>();		
		};
		
		relationEdges.add(relationEdge);
		return this;
	}
	
    /**
     * 将List 转化成Set
     * @param nodes
     */
	public void setNodes(Collection<Node> nodes) {
		this.nodes = new HashSet<Node>(nodes);
	}
	
	/**
	 * 将Map values 转化成 Set
	 * @param nodesMap
	 */
	public void setNodes(Map<?,Node> nodesMap) {
		this.nodes = new HashSet<Node>(nodesMap.values());
	}


}
