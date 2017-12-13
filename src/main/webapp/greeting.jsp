<%@ page  contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
    <s:textfield label="k" name="k" value="6"/>
    <s:textfield label="b" name="b" value="1"/>
    <s:textfield label="g" name="g" value="0.5"/>
    <s:textfield label="d" name="d" value="0.02"/>
    <s:textfield label="привет" value="ку"/>
    <s:submit value="Calculate"/>
</s:form>


</body>
</html>