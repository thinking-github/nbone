package org.nbone.mx.datacontrols.graph;

import java.util.Collection;

import org.nbone.mx.datacontrols.Node;
import org.nbone.util.graph.DirectedGraph;

/**
 * 使用节点标识（id）存储图数据结构
 * @author thinking
 *
 */
public class IdDirectedGraph extends DirectedGraph<String>  {
	
	public IdDirectedGraph() {
	}
	
	public IdDirectedGraph(Graph<GraphEdgeModel> graph) {
		this.setGraph(graph);
	}

	public void  addNodes(Collection<Node> nodes) {
		if(nodes == null){
			return ;
		}
		for (Node node : nodes) {
			super.addNode(node.getId());
		}
		
	}

	public void addEdges(Collection<GraphEdgeModel> edges) {
		if(edges == null){
			return ;
		}
		for (GraphEdgeModel graphEdgeModel : edges) {
			super.addEdge(graphEdgeModel.getFrom().getId(),graphEdgeModel.getTo().getId());
		}
	}
    
	/**
	 * 
	 * @param graph
	 * @see #addNodes(Collection)
	 * @see #addEdges(Collection)
	 */
	public  void setGraph(Graph<GraphEdgeModel> graph) {
		addNodes(graph.getNodes());
		addEdges(graph.getRelationEdges());
	}
	
	public void print(){
		for (String node : this.nodes()) {
			System.out.println(node+"-------->"+this.edgesFrom(node));
		}
		
	}
	

}
