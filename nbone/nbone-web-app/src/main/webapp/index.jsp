<html>
<head>

<script src="https://code.jquery.com/jquery-1.12.4.js" 
		integrity="sha256-Qw82+bXyGq6MydymqBxNPYTaUXXq7c8v3CwiYwLLNXU="
		crossorigin="anonymous">
</script>
</head>
<body>
<h2>Hello World!</h2>


<button value="put"  onclick="updateTest()">put</button>
<button value="delete" onclick="deleteTest()">delete</button>
<script type="text/javascript">

(function($) {
	
	var jQuery = $;
	
	$.postJSON = function(url,data,callback){
		
		return $.post(url,data,callback,"json");
	}
	$.postJson = $.postJSON;
	
	
	$.postXML = function(url,data,callback){
		
		return $.post(url,data,callback,"xml");
	}
	$.postXml = $.postXML;
	
	
	
	var jsonOptions = {
			contentType: "application/json",
            dataType: "json",
	};
	/**
	 * 封装ajax请求的总控制器
	 * @see jquery.js
	 * @param url 
	 * @param method
	 * @param data
	 * @param callback
	 * @param options ajax settings
	 */
	jQuery.send = function( url,method, data, callback, options) {
        //XXX:thinking
		if(method == null){
        	 method = "GET";
        }
		// Shift arguments if data argument was omitted
		if ( jQuery.isFunction( data ) ) {
			options = options || callback;
			callback = data;
			data = undefined;
		}
		//XXX:thinking
		if(!options){
			options = {};
		}

		// The url can be an options object (which then must have .url)
		return jQuery.ajax( jQuery.extend({},options,{
			url: url,
			type: method,
			data: data,
			success: callback
		}, jQuery.isPlainObject( url ) && url ) );
	};
	
	
	
	/**
	 * delete 是 js 关键字 故使用DELETE
	 * add http put patch delete method 
	 * @see jQuery.js
	 */
	jQuery.each( [ "put","patch", "DELETE" ], function( i, method ) {
		jQuery[ method ] = function( url, data, callback, type ) {
			jQuery.send(url, method, data, callback, type);
		
		};
	} );
	
	jQuery.httpDelete = function( url, data, callback, type){
		jQuery.send(url, "DELETE", data, callback, type);
	}
	
	

})(jQuery);

function updateTest(){
	var url = "/user/22";
	$.put(url,function(json){
		console.info(json);
	});
	
}
function deleteTest(){
	var url = "/user/23";
	jQuery.DELETE(url,function(json){
		console.info(json);
	});
	
}



</script>
</body>
</html>
