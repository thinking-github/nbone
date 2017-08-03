package org.nbone.mx.datacontrols.graph;

import org.nbone.mx.datacontrols.Node;
import org.nbone.util.graph.CycleDetector;
import org.nbone.util.graph.DirectedGraph;

public class CircleDetector extends CycleDetector<Node> {

	
	public CircleDetector(DirectedGraph<Node> graph) {
		super(graph);
	}
	
	

}
