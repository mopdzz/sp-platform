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
                        <div class="titlebt">业务号码</div>
                    </td>
                    <td class="left_txt" align="left" style="color: red; padding-right: 5px;">
                        <a href="javascript:history.back(-1);" style="color: red;">返回上一级</a>
                    </td>
                    <td class="left_txt" align="right" style="color: red; padding-right: 33px;">
                        <a href="snum!add.action" style="color: red;">添加业务号码</a>
                    </td>
                </tr>
            </table>
        </td>
        <td height="31" width="16" valign="top" background="${stx}/images/mail_rightbg.gif">
            <img src="${stx}/images/nav-right-bg.gif" width="16" height="29"/>
        </td>
    </tr>
    <tr>
        <form action="snum!list.action" name="snumForm" method="post">

            <td width="17" height="31" valign="top" background="${stx}/images/mail_leftbg.gif">
                <img src="${stx}/images/left-top-right.gif" width="17" height="29"/>
            </td>
            <td height="31" valign="top" background="${stx}/images/content-bg.gif">
                <table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table3">
                    <tr>
                        <td class="left_txt" height="31" width="30%">
                            选择合作方：
                            <s:select theme="simple" name="pageView.spid" list="list2" listKey="id"
                                      listValue="name" headerKey="0" headerValue="全部">
                            </s:select>
                        </td>
                        <td class="left_txt" align="left" style="color: red; padding-right: 5px;">
                            <input type="submit" value="查询" />
                        </td>
                        <td class="left_txt" align="right">
                        </td>
                    </tr>
                </table>
            </td>
            <td height="31" width="16" valign="top" background="${stx}/images/mail_rightbg.gif">
                <img src="${stx}/images/nav-right-bg.gif" width="16" height="29"/>
            </td>

        </form>
    </tr>
    <tr>
        <td height="200"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td height="200" valign="top" bgcolor="#F7F8F9">

            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="2" valign="top">&nbsp;</td>
                    <td>&nbsp;</td>
                    <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" colspan="4">
                        <table width="100%" cellpadd
                               ing="0" class="Table1" border="1" cellspacing="0">
                            <tr height="20">
                                <th width="5%" class="left_bt2">ID</th>
                                <th width="7%" class="left_bt2">通道方</th>
                                <th width="10%" class="left_bt2">业务号码</th>
                                <th width="10%" class="left_bt2">渠道方</th>
                                <th width="8%" class="left_bt2">资费</th>
                                <th width="5%" class="left_bt2">用户日上限</th>
                                <th width="5%" class="left_bt2">用户月上限</th>
                                <th width="5%" class="left_bt2">状态</th>
                                <th width="14%" class="left_bt2">屏蔽地市</th>
                                <th width="7%" class="left_bt2">操作</th>
                            </tr>
                            <s:iterator value="list" id="snum">
                                <tr class='t2'>
                                    <td>${snum.id}</td>
                                    <td>${snum.spname}</td>
                                    <td>${snum.called}</td>
                                    <td>${snum.cpname}</td>
                                    <td>${snum.fee}</td>
                                    <td>${snum.daylimit}</td>
                                    <td>${snum.monthlimit}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${snum.status=='1'}">
                                                <font color="blue">启用</font>
                                            </c:when>
                                            <c:otherwise>
                                                <font color="red">停用</font>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${snum.memo}</td>
                                    <td><a href="#" onclick="confirmUpdate(${snum.id})">修改</a> <a href="#" onclick="confirmdel(${snum.id})">删除</a> </td>
                                </tr>
                            </s:iterator>
                            <tr>
                                <td colspan="15" align="right" ><div class="PageGoTo">${pageGoto}</div></td>
                            </tr>
                        </table>
                    </td>
                </tr>

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

<script type="text/javascript">
    function confirmdel(id) {
        if (confirm("确认删除?")) {
            location.href = "snum!delete.action?pageView.id=" + id;
        }
    }
    function confirmUpdate(id) {
        location.href = "snum!add.action?pageView.id=" + id ;
    }

</script>