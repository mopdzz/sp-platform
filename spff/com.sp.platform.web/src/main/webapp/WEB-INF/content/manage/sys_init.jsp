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
                            <div class="titlebt">在线操作</div>
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
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="2" valign="top">${pageView.message}&nbsp;</td>
                        <td>&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="center" colspan="4">
                            <table width="60%" cellpadding="0" class="Table1" border="1" cellspacing="0">
                                <tr class='t2' height="20">
                                    <form action="sys!select1.action" method="post">
                                        <td>
                                            <div align="left">
                                                <input size="60" id="select" name="pageView.passwd" value="${pageView.passwd}" />
                                            </div>
                                        </td>
                                        <td><input type="submit" value="查询" /> </td>
                                    </form>
                                </tr>
                                <tr class='t2' height="20">
                                    <form action="sys!update1.action" method="post">
                                        <td>
                                            <div align="left">
                                                <input size="60" id="exec" name="pageView.name" value="${pageView.name}" />
                                            </div>
                                        </td>
                                        <td><input type="submit" value="提交" /> </td>
                                    </form>
                                </tr>
                            </table>
                        </td>
                    </tr>

                </table>
                <br>
                <table width="100%" class="Table1" border="1" cellpadding="0" cellspacing="0">
                    <s:iterator value="list" id="maps">
                        <tr>
                            <s:iterator value="maps" id="col">
                                <td>${col}</td>
                            </s:iterator>
                        </tr>
                    </s:iterator>
                </table>
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
        var name = $("#name").val();
        if (name == "") {
            alert("名称不能为空");
            $("#name").focus();
            return false;
        }
        var passwd = $("#passwd").val();
        if (passwd == "") {
            alert("密码不能为空");
            $("#passwd").focus();
            return false;
        }

        $("#cpForm").submit();
    }
</script>