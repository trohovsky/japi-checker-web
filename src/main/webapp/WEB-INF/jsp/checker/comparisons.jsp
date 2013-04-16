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
    
    <h2>Releases</h2>
    
    <datatables:table id="comparisons" data="${comparisons}" cdn="true" row="comparison" theme="bootstrap2"
                      cssClass="table table-striped" paginate="false" info="false" cssStyle="width: 400px;" sort="false">
        <datatables:column title="Name">
            <c:choose>
				<c:when test="${not empty comparison.referenceRelease}">
					<spring:url value="releases/{referenceId}-{newId}.html" var="comparisonUrl">
						<spring:param name="referenceId" value="${comparison.referenceRelease.id}" />
						<spring:param name="newId" value="${comparison.newRelease.id}" />
					</spring:url>
					<a href="${fn:escapeXml(comparisonUrl)}">
					   <c:out value="${comparison.newRelease.name}" />
					</a>
				</c:when>
				<%-- dummy comparison to show initial release name --%>
				<c:otherwise>
					<c:out value="${comparison.newRelease.name}" />
				</c:otherwise>
			</c:choose>
        </datatables:column>
        <datatables:column title="Date">
            <c:out value="${comparison.newRelease.date}"></c:out>
        </datatables:column>
        <datatables:column title="Compatibility">
            <c:if test="${not empty comparison.referenceRelease}">
                <c:choose>
                    <c:when test="${comparison.isCompatible()}">
                        <span style="color: green">Compatible</span>
                    </c:when>
                    <c:otherwise>
                        <span style="color: red">Incompatible</span>
                    </c:otherwise>
                    </c:choose>
            </c:if>
        </datatables:column>
    </datatables:table>
    
    <jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>