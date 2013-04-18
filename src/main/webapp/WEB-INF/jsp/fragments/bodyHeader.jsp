<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h1>Java API Checker</h1>
<div class="navbar">
    <div class="navbar-inner">
        <ul class="nav">
            <li><a href="<spring:url value="/" htmlEscape="true" />">Home</a></li>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
            <li><a href="<spring:url value="/admin/libraries.html" htmlEscape="true" />">Libraries</a></li>
            </sec:authorize>
            <li><a href="<spring:url value="/help.html" htmlEscape="true" />">Help</a></li>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
            <li><a href="<spring:url value="/logout" htmlEscape="true" />">Logout</a></li>
            </sec:authorize>
        </ul>
    </div>
</div>
