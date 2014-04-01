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
        <td width="17" height="31" valign="top" background="${stx}/images/mail_leftbg.gif">
            <img src="${stx}/images/left-top-right.gif" width="17" height="29"/>
        </td>
        <td height="31" valign="top" background="${stx}/images/content-bg.gif">
            <table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table2">
                <tr>
                    <td height="31" width="100">
                        <div class="titlebt">修改密码</div>
                    </td>
                    <td class="left_txt" align="left" style="color: red; padding-right: 5px;">
                        <a href="javascript:history.back(-1);" style="color: red;">返回上一级</a>
                    </td>
                </tr>
            </table>
        </td>
        <td height="31" width="16" valign="top" background="${stx}/images/mail_rightbg.gif">
            <img src="${stx}/images/nav-right-bg.gif" width="16" height="29"/>
        </td>
    </tr>
    <tr>
        <td height="200"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td height="200" valign="top" bgcolor="#F7F8F9">
            <form id="spInfoForm" name="spInfoForm" method="post" action="cp!doUpdate.action" onsubmit="return check();">
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="2" valign="top">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="center" colspan="4">
                            <table width="60%" cellpadd
                                   ing="0" class="Table1" border="1" cellspacing="0">
                                <tr class='t2' height="20">
                                    <td>原密码：</td>
                                    <td>
                                        <div align="left">
                                            <input size="60" type="password" id="passwd" name="pageView.passwd" value="" /><font color="red">${pageView.message}</font>
                                        </div>
                                    </td>
                                </tr>
                                <tr class='t2' height="20">
                                    <td>新密码：</td>
                                    <td>
                                        <div align="left">
                                            <input type="password" size="60" id="memo" name="pageView.memo" value="" />
                                        </div>
                                    </td>
                                </tr>
                                <tr class='t2' height="20">
                                    <td>重新输入：</td>
                                    <td>
                                        <div align="left">
                                            <input type="password" size="60" id="memo2" name="memo2" value="" />
                                        </div>
                                    </td>
                                </tr>

                                <tr class='t2' height="25">
                                    <td></td>
                                    <td>
                                        <div align="left">
                                            <input type="submit" value="提交"/>&nbsp;<input type="reset" value="重置"/>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                </table>
            </form>
        </td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
    </tr>
    <tr>
        <td height="220"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td height="220" valign="center" bgcolor="#F7F8F9">&nbsp;</td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
    </tr>
    <jsp:include page="/down.jsp"/>
</table>
</body>
</html>
<script src="${stx}/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
    function check(){
        var passwd = $("#passwd").val();
        if (passwd == "") {
            alert("原密码不能为空");
            $("#passwd").focus();
            return false;
        }
        var memo = $("#memo").val();
        if (memo == "") {
            alert("新密码不能为空");
            $("#memo").focus();
            return false;
        }

        var memo2 = $("#memo2").val();
        if (memo != memo2) {
            alert("两次密码不一样");
            return false;
        }

        $("#spInfoForm").submit();
    }
</script>