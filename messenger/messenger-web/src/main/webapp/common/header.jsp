<%@ include file="/common/taglibs.jsp"%>

<div id="branding">
	<div >
		<h1>
			<a href="<c:url value='/'/>">
				<fmt:message key="webapp.name" />
				<img alt="Delta Clinic" src="<c:url value='/images/sunflower.png'/>" style="width: 25px; height: 25px; position: relative 110px;">
			</a>
		</h1>
		<div style="float: left;">
			<i>"<fmt:message key="webapp.tagline" />"</i>
		</div>
		<div id="switchLocale">
			<c:if test="${pageContext.request.remoteUser != null}">
				<fmt:message key="loggedInAs"/> "${pageContext.request.remoteUser}" | 
        	 
				<a href="<c:url value='logout.jsp'/>"><fmt:message
				key="menu.logout" /> </a> |
			</c:if>
			<a href="<c:url value='${actualPage}?request_locale=en'/>"><fmt:message
				key="locale.english" /> </a> |
			<a href="<c:url value='${actualPage}?request_locale=sr'/>"><fmt:message
				key="locale.serbian" /> </a>
		</div>
	</div>
</div>
<hr />