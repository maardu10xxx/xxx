<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %> 
<%@ page isELIgnored="false"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    
    <title>目检图形文件上传及格式转换 DXF->SVG</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<s:head />
	<sx:head debug="false" cache="false" compressed="false" />

	<link rel="stylesheet" type="text/css" href="./css/interface.css">
				
  </head>
  <script type="text/javascript" src="./js/openWindow.js"></script>  
  <script type="text/javascript">
  	function csChecked(){
  		//alert(document.getElementsByTagName("path").length);
  	}
  	function ssChecked(){
  		//alert("test");
  	}
  	
  	function checkApprove()
  	{
  		if(confirm("确认上传")){
	  		return true;
  		}else{
	  		return false;
  		}
  	}
  </script>
  <body>
  
  <%
  String role=(String)session.getAttribute("role");
  %>
  
  <div id="content"	align="left" style="background-color: beige;">
  <table>
    <tr>
    <s:form action="uploadAction" id="Xdf2Svg" method ="POST" enctype ="multipart/form-data">
				<%
				if(role.equalsIgnoreCase("creator"))
				{
				%>
				<td Style="height:1cm;"><s:file id="uploadFile" labelposition="left" key="oriFile"/></td> 
				<td>
				<s:submit 
				cssStyle="width:1.5cm;" 
				key="pageLabel.uploadFile" 
				/>
			 	</td>
				<%
				}
				%>
	</s:form>
	
    <s:form action="sideMaintenance" target="_blank">
		<td><s:hidden key="pageLabel.partNo"></s:hidden></td>
		<td><s:hidden key="pageLabel.partAs"></s:hidden></td>
		 <td>
		 <%
		if(role.equalsIgnoreCase("creator"))
		{
		%>
		<s:submit cssStyle="width:2cm;" key="pageLabel.SideLayer"/>
		<%
		}
		%>
	 	</td>
    </s:form>
    <!--<s:form action="optionTransferSelectAction">
		 <td><sx:submit cssStyle="width:2cm;" key="pageLabel.SideLayer"/></td>
    </s:form > action="loadSvg" 
    
    -->
    <s:form>
    </s:form>
    
    <s:form action="userManual"  name="loadSvg" id="loadSvg"  target="_blank">
		<td><s:textfield labelposition="left" key="pageLabel.partNo"></s:textfield></td>
		<td><s:textfield cssStyle="width:1cm" labelposition="left" key="pageLabel.partAs"></s:textfield></td>
 	    <td><s:checkbox labelposition="left" key="pageLabel.chooseCs" value="true" onclick="csChecked()"/></td>
	    <td><s:checkbox labelposition="left" key="pageLabel.chooseSs" value="true" onclick="ssChecked()"/></td>
       <td><s:submit cssStyle="width:1.5cm;" key="pageLabel.userManual" /></td>	

    </s:form>
    
       <td><sx:submit cssStyle="width:1.5cm;" href="loadSvg.action" key="pageLabel.showPhoto" targets="photoDiv" formId="loadSvg"/></td>			    		
       <td>
		 <%
		if(role.equalsIgnoreCase("creator"))
		{
		%>
       	<sx:submit cssStyle="width:1.5cm;" href="unApprove.action" key="pageLabel.unApprove"  targets="photoDiv" formId="loadSvg"/>
		<%
		}
		else if(role.equalsIgnoreCase("approver"))
		{
		%>
       	<sx:submit cssStyle="width:1.5cm;" href="approve.action" key="pageLabel.approve"  targets="photoDiv" formId="loadSvg"/>
		<%
		}
		%>
       </td>			    		
    
 
	</tr>
  </table>
  </div>
  <s:div>
	<sx:div id="photoDiv">
   		<iframe name="photoframe" src=<s:property value="filename"/> width="100%" height="85%"
		/></iframe>
   		<!-- <embed src=<s:property value="filename"/> width="100%" height="85%"
		type="image/svg-xml"  pluginspage="http://www.adobe.com/svg/viewer/install/"/>-->
	</sx:div>
  </s:div>
	</body>
</html>
