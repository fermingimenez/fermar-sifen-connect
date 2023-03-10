<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<style type="text/css">
.textareaMsg {
	overflow: hidden;
	border: 0px #000000 solid;
	background: none;
	font-weight: bold;
	font-family: Verdana, Helvetica, Arial, sans-serif;
	font-size: 12px;
	color: black;
	text-align: left;
	resize: none;
}
</style>

<script type="text/javascript">
	function init() {

	}
</script>

<html>

<head>
<base href="<%=basePath%>">

<title>SIFEN Utiles</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="SIFEN Utils">

<!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
</head>

<body onload="init();">

	<P>
	<FORM action="Respuesta" method="POST">

		<TABLE align="center" bgcolor="E0ECFF" width="400px" border="0">
			<TR>
				<TD align="center" colspan="2"><b>SIFEN Utiles</b></TD>
			</TR>
			<TR>
				<TD><textarea id="rDE" name="rDE"
						class="textareaMsg" value="${rDE}" >
									</textarea></TD>
			</TR>

			<TR>
				<TD><INPUT type="Submit" name="procesar" value="Invocar metodo" />
				</TD>
			</TR>
		</TABLE>
	</FORM>
</body>
</html>
