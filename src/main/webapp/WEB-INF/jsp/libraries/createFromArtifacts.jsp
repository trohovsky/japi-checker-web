<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body onload='document.artifacts.artifactsText.focus();'>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <h2>
    	Create Libraries from Artifacts
    </h2>
    <form:form modelAttribute="artifactsForm" method="post" class="form-horizontal" name="artifacts">
        <div class="control-group">
        	<label class="control-label" for="artifactsText">Artifacts</label>
        	<div class="controls">
        		<form:textarea path="artifactsText" rows="15" cols="80" />
        		<form:errors path="artifactsText"/>
  	      	</div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Add Artifacts</button>
        </div>
    </form:form>
</div>
<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
