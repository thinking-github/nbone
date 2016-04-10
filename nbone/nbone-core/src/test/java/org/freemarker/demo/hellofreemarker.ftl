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
${user.getChen()}
${staticUser}
${dateO?date}
${dateO?time}
${dateO?datetime}

