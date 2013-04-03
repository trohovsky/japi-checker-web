<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="navbar">
    <div class="navbar-inner">
        <ul class="nav">
            <li><a href="<spring:url value="/" htmlEscape="true" />">Home</a></li>
            <li><a href="<spring:url value="/libraries/list.html" htmlEscape="true" />">Libraries</a></li>
            <li><a href="#" title="not available yet. Work in progress!!">Help</a></li>
        </ul>
    </div>
</div>
