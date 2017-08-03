package org.nbone.notice.sms;

import java.util.ArrayList;
import java.util.List;

import org.nbone.message.sms.service.impl.SmsUseCallback;
import org.nbone.message.sms.vo.SmsVo;

public class SmsTest {
	
	public static SmsVo createSmsVo() {
		List<String> to = new ArrayList<String>();
		to.add("18655166563");
		SmsVo  smsVo = new SmsVo(); 
		smsVo.setUserNumberList(to);
		smsVo.setSmsContent("Sms Test 99999999999");
		return smsVo;
	}
	
	
	public static void main(String[] args) {
		SmsUseCallback useCallback =  new SmsUseCallback();
		
		System.out.println(useCallback.preHandle(createSmsVo()));
		List<String> to = new ArrayList<String>();
		to.add("18655166563");
		//SmsUtils.send("Sms Test 9999",to);

	}

}
