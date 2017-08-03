package org.nbone.mx.datacontrols;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author thinking
 * @since 2016-09-07
 * @version 1.0.2
 */
public class NodeGroup {
	
	private String id;
	private String name;
	
	private Set<Node> gnodes;


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
	
	public Set<Node> getGnodes() {
		return gnodes;
	}
	public void setGnodes(Set<Node> gnodes) {
		this.gnodes = gnodes;
	}
	
	
	/**
	 * 集合转化成节点集合
	 * @param nodeGroup
	 */
	public void setNodeGroup(Collection<Node> nodeGroup) {
		if(nodeGroup instanceof Set){
			this.gnodes = (Set<Node>) nodeGroup;
		}else{
			this.gnodes = new HashSet<Node>(nodeGroup);
		}
	}
	
	public void setNodeGroup(Node[] nodes) {
		this.gnodes = new HashSet<Node>();
		for (Node node : nodes) {
			gnodes.add(node);
		}
	}
}
