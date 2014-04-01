<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
    <title>增值业务管理系统</title>
    <meta http-equiv=Content-Type content=text/html;charset=utf-8>
    <base target="main">
    <link href="${stx}/css/skin.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" style="margin-right: 0px;">
<table width="100%" height="64" border="0" cellpadding="0" cellspacing="0" class="admin_topbg">
    <tr>
        <td width="61%" height="64"><img src="${stx}/images/logo.gif" width="262" height="64"></td>
        <td width="39%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="74%" height="38" class="top_txt">
                        <b>${sessionScope.SESSION_SCS_USER.name}</b> 您好, 感谢登陆使用！
                    </td>
                    <td align="center">
                        <img src="${stx}/images/out.gif" alt="安全退出" width="46" height="20" border="0" onclick="topLogout();"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
<script type="text/javascript">
    function topLogout(){
        if (confirm("您确定要退出管理系统？"))
            top.location = "logout.action";
        return false;
    }
</script>
