<%@ include file="/common/taglibs.jsp"%>

<head></head>
<body>

<div class="container">
<h1><fmt:message key="welcome.title" /></h1>
<p>Locale = ${pageContext.response.locale}</p>
<hr>
<ul>
	<li><a href="?locale=en_us">us</a> | <a href="?locale=en_gb">gb</a>
	| <a href="?locale=es_es">es</a> | <a href="?locale=de_de">de</a></li>
</ul>
<ul>
	<li><a href="account">@Message Example</a></li>
</ul>

<display:table name="messages" id="message" class="table"
	requestURI="" export="true" pagesize="25">

	<display:column property="from" sortable="true"
		titleKey="message.from"
		href="message/${message.id}" />
	<display:column property="to" sortable="true"
		titleKey="message.to" />
	<display:column property="subject" sortable="true"
		titleKey="message.subject" />
	

	<display:setProperty name="export.excel.filename"
		value="message List.xls" />
	<display:setProperty name="export.csv.filename"
		value="message List.csv" />
	<display:setProperty name="export.pdf.filename"
		value="message List.pdf" />
</display:table></div>

<script type="text/javascript">
    Form.focusFirstElement($("serviceTypeForm"));
</script>

</body>