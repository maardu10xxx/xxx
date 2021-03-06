var SVGDocument = null;
var SVGRoot = null;
var lastElement = null;
var dataWin = null;
var absoluteURL= null;
var originalStrokeWidth=null;
var isOriginal=false;
var layerListCS=new Array(5);
var layerListSS=new Array(5);
var color=new Array("blueviolet","brown","chartreuse","darkorange","magenta","olivedrab"
		,"orangered","royalblue","tan","yellowgreen");


function Init(evt,absURL,layerListA,layerListB,initSide) {
	SVGDocument = evt.target.ownerDocument;
	SVGRoot = document.documentElement;
	absoluteURL=absURL;
	oriMenu = contextMenu.firstChild ;
	xmlDoc=getURL("../menudefine.xml",menuLoaded); //载入菜单
	//alert(layerListA.substring(1,layerListA.length-1).length);
	//alert(layerListB.substring(1,layerListB.length-1).length);
	/*layerListA=layerListA.substring(1,layerListA.length-1);
	if(layerListA.length > 0){
		layerListCS=layerListA.split(",");
	}*/
	layerListCS=layerListA.substring(1,layerListA.length-1).split(",");
	layerListSS=layerListB.substring(1,layerListB.length-1).split(",");
	//alert(layerListCS.length);
	//alert(layerListSS.length);
	chooseSide(initSide);
}

function menuLoaded(xmlDoc)
{
	if(xmlDoc.success)
	{
		//menus=parseXML(data.content,contextMenu);
    var newMenuRoot = parseXML(xmlDoc.content, contextMenu);
    contextMenu.replaceChild(newMenuRoot, contextMenu.firstChild);//替换菜单
	}
}

function aMouseDown(evt)
{
  if(evt.getButton() == 2)
  {
  	//alert(printNode(menus));
    var newMenuRoot = parseXML(printNode(menus), contextMenu);
    contextMenu.replaceChild(newMenuRoot, contextMenu.firstChild) ;
  }
}

//show and hide the labels
function ShowMyTooltip(evt,display) {
	try {
		var targetElement = evt.target.getParentNode();
		//get <text and <use nodes
		var partInfo = targetElement.getElementsByTagName("text").item(0);
		var useTag= targetElement.getElementsByTagName("use").item(0);

		if(display){	
			//change color and stroke width of selected part
			//only change it once/ the first time it's touched
			//otherwise storkeWidth would get huge quickly	
			if(!isOriginal){
				useTag.setAttribute( "color", "rgb(0,0,255)");
				
				originalStrokeWidth=useTag.getAttribute( "stroke-width" );
				var newStrokeWidth=Number(originalStrokeWidth);
				useTag.setAttribute( "stroke-width", String(newStrokeWidth) );
				
				//display the part info
				partInfo.setAttribute( "display", "inline");
				
				isOriginal=true;
			}
		}
		else{			
			//change color and stroke width back to normal
			useTag.removeAttribute( "color" );
			
			useTag.setAttribute( "stroke-width", originalStrokeWidth );
			
			//hide the part info
			partInfo.setAttribute( "display", "none");
			
			isOriginal=false;
		}
		
				
		
		
	}
	catch (er) {
	}
}

