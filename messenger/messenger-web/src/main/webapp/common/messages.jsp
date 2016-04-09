<%@ include file="/common/taglibs.jsp"%>

<%-- ActionError Messages - usually set in Actions --%>
<!--<c:if test="${not empty actionErrors}">
	<div class="error" id="errorMessages">
		<c:forEach var="actionError" items="${actionErrors}">
			<img src="<c:url value="/images/warning.png"/>"
					alt="<fmt:message key="warning"/>" class="icon" />
			<c:out value="${actionError}" />
			<br />
		</c:forEach>
	</div>
</c:if>

-->
<%-- FieldError Messages - usually set by validation rules --%>
<!--<c:if test="${not empty fieldErrors}">
	<div class="error" id="errorMessages">
		<img 	src="<c:url value="/images/warning.png"/>"
				alt="<fmt:message key="warning"/>" class="icon" />
		<fmt:message key="warning.invalid.fields" />
	</div>
</c:if>
	
-->
<%-- Success Messages --%>
<!--<c:if test="${not empty messages}">
	<div class="message" id="successMessages">
	<c:forEach var="msg" items="${messages}">
		<img 	src="<c:url value="/images/information.png"/>"
				alt="<fmt:message key="information"/>" class="icon" />
		<c:out value="${msg}" />
		<br />
	</c:forEach></div>
	<c:remove var="messages" scope="session" />
</c:if>-->