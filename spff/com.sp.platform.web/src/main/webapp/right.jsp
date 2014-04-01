<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
    <link href="${stx}/css/skin.css" rel="stylesheet" type="text/css"/>
    <script src="${stx}/js/jquery-1.7.1.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
</head>

<body class="body">
    <table width="100%" height="95%" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td width="17" valign="top" background="${stx}/images/mail_leftbg.gif">
                <img src="${stx}/images/left-top-right.gif" width="17" height="29"/>
            </td>
            <td valign="top" background="${stx}/images/content-bg.gif">
                <table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table2">
                    <tr>
                        <td height="31">
                            <div class="titlebt">欢迎界面</div>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="16" valign="top" background="${stx}/images/mail_rightbg.gif">
                <img src="${stx}/images/nav-right-bg.gif" width="16" height="29"/>
            </td>
        </tr>
        <tr>
            <td height="200"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
            <td height="200" valign="center" bgcolor="#F7F8F9" style="height: 211px;">

                <table width="65%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="2" valign="top">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="center" colspan="4">
                            <table width="100%" cellpadding="0" class="Table1" border="1" cellspacing="0">
                                <tr height="20">
                                    <th class="left_bt3" colspan="2">欢迎使用增值业务管理系统</th>
                                </tr>
                                <tr class='t2'>
                                    <td width="50%">用户</td>
                                    <td>${sessionScope.SESSION_SCS_USER.name}</td>
                                </tr>
                                <tr>
                                    <td width="50%">备注</td>
                                    <td>${sessionScope.SESSION_SCS_USER.showname}</td>
                                </tr>
                                <tr class='t2'>
                                    <td width="50%">注册时间</td>
                                    <td>${sessionScope.SESSION_SCS_USER.ctime}</td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                </table>

            </td>
            <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
        </tr>
        <tr>
            <td height="50%"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
            <td height="50%" valign="center" bgcolor="#F7F8F9">&nbsp;</td>
            <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
        </tr>
        <jsp:include page="/down.jsp"/>
    </table>
</body>
</html>