<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <c:choose>
        <c:when test="${library['new']}"><c:set var="method" value="post"/></c:when>
        <c:otherwise><c:set var="method" value="put"/></c:otherwise>
    </c:choose>

    <h2>
    	<c:choose>
        	<c:when test="${library['new']}">New Library</c:when>
        	<c:otherwise>Edit Library</c:otherwise>
    	</c:choose>
    </h2>
    <form:form modelAttribute="library" method="${method}" class="form-horizontal" id="add-library-form">
        <div class="control-group">
        	<label class="control-label" for="name">Name</label>
        	<div class="controls">
        		<input type="text" name="name" value="${library.name}"/>
        		<form:errors path="name"/>
  	      	</div>
        </div>

        <div class="form-actions">
            <c:choose>
                <c:when test="${library['new']}">
                    <button type="submit" class="btn btn-info">Add Library</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-info">Update Library</button>
                </c:otherwise>
            </c:choose>
        </div>
    </form:form>
</div>
<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
