<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body onload='document.login.j_username.focus();'>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <div id="login-container">
        <form class="login-form" name="login" action="j_spring_security_check" method="post" >
            <fieldset class="form-horizontal">
                <legend>Login</legend>
            
                <div class="control-group">
                    <label class="control-label" for="j_username">Username</label>
                    <div class="controls">
                        <input id="j_username" name="j_username" size="20" maxlength="50" type="text"/>
                    </div>
                </div>
            
	            <div class="control-group">
	                <label class="control-label"  for="j_password">Password</label>
	                <div class="controls">
	                    <input id="j_password" name="j_password" size="20" maxlength="50" type="password"/>
	                </div>
	            </div>
	            <div class="form-actions">
	                <input type="submit" value="Login" class="btn btn-primary"/>
	            </div>
	        </fieldset>
	    </form>
        <p style="color: red">${message}</p>
    </div>
</div>
</body>
</html>