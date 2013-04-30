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
    	Import Libraries from Artifacts
    </h2>
    <form:form modelAttribute="artifactsForm" method="post" class="form-horizontal" name="artifacts">
        <p>
        Please write one Maven artifact (whitespace separated groupId and artifactId) per line. Example:<br>
        <em>
        com.google.inject guice<br>
        com.googlecode.japi-checker japi-checker
        </em>
        </p>
        <p>
        Important: Aartifacts will be downloaded from Maven Central, which has limited download speed. 
        Thus downloading of artifacts may take a long time. Approximate speed of processing is 9 MB/min.
        </p>
        <div class="control-group">
        	<label class="control-label" for="artifactsText">Artifacts</label>
        	<div class="controls">
        		<form:textarea path="artifactsText" rows="15" class="input-xxlarge" />
        		<form:errors path="artifactsText"/>
  	      	</div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Import Libraries</button>
        </div>
    </form:form>
</div>
<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
