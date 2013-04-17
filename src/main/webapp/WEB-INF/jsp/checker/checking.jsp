<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <div id="checker-container">
		<form:form modelAttribute="checkingForm" action="check" method="get" class="form-inline">

			<label class="control-label" for="reference">Reference</label>

			<form:select path="referenceId" items="${reference}" itemValue="id"></form:select>

			<label class="control-label" for="newReleases">New</label>

			<form:select path="newId" items="${newRelease}" itemValue="id"></form:select>

				<button type="submit" class="btn btn-info">Check</button>
		</form:form>
    </div>
    <a href="libraries.html">Libraries</a>
	<jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>