package com.google.common;

import com.google.common.html.HtmlEscapers;

public class escapeTest {

	public static void main(String[] args) {

		String ss = HtmlEscapers.htmlEscaper().escape("'\"");
		String cn = HtmlEscapers.htmlEscaper().escape("中文标点”“‘’");
		
		System.out.println(ss);
		System.out.println(cn);
	}

}
