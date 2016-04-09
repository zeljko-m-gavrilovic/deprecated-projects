<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<%@ include file="/common/taglibs.jsp"%>
	<%@ include file="/common/meta.jsp"%>
	<%@ include file="/common/scripts.jsp"%>
	<%@ include file="/common/styles.jsp"%>
	<title><decorator:title /> | <fmt:message key="webapp.name" /></title>
	<decorator:head />
</head>
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> >
	<div id="page">
		<div id="header" class="clearfix">
			<jsp:include page="/common/header.jsp" />
		</div>
		<div id="content" class="clearfix">
			<div id="menu_div" class="yui-skin-sam" >
				<!--<h:menu />-->
			</div> 
			
			<br/>
			<hr/>
			<div id="main">
				<!--<h:breadcrumb />
				<br/><br/>-->
				<h1 id="pageTitle"><decorator:getProperty property="meta.heading" /></h1>
				
				<%@ include file="/common/messages.jsp"%>
				
				<decorator:body />
			</div>
			MIKA ZIKA
			
		</div>
		
		<div id="footer" class="clearfix">
			<jsp:include page="/common/footer.jsp" />
		</div>
	</div>
</body>
</html>