<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %> 
<html>

<head><title>Svg result</title>

</head>
<body>

<sx:div  executeScripts="true">
 
   	<embed src=<s:property value="filename"/> width="100%" height="85%"
	type="image/svg+xml"/>   
		
</sx:div>

</body>

</html> 