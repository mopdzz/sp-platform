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
                            <div class="titlebt">添加通道</div>
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
                <form id="spInfoForm" name="spInfoForm" method="post" action="sp!doAdd.action" onsubmit="return check();">
                    <input type="hidden" name="spInfo.id" value="${spInfo.id}">
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
                                        <td>名称：</td>
                                        <td>
                                            <div align="left">
                                                <input size="60" id="name" name="spInfo.name" value="${spInfo.name}" />
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20">
                                        <td>类型：</td>
                                        <td>
                                            <div align="left">
                                                <s:select list="{'IVR','SMS'}" name="spInfo.type" />
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20">
                                        <td>联系人：</td>
                                        <td>
                                            <div align="left">
                                                <input size="60" name="spInfo.contact" value="${spInfo.contact}" />
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20">
                                        <td>同步地址：</td>
                                        <td>
                                            <div align="left">
                                                <input size="60" id="syncurl" name="spInfo.syncurl" value="${spInfo.syncurl}" />
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20">
                                        <td>备注：</td>
                                        <td>
                                            <div align="left">
                                                <input size="60" name="spInfo.memo" value="${spInfo.memo}" />
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20">
                                        <td>状态：</td>
                                        <td>
                                            <div align="left">
                                                <select name="spInfo.status">
                                                    <option value="1" <c:if test="${spInfo.status == 1}">selected</c:if>>启用</option>
                                                    <option value="0" <c:if test="${spInfo.status == 0}">selected</c:if>>停用</option>
                                                </select>
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
        var name = $("#name").val();
        if (name == "") {
            alert("名称不能为空");
            $("#name").focus();
            return false;
        }
        var syncurl = $("#syncurl").val();
        if (syncurl == "") {
            alert("同步地址不能为空");
            $("#syncurl").focus();
            return false;
        }

        $("#spInfoForm").submit();
    }
</script>