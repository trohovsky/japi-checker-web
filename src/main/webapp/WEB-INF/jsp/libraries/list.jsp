<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    
    <h2>Libraries</h2>
    
    <div class="form-horizontal">
        <div class="form-actions">
            <spring:url value="libraries/new.html" var="addUrl">
            </spring:url>
            <a href="${fn:escapeXml(addUrl)}" class="btn btn-success"><i class="icon-plus icon-white"></i> New Library</a>
    	</div>
    </div>
    
    <datatables:table id="libraries" data="${selections}" cdn="true" row="library" theme="bootstrap2"
                      cssClass="table table-striped" paginate="false" info="false" cssStyle="width: 400px;">
        <datatables:column title="Name">
            <spring:url value="libraries/{libraryId}.html" var="libraryUrl">
                <spring:param name="libraryId" value="${library.id}"/>
            </spring:url>
            <a href="${fn:escapeXml(libraryUrl)}"><c:out value="${library.name}"/></a>
        </datatables:column>
    </datatables:table>
    
    <jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>