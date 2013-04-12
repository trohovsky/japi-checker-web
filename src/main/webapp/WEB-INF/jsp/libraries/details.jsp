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

    <h2>Library Information</h2>

    <div class="form-horizontal">
    	<dl class="dl-horizontal">
        	<dt>Name</dt>
        	<dd><c:out value="${library.name}" /></dd>
    	</dl>
        <div class="form-actions">
            <spring:url value="{libraryId}/edit.html" var="editUrl">
                <spring:param name="libraryId" value="${library.id}"/>
            </spring:url>
            <a href="${fn:escapeXml(editUrl)}" class="btn btn-info"><i class="icon-pencil icon-white"></i> Edit Library</a>
            <spring:url value="{libraryId}/releases/new.html" var="addUrl">
                <spring:param name="libraryId" value="${library.id}"/>
            </spring:url>
            <a href="${fn:escapeXml(addUrl)}" class="btn btn-success"><i class="icon-plus icon-white"></i> Add New Release</a>
    	</div>
    </div>

    <h3>Releases</h3>
    
    <datatables:table id="libraries" data="${library.releases}" cdn="true" row="release" theme="bootstrap2"
                      cssClass="table table-striped" paginate="false" info="false">
        <datatables:column title="Name">
            <spring:url value="{libraryId}/releases/{releaseId}.html" var="releaseUrl">
                <spring:param name="libraryId" value="${library.id}"/>
                <spring:param name="releaseId" value="${release.id}"/>
            </spring:url>
            <a href="${fn:escapeXml(releaseUrl)}"><c:out value="${release.name}"/></a>
        </datatables:column>
        <datatables:column title="Updated">
            <fmt:formatDate value="${release.date}" type="both" pattern="dd-MM-yyyy" />
        </datatables:column>
    </datatables:table>

    <jsp:include page="../fragments/footer.jsp"/>

</div>

</body>

</html>
