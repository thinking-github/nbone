--------------------------------------------------------------
<#assign array= [1,2,"kk"]>
<#assign array1 = ["chen","thinking"]>
<#assign map = {"id":"001","name":"chen2222222"} >

==========================================${array[1]}
==========================================${array[2]}
==========================================${map["id"]}
==========================================${map.id}

<#list array as item>
  ${item} ---------------
</#list>

<#list map?keys as key>
  ${key} --------------- ${map[key]} 
  
</#list>

${sharedChen}
${name}
${user.id}
${user.getId()}
${staticUser}
${dateO?date}
${dateO?time}
${dateO?datetime}

<#if chen?? && chen !="">
   object is not null..只能判断null  不能判断为""
<#else>
	object is  null....................s
</#if>


