package org.nbone.util.escape;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;

import com.google.common.html.HtmlEscapers;
import com.google.common.xml.XmlEscapers;

/**
 * @author thinking
 * @version 1.0 
 * @since 2013-12-12
 * @see  com.google.common.html.HtmlEscapers
 * @see  com.google.common.xml.XmlEscapers
 * @see  org.springframework.web.util.HtmlUtils
 * @see  org.springframework.web.util.JavaScriptUtils
 * @see  org.apache.commons.lang3.StringEscapeUtils
 * @see  org.apache.commons.lang3.StringEscapeUtils
 *
 */
public class UnsafeStringEscape {
	

	
	/**
	 * 大颗粒标签过滤(含<>)
	 * @param value
	 * @return 返回转义后的字符
	 */
	public String largeGrainedTagFilter(String value) {
		 //标准脚本和HTML<script>alert(999)</script>
		 //简化脚本和HTML<script type="" src=""/>
		 //</script>
		Pattern scriptPattern = Pattern.compile("(<(.*?)>(.*)</(.*?)>|<(.*?)/>|</(.*?)>)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    //value = scriptPattern.matcher(value).replaceAll("");
		boolean boo = scriptPattern.matcher(value).find();
        if(boo){
        	value =	HtmlUtils.htmlEscape(value);
        }
        System.out.println("1:"+value);
        
        //不规范的脚本<script>
        scriptPattern = Pattern.compile("<(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        boo = scriptPattern.matcher(value).find();
        if(boo){
        	value =	HtmlUtils.htmlEscape(value);
        }
        System.out.println(value);
        
        return value;
		
	}
	
	/**
	 * 小颗粒标签过滤(含script  iframe img)<br>
	 * 此方法功能不全,请参见{@link #largeGrainedTagFilter(String)}
	 * @param value
	 * @return
	 */
	public String smallGrainedTagFilter(String value) {
		
		 //<script>alert(11)</script>
	     Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
         value = scriptPattern.matcher(value).replaceAll("");
	    
         //<script%00>
	     //<%00/script>
	     //</script>
         scriptPattern = Pattern.compile("<(.*?)script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
         value = scriptPattern.matcher(value).replaceAll("");
        
         //scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        
         
         
         //<iframe src=""></iframe>
	     scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
         value = scriptPattern.matcher(value).replaceAll("");
	    
         //<iframe>
         scriptPattern = Pattern.compile("<(.*?)iframe(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
         value = scriptPattern.matcher(value).replaceAll("");
        
        
        
        //<img src=""></img>
 	     scriptPattern = Pattern.compile("<img>(.*?)</img>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
         value = scriptPattern.matcher(value).replaceAll("");
 	    
         //<img>
         scriptPattern = Pattern.compile("<(.*?)img(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
         value = scriptPattern.matcher(value).replaceAll("");
		
		return value;
	}
	
	/**
	 * 关键字过滤<br>
	 * 最后执行,防止破坏其他标签结构
	 * @param value
	 * @return
	 */
	public String keywordFilter(String value) {
             
		    /*scriptPattern = Pattern.compile("src(\\s*)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	        value = scriptPattern.matcher(value).replaceAll("");*/
        
		    Pattern scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        
            
		    scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	        value = scriptPattern.matcher(value).replaceAll("");

	        scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	        value = scriptPattern.matcher(value).replaceAll("");
	        
	        scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	        value = scriptPattern.matcher(value).replaceAll("");
	        
	        scriptPattern = Pattern.compile("alert\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	        value = scriptPattern.matcher(value).replaceAll("");
	        

	        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	        value = scriptPattern.matcher(value).replaceAll("");

	        scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
	        value = scriptPattern.matcher(value).replaceAll("");

	        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	        value = scriptPattern.matcher(value).replaceAll("");
	        
	        //window.location=
	        //window["location"]=
	        //window['location']=
	        scriptPattern = Pattern.compile("(\\[*?)(\"*?)('*?)location(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	        value = scriptPattern.matcher(value).replaceAll("");
	        
	        //try{}catch(e){}
            scriptPattern = Pattern.compile("try(\\s*)\\{(.*?)}(.*?)catch(.*?)\\{(.*?)}", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
		
		
		return value;
	}
	
	public String escapeSqlFilter(String value) {
		
		   Pattern scriptPattern = Pattern.compile("[';+]*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	       value = scriptPattern.matcher(value).replaceAll("");
	       
	       value = value.replaceAll("--","");
		return value;
	}
	
	//---------------------------------------------------------
	public static void main(String[] args) {
	    String value = "<script/></script><script%00>kkkkkkkkkkkkk</script><img src='9999' id=\"chen\" -- javascript:alert'\\\\'";
	    
	    String value1 = "111111</script><script>9999<script%00>kkkkkkkkkkkkk</script></script><script>88888";
	    
	    String value2 = "alert('0000')";
	    String value3 = ";77777+'--";
	   // new UnsafeStringEscape().largeGrainedTagFilter(value1);
	   // System.out.println("\'");
	    String kk = StringEscapeUtils.escapeHtml(value);
	    //System.out.println("==="+kk);
	    //System.out.println(StringEscapeUtils.escapeJavaScript(value));
	    //System.out.println(StringEscapeUtils.escapeSql(value));
	    System.out.println(new UnsafeStringEscape().escapeSqlFilter(value3));
	    
	    
	    System.out.println("-------------largeGrainedTagFilter----------------------");
	    System.out.println(new UnsafeStringEscape().largeGrainedTagFilter(value));
	    
	    
	    System.out.println("-------------StringEscapeUtils----------------------");
	    System.out.println(kk);
	    
	    
	    System.out.println("-------------HtmlEscapers----------------------");
	    System.out.println(HtmlEscapers.htmlEscaper().escape(value));
	    
	    System.out.println("-------------XmlEscapers----------------------");
	    System.out.println(XmlEscapers.xmlContentEscaper().escape(value));
	    
	    
}
	
	

}
