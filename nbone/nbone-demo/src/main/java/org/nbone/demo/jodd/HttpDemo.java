package org.nbone.demo.jodd;

import java.io.File;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

public class HttpDemo {
	
	public static void main(String[] args) {
		//get();
	   // post();
		put();
		delete();
	}
	
	static void get(){
		//get
		HttpRequest httpRequest = HttpRequest.get("http://jodd.org");
		httpRequest.query("chen",1);
	    HttpResponse response = httpRequest.send();

	    System.out.println(response);
	    System.out.println(response.statusCode());
		
	}
	static void post(){
	    HttpResponse response = HttpRequest
	            .post("http://srv:8080/api/jsonws/user/get-user-by-id")
	            .form("userId", "10194")
	            .send();
	    
	    System.out.println(response);
	    
	    
	    
	    HttpRequest httpRequest = HttpRequest
	            .post("http://srv:8080/api/jsonws/dlapp/add-file-entry")
	            .form(
	                "repositoryId", "10178",
	                "folderId", "11219",
	                "sourceFileName", "a.zip",
	                "mimeType", "application/zip",
	                "title", "test",
	                "description", "Upload test",
	                "changeLog", "testing...",
	                "file", new File("d:\\a.jpg.zip")
	            );

	        HttpResponse httpResponse = httpRequest.send();
	}
	
	
	static void put(){
		//put
		HttpRequest httpRequest = HttpRequest.put("http://localhost:8080/user");
		httpRequest.form("id", "001","name","thinking","password","chen..");
	    HttpResponse response = httpRequest.send();

	    System.out.println(response);
	    System.out.println(response.statusCode());
		
	}
	
	static void delete(){
		//delete
		HttpRequest httpRequest = HttpRequest.delete("http://localhost:8080/user/22");
	    HttpResponse response = httpRequest.send();

	    System.out.println(response);
	    System.out.println(response.statusCode());
		
	}
	
	
	
}
