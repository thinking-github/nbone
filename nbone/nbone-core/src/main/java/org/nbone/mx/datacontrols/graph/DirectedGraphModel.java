package org.nbone.mx.datacontrols.graph;

import java.util.Collection;

import org.nbone.mx.datacontrols.Node;
import org.nbone.util.graph.DirectedGraph;

/**
 * 分布式条件下 使用对象标识存在无法对应关系问题
 * @author thinking
 *
 */
public class DirectedGraphModel extends DirectedGraph<Node> {

	public DirectedGraphModel() {
	}
	
	public DirectedGraphModel(Graph<GraphEdgeModel> graph) {
		this.setGraph(graph);
	}

	public void  addNodes(Collection<Node> nodes) {
		if(nodes == null){
			return ;
		}
		for (Node node : nodes) {
			super.addNode(node);
		}
		
	}

	public void addEdges(Collection<GraphEdgeModel> edges) {
		if(edges == null){
			return ;
		}
		for (GraphEdgeModel graphEdgeModel : edges) {
			super.addEdge(graphEdgeModel.getFrom(),graphEdgeModel.getTo());
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
		for (Node node : this.nodes()) {
			System.out.println(node+"-------->"+this.edgesFrom(node));
		}
		
	}
	
	

}
