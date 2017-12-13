<%@ page  contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>
<html>
<head class="head">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Jacobi</title>
    <sb:head/>
    <style>
        table.tabulation {
            font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
            font-size: 14px;
            border-collapse: collapse;
            text-align: center;
            margin-left: auto;
            margin-right: auto;
        }

        table.input{
            margin-left: auto;
            margin-right: auto;
        }

        th.tabulation, td:first-child.tabulation {
            background: #AFCDE7;
            color: white;
            padding: 10px 20px;
        }
        th.tabulation, td.tabulation {
            border-style: solid;
            border-width: 0 1px 1px 0;
            border-color: white;
        }
        td.tabulation {
            background: #D8E6F3;
        }
        th:first-child.tabulation, td:first-child.tabulation {
            text-align: left;
        }
        .head{
            font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
            background: #D8E6F3;
            width: 100%;
            float: none;
            position: relative;
            text-align: center;
        }

        .body{
            background: #AFCDE7 no-repeat center;
            width: 100%;
            height: 100%;
            float: none;
            position: relative;
            top: 5px;
            padding-bottom: 10px;
            align-content: center;
            text-align: center;
        }
        .button {
            background-color: #3b84c4;
            border: none;
            color: #FFFFFF;
            padding: 15px 32px;
            box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);
            text-align: center;
            transition-duration: 0.4s;
            margin: 16px 0 !important;
            text-decoration: none;
            font-size: 16px;
        }

        .button:hover {
            background-color: #3b84c4;
            border: none;
            color: #FFFFFF;
            padding: 15px 32px;
            box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
            text-align: center;
            transition-duration: 0.4s;
            margin: 16px 0 !important;
            text-decoration: none;
            font-size: 16px;
        }
        .input{
            padding: 10px;
            border: solid 2px #c9c9c9;
            transition: border 0.3s;
            height: 29px;
        }
        .input:focus{
            border: solid 2px #969696;
            height: 29px;
        }

        .blue {
            background-color: #3b84c4;
        }
        .semi-square {
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
        }
        .styled-select {
            font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
            height: 29px;
            overflow: hidden;
            width: 40px;
            color: #FFFFFF;
        }

        .text{
            font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
            font-size: 16px;
        }

        .error{
            color: red;
        }</style>
</head>
<body class="body">

<s:url action="JacobiAction"> </s:url>


<br>

<s:form action="JacobiAction"
        class="action.JacobiAction" method="post"
        enctype="multipart/form-data">
    <s:property value="exception"/>

    <s:textfield cssClass="input" type="text"  label="k" name="k" value="6"/>
    <s:textfield cssClass="input" type="text" label="b" name="b" value="1"/>
    <s:textfield cssClass="input" type="text" label="g" name="g" value="0.5"/>
    <s:textfield cssClass="input" type="text" label="d" name="d" value="0.02"/>
    <s:submit  cssClass="button" value="Calculate"/>
</s:form>


</body>
</html>