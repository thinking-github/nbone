package org.nbone.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 集成EasyUI框架
 * @author Thinking  2014-9-23
 *
 */
public class EasyuiUtils {
	
	private static Log logger = LogFactory.getLog(EasyuiUtils.class);
	
	public final static  String PAGE ="page";
	public final static  String ROWS ="rows";

	
    /**
     * 分页时，获取显示的页码,默认为第一页
     * @param request
     * @return
     */
	public static int getOffset(HttpServletRequest request) {
		int offset = 1;
		try {
			offset = Integer.parseInt(request.getParameter(PAGE));
		} catch (NumberFormatException ignore) {
			logger.warn("current pageNum must is number.", ignore);
		}
		return offset;
	}
    /**
     * 分页时，获取显示的页的大小,默认为20行
     * @param request
     * @return
     */
	public static int getPageSize(HttpServletRequest request) {
		int PageSize=20;
		try {
			PageSize = Integer.parseInt(request.getParameter(ROWS));
		} catch (NumberFormatException ignore) {
			logger.warn("current pageSize must is number.", ignore);
		}
		return PageSize;
	}
	
	

}
