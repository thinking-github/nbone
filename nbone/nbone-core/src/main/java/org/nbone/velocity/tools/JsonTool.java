package org.nbone.velocity.tools;

import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;
import org.apache.velocity.tools.generic.SafeConfig;
import org.nbone.util.json.jackson.JsonUtils;
import org.apache.velocity.tools.Scope;
/**
 * 
 * @author thinking
 * @since 2016-08-08
 * @version 1.0.1
 */
@DefaultKey("Json")
@ValidScope(Scope.APPLICATION)
public class JsonTool extends SafeConfig {

	public String stringify(Object value){
		String string = "";
		if(value == null){
			return string;
		}
		
		try {
			string = JsonUtils.pojoToJson(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
		
	}
	
	
}
