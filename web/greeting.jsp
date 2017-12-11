<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Jacobi</title>
</head>
<body>

<s:url action="JacobiAction"> </s:url>


<br>

<s:form action="JacobiAction"
        class="action.JacobiAction" method="post"
        enctype="multipart/form-data">
    <s:textfield label="k" key="k" value="6"></s:textfield>
    <s:textfield label="b" key="b" value="1"></s:textfield>
    <s:textfield label="g" key="g" value="0.5"></s:textfield>
    <s:textfield label="d" key="d" value="0.02"></s:textfield>
    <s:submit value="Calculate"/>
</s:form>


</body>
</html>