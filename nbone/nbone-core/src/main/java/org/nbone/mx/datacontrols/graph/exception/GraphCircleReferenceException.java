package org.nbone.mx.datacontrols.graph.exception;

import org.nbone.mx.datacontrols.graph.GraphEdgeModel;

/**
 * 循环引用异常
 * @author thinking
 * @since 1.0.2 
 *
 */
public class GraphCircleReferenceException extends RuntimeException {

	private static final long serialVersionUID = -857061989355920812L;
	
	private GraphEdgeModel errObject;

	public GraphCircleReferenceException() {
		super();
	}

	public GraphCircleReferenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public GraphCircleReferenceException(String message) {
		super(message);
	}

	public GraphCircleReferenceException(Throwable cause) {
		super(cause);
	}
	
	
	
	public GraphCircleReferenceException(GraphEdgeModel errObject) {
		super();
	}
	public GraphCircleReferenceException(String message,GraphEdgeModel errObject) {
		super(message);
		this.errObject = errObject;
	}

	
	public GraphEdgeModel getErrObject() {
		return errObject;
	}

	public void setErrObject(GraphEdgeModel errObject) {
		this.errObject = errObject;
	}
	

}
