package org.nbone.component.logger;


/**
 * 操作类型枚举类
 * <p/>
 * @author Thinking  2014-8-8
 * @see LogOperateType
 */
public enum OperateType {

	QUERY("0", "查询"),

	CREATE("1", "创建"),
	
	DELETE("2", "删除"),
	
	UPDATE("3", "修改")
	;

	/**
	 * 代码
	 */
	private final String code;
	/**
	 * 信息
	 */
	private final String message;

	/**
	 * @param code
	 * @param message
	 */
	OperateType(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 根据code获得该code对应的返回类型
	 * @param code
	 * @return
	 */
	public static OperateType getOperateTypeByCode(String code) {
		if (code == null || code.trim().length() == 0) {
			return null;
		}
		for (OperateType type : OperateType.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
