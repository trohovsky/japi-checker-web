<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body>
<script>
    $(function () {
        $("#date").datepicker({ dateFormat: 'dd-mm-yy'});
    });
</script>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <c:choose>
        <c:when test="${release['new']}"><c:set var="method" value="post"/></c:when>
        <c:otherwise><c:set var="method" value="put"/></c:otherwise>
    </c:choose>

    <h2>
        <c:choose>
        <c:when test="${release['new']}">New Release</c:when>
        <c:otherwise>Edit Release</c:otherwise>
        </c:choose>
    </h2>
    <form:form modelAttribute="release" method="${method}" enctype="multipart/form-data" class="form-horizontal" id="add-release-form">
        <div class="control-group">
            <label class="control-label" for="name">Name</label>
            <div class="controls">
                <form:input path="name"/>
                <form:errors path="name"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="date">Date</label>
            <div class="controls">
                <form:input path="date"/>
                <form:errors path="date"/>
            </div>
        </div>
        <c:if test="${release['new']}">
        <div class="control-group">    
            <label class="control-label" for="file">JAR archive</label>
            <div class="controls">
                <input type="file" name="file"/>
            </div>
        </div>
        </c:if>

        <div class="form-actions">
            <c:choose>
                <c:when test="${release['new']}">
                    <button type="submit" class="btn btn-info">Add Release</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-info">Update Release</button>
                </c:otherwise>
            </c:choose>
        </div>
    </form:form>
</div>
<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
