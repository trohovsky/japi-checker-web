<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>

    <h2>Release Information</h2>

    <div class="form-horizontal">
    	<dl class="dl-horizontal">
        	<dt>Name</dt>
        	<dd><c:out value="${release.name}" /></dd>
    	</dl>
    	<dl class="dl-horizontal">
            <dt>Date</dt>
            <dd><fmt:formatDate value="${release.date}" type="both" pattern="dd-MM-yyyy" /></dd>
        </dl>
        <div class="form-actions">
            <spring:url value="{releaseId}/edit.html" var="editUrl">
                <spring:param name="releaseId" value="${release.id}"/>
            </spring:url>
            <a href="${fn:escapeXml(editUrl)}" class="btn btn-info"><i class="icon-pencil icon-white"></i> Edit Release</a>
    	</div>
    </div>

    <jsp:include page="../fragments/footer.jsp"/>

</div>

</body>

</html>
