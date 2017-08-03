package org.nbone.mx.datacontrols.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nbone.core.exception.CircleReferenceException;
import org.nbone.mx.datacontrols.Node;
import org.nbone.mx.datacontrols.graph.exception.GraphCircleReferenceException;
import org.nbone.util.graph.CycleDetector;
import org.nbone.util.json.jackson.JsonUtils;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;


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
	
	public GraphModel() {
	}

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
	
	
	
	@Override
	public boolean checkCircle() {
		return super.checkCircle();
	}
	
	/**
	 * 检测环，如果含有环并更新Node节点环的状态
	 * @return
	 */
	public static  boolean checkCircleAndUpdate(Graph<GraphEdgeModel> graph) {
		boolean check = false ;
		List<String> cnodes = checkCircle(graph);
		Set<Node> allNode = graph.getNodes();
		if(cnodes != null && cnodes.size() > 0 ){
			check = true;
			for (String nodeId : cnodes) {
				for (Node node : allNode) {
					if(nodeId.equals(node.getId())){
						node.setFlag(true);
						break;
					}
				}
			}
		}
		
		return check;
	}
	
	/**
	 * 检测环并返回含有环的列表
	 * @param graph
	 * @return
	 */
	public static List<String> checkCircle(Graph<GraphEdgeModel> graph){
		IdDirectedGraph directedGraph = new IdDirectedGraph(graph);
		
		CycleDetector<String> circle = new CycleDetector<String>(directedGraph) ;
		circle.containsCycle();
		//debug print 
		//directedGraph.print();
		
		return circle.getVerticesInCycles();
	}

	/**
	 * 判断图是否含有循环依赖
	 * @param rootNode 第一个顶点
	 * @param edges    关系集合
	 * @param nextNode 第一次调用可为空
	 * @return
	 * @throws CircleReferenceException
	 * FIXME： 方法功能不完善,存在Bug 下一节点可能存在环的问题
	 */
	public static Node checkCircle(Node rootNode,List<GraphEdgeModel> edges,Node nextNode) throws GraphCircleReferenceException {
		if(rootNode == null || rootNode.getId() == null){
			return null;
		}
		Node result = null; 
		String rootId = rootNode.getId();
		Node  useNode = null;
		if(nextNode == null){
			useNode = rootNode;
		}else{
			useNode = nextNode;
		}
		List<Node> nextNodes = new  ArrayList<Node>();
		for (GraphEdgeModel graphEdgeModel : edges) {
			if(useNode.getId().equals(graphEdgeModel.getFrom().getId())){
				Node to = graphEdgeModel.getTo();
				nextNodes.add(to);
			}
		}
		
		for (Node node : nextNodes) {
			//XXX：计算开始节点循环依赖
			if(rootId.equals(node.getId())){
				throw new GraphCircleReferenceException("Graph 存在循环引用.",new GraphEdgeModel(nextNode, rootNode));
			}
			//Node childNode = checkCircle(node, edges,null);
			
			
			result = node;
			System.out.println(useNode.getId()+":"+ useNode.getName() +"---------------->"+ node.getId()+":"+node.getName());
			Node next = checkCircle(rootNode, edges,node);
			
			//XXX：一条边遍历结束
			if(next == null){
				continue ;
			}
		}
		
		return result;
		
	}
	
	public static void main(String[] args) throws Exception {
	
		
		
		Node rootNode =  new Node("0","root");
		
		Node node1 =  new Node("1","node1");
		Node node2 =  new Node("2","node2");
		Node node3 =  new Node("3","node3");
		Node node4 =  new Node("4","node4");
		
		GraphEdgeModel edge1 = new GraphEdgeModel(rootNode, node1);
		GraphEdgeModel edge2 = new GraphEdgeModel(rootNode, node2);
		GraphEdgeModel edge3 = new GraphEdgeModel(node1, node3);
		GraphEdgeModel edge4 = new GraphEdgeModel(node3, node4);
		
		GraphEdgeModel edge5 = new GraphEdgeModel(node2, node3);
		
		GraphEdgeModel edge6 = new GraphEdgeModel(node4, node3);
		
		List<GraphEdgeModel> list =  new ArrayList<>();
		list.add(edge1);
		list.add(edge2);
		list.add(edge3);
		list.add(edge4);
		list.add(edge5);
		list.add(edge6);
		
		MutableGraph<Node> graph1 = GraphBuilder.directed().build();
		graph1.addNode(rootNode);
		graph1.addNode(node1);
		graph1.addNode(node2);
		graph1.addNode(node3);
		graph1.addNode(node4);
		graph1.putEdge(rootNode, node1);
		graph1.putEdge(rootNode, node2);
		System.out.println(JsonUtils.pojoToJson(graph1));
		
		for (int i = 0; i < 5; i++) {
			
			try {
				//checkCircle(rootNode, list, null);
				
				GraphModel graph = new GraphModel();
				graph.addNode(rootNode);
				graph.addNode(node1);
				graph.addNode(node2);
				graph.addNode(node3);
				graph.addNode(node4);
				
				graph.setRelationEdges(list);
				
				
				
				List<String> cycNodes = GraphModel.checkCircle(graph);
				System.out.println(cycNodes);
				
			} catch (GraphCircleReferenceException e) {
				System.out.println(e.getMessage());
				System.out.println(e.getErrObject().getFrom().getId()+"-------err-------->"+e.getErrObject().getTo().getId());
			}
			
			System.out.println("---------------");
		}
	}

}
