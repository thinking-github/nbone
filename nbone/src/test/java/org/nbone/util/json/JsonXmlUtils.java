package org.nbone.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nbone.test.UserTest;

public class JsonXmlUtils {
	// test main
	public static void main(String[] args) {
		
		List list = new ArrayList();
		Map map1 = new HashMap();
		map1.put("id", "77777");
		map1.put("userName", "chenyc");
		list.add(map1);
		list.add(map1);
        //数组
		String xmls = JSONXOperUtils.pojo2XML(list, new JSONXConfig("datas", "node", "node"));
		System.out.println("xmls---:" + xmls);
		//单个对象
		String xml = JSONXOperUtils.pojo2XML(map1, new JSONXConfig("datas", "node1", "object1"));
		System.out.println("xml---:" + xml);
        
		String jsonString = JSONXOperUtils.xml2JSON(xmls, true);
		System.out.println("jsonString==" + jsonString);

		
		Object[] obj = JSONXOperUtils.xml2ArrayForMap(xmls);
		Map  map = JSONXOperUtils.xml2Map(xml);
		Object[] obj1 = JSONXOperUtils.xml2ArrayForObject(xmls, UserTest.class);
		
		System.out.println("obj=="+obj[0]);
		System.out.println("map=="+map);

	}

}
