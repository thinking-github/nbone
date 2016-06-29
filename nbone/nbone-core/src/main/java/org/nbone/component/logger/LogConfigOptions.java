package org.nbone.component.logger;


/**
 * 
 * @author Thinking 2014-8-8
 * @see LoggerStatic
 * 
 */
public class LogConfigOptions implements LoggerStatic {

	/**
	 * 获取key 和 option 的组合<br>
	 * 例如org.nbone.meetingcontrol.bizc.MeetingControlBizc.queryMeeting和.DESC
	 * 
	 * @param key
	 * @param option
	 * @return
	 */
	public static String getOptions(String key, String option) {

		String key1 = key + option;

		return key1;

	}
	public static String getOptionId(String key, String option) {


		return null;

	}
	

}
