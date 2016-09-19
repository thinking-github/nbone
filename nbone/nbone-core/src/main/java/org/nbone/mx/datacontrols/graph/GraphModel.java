package org.nbone.mx.datacontrols.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nbone.core.exception.CircleReferenceException;
import org.nbone.mx.datacontrols.Node;
import org.nbone.mx.datacontrols.graph.exception.GraphCircleReferenceException;

/**
 * 图的数据模型
 * @author thinking
 *
 */
public class GraphModel extends Graph<GraphEdgeModel> {

	private static final long serialVersionUID = -8929053959689145682L;
	
	/**
	 * 标识映射节点信息
	 */
	private Map<String,Node> mapNode;
	


	public GraphModel(Map<String, Node> mapNode) {
		this(mapNode,null);
	}

	public GraphModel(Map<String, Node> mapNode, List<GraphEdgeModel> relationEdges) {
		this.mapNode = mapNode;
		super.setNodes(mapNode.values());
		
		super.setRelationEdges(relationEdges);
	}

	public Map<String, Node> getMapNode() {
		return mapNode;
	}

	public void setMapNode(Map<String, Node> mapNode) {
		this.mapNode = mapNode;
	}
	
	
	/**
	 * 判断图是否含有循环依赖
	 * @param rootNode 第一个顶点
	 * @param edges    关系集合
	 * @param nextNode 第一次调用可为空
	 * @return
	 * @throws CircleReferenceException
	 */
	public static Node checkCircle(Node rootNode,List<GraphEdgeModel> edges,Node nextNode) throws GraphCircleReferenceException {
		if(rootNode == null || rootNode.getId() == null){
			return null;
		}
		Node result = null; 
		String rootId = rootNode.getId();
		String id = null;
		if(nextNode == null){
			id = rootNode.getId();
		}else{
			id = nextNode.getId();
		}
		List<Node> nextNodes = new  ArrayList<Node>();
		for (GraphEdgeModel graphEdgeModel : edges) {
			if(id.equals(graphEdgeModel.getFrom().getId())){
				Node to = graphEdgeModel.getTo();
				nextNodes.add(to);
			}
		}
		
		for (Node node : nextNodes) {
			//XXX：计算循环依赖
			if(rootId.equals(node.getId())){
				throw new GraphCircleReferenceException("Graph 存在循环引用.",new GraphEdgeModel(nextNode, rootNode));
			}
			result = node;
			System.out.println(id+"---------------->"+ node.getId());
			Node next = checkCircle(rootNode, edges,node);
			
			//XXX：一条边遍历结束
			if(next == null){
				continue ;
			}
		}
		
		return result;
		
	}
	
	public static void main(String[] args) {
		Node rootNode =  new Node("0","root");
		
		Node node1 =  new Node("1","node1");
		Node node2 =  new Node("2","node2");
		Node node3 =  new Node("3","node3");
		Node node4 =  new Node("4","node3");
		
		GraphEdgeModel edge1 = new GraphEdgeModel(rootNode, node1);
		GraphEdgeModel edge2 = new GraphEdgeModel(rootNode, node2);
		GraphEdgeModel edge3 = new GraphEdgeModel(node1, node3);
		GraphEdgeModel edge4 = new GraphEdgeModel(node3, node4);
		
		GraphEdgeModel edge5 = new GraphEdgeModel(node2, node3);
		GraphEdgeModel edge6 = new GraphEdgeModel(node4, rootNode);
		
		List<GraphEdgeModel> list =  new ArrayList<>();
		list.add(edge1);
		list.add(edge2);
		list.add(edge3);
		list.add(edge4);
		list.add(edge5);
		list.add(edge6);
		try {
			checkCircle(rootNode, list, null);
		} catch (GraphCircleReferenceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getErrObject().getFrom().getId()+"-------err-------->"+e.getErrObject().getTo().getId());
		}
		
		System.out.println("---------------");
	}

}
