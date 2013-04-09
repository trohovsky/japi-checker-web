<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    
        <datatables:table id="comparison" data="${comparison.differences}" cdn="true" row="difference" theme="bootstrap2"
                      cssClass="table table-striped" paginate="false" info="false">
        <datatables:column title="severity">
            <c:out value="${difference.differenceType.severity}"/>
        </datatables:column>
        <datatables:column title="source">
            <c:out value="${difference.source}"/>
        </datatables:column>
        <datatables:column title="message">
            <c:out value="${difference.message}"/>
        </datatables:column>
    </datatables:table>
    
	<jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>