function chooseSide(side) {
	//var colorIndex=0;
	var useTag=document.getElementsByTagName("path");
	var useTagC=document.getElementsByTagName("circle");
  	for(var i = 0; i < useTag.length;i=i+1 ) {
	  	useTag.item(i).setAttribute("display","none");
  	}
  	for(var i = 0; i < useTagC.length;i=i+1 ) {
	  	useTagC.item(i).setAttribute("display","none");
  	}
	switch (side){
	case "CS":
		//length 1 means a empty layer list
		for(var j=0;j<layerListCS.length-1 ;j=j+2){
			//useTag=document.getElementsByTagName("path");
		  	for(var i = 0; i < useTag.length;i=i+1 ) {
			  	//alert("CS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+layerListCS[j]);
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"") == layerListCS[j].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
			  	useTag.item(i).setAttribute("color",layerListCS[j+1]);
			  	//alert("CSin: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+useTag.item(i).getAttribute("color"));
		  	}
		  	}
		  	for(var i = 0; i < useTagC.length;i=i+1 ) {
			  	//alert("CSC: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+layerListCS[j]);
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[j].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
			  	useTagC.item(i).setAttribute("color",layerListCS[j+1]);
			  	//alert("CSinC: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+useTag.item(i).getAttribute("color"));
		  	}
		  	}
		}
		break;
	case "SS":
		for(var j=0;j<layerListSS.length-1;j=j+2){
			//useTag=document.getElementsByTagName("path");
		  	for(var i = 0; i < useTag.length;i=i+1 ) {
			  	//alert("SS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+layerListSS[j]);
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[j].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
			  	useTag.item(i).setAttribute("color",layerListSS[j+1]);
			  	//alert("SS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+useTag.item(i).getAttribute("color"));
		  	}
		  	}
		  	for(var i = 0; i < useTagC.length;i=i+1 ) {
			  	//alert("SS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+layerListSS[j]);
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[j].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
			  	useTagC.item(i).setAttribute("color",layerListSS[j+1]);
			  	//alert("SS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+useTag.item(i).getAttribute("color"));
		  	}
		  	}
		}
		break;
	case "AB":
		//length 1 means a empty layer list
		//alert("CS layerlist:" + layerListCS.length);
		//alert("ss layerlist: " +layerListSS.length);
		for(var j=0;j<layerListCS.length-1 ;j=j+2){
			//useTag=document.getElementsByTagName("path");
		  	for(var i = 0; i < useTag.length;i=i+1 ) {
			  	//alert("CS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+layerListCS[j]);
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[j].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
			  	useTag.item(i).setAttribute("color",layerListCS[j+1]);
			  	//alert("CS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+useTag.item(i).getAttribute("color"));
		  	}
		  	}
		  	for(var i = 0; i < useTagC.length;i=i+1 ) {
			  	//alert("CS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+layerListCS[j]);
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[j].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
			  	useTagC.item(i).setAttribute("color",layerListCS[j+1]);
			  	//alert("CS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+useTag.item(i).getAttribute("color"));
		  	}
		  	}
		}
		for(var j=0;j<layerListSS.length-1;j=j+2){
			//useTag=document.getElementsByTagName("path");
		  	for(var i = 0; i < useTag.length;i=i+1 ) {
			  	//alert("SS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+layerListSS[j]);
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[j].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
			  	useTag.item(i).setAttribute("color",layerListSS[j+1]);
			  	//alert("SS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+useTag.item(i).getAttribute("color"));
		  	}
		  	}
		  	for(var i = 0; i < useTagC.length;i=i+1 ) {
			  	//alert("SS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+layerListSS[j]);
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[j].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
			  	useTagC.item(i).setAttribute("color",layerListSS[j+1]);
			  	//alert("SS: "+ i+":"+useTag.item(i).getAttribute("layer")+":"+useTag.item(i).getAttribute("color"));
		  	}
		  	}
		}
		break;
	case "layer01":
	  	for(var i = 0; i < useTag.length;i=i+1 ) {
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[0].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	for(var i = 0; i < useTagC.length;i=i+1 ) {
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[0].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	break;
	case "layer02":
	  	for(var i = 0; i < useTag.length;i=i+1 ) {
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[1].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	for(var i = 0; i < useTagC.length;i=i+1 ) {
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[1].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	break;
	case "layer03":
	  	for(var i = 0; i < useTag.length;i=i+1 ) {
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[2].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	for(var i = 0; i < useTagC.length;i=i+1 ) {
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListCS[2].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	break;
	case "layer04":
	  	for(var i = 0; i < useTag.length;i=i+1 ) {
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[0].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	for(var i = 0; i < useTagC.length;i=i+1 ) {
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[0].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	break;
	case "layer05":
	  	for(var i = 0; i < useTag.length;i=i+1 ) {
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[1].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	for(var i = 0; i < useTagC.length;i=i+1 ) {
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[1].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	break;
	case "layer06":
	  	for(var i = 0; i < useTag.length;i=i+1 ) {
		  	if(useTag.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[2].replace(/ /g,"")){
			  	useTag.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	for(var i = 0; i < useTagC.length;i=i+1 ) {
		  	if(useTagC.item(i).getAttribute("layer").replace(/ /g,"")==layerListSS[2].replace(/ /g,"")){
			  	useTagC.item(i).setAttribute("display","inline");
		  	}
		  	}
	  	break;
	}
}
//open popup when a part is clicked on
function aMouseClick(evt) {
		
		//only react to left mouse button click
		if (evt.getButton()==0) {
			//close old window
      		if (dataWin!==null) {
      			dataWin.close();
      		} 
      		//open new popup window
      		//need absolute URL, i think due to security reasons
      		// second param must not contain whitespaces!
      		//alert("abc"+ window);
      		dataWin=parent.open(absoluteURL + "/goToFailureReport.action?partname="+  evt.currentTarget.id, 
      							"failureReportWindow",  
		         				"width=600, height=500, left=400, top=0, scrollbars=no, toolbar=no,location=no, menubar=no, resizable=yes, status=yes");
			//make sure the popup has the correct parent set
			if (dataWin.opener == null) dataWin.opener = parent;    				      
      		//parent.document.getElementById("inputFID");
			parent.setFocusFID();
			
      		dataWin.focus();
      		
    	}	
		
	
}

