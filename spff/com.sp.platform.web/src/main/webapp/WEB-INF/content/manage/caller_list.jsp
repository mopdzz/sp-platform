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
                        <div class="titlebt">拨打记录</div>
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
        <form action="caller!list.action" name="snumForm" method="post">

            <td width="17" height="31" valign="top" background="${stx}/images/mail_leftbg.gif">
                <img src="${stx}/images/left-top-right.gif" width="17" height="29"/>
            </td>
            <td height="31" valign="top" background="${stx}/images/content-bg.gif">
                <table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table3">
                    <tr>
                        <td class="left_txt" height="31" width="20%">
                            主叫号码：
                            <input type="text" name='pageView.caller' value="${pageView.caller}"/>
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
        <td height="20"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td >统计表</td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
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
                                <th width="8%" class="left_bt2">ID</th>
                                <th width="8%" class="left_bt2">主叫号码</th>
                                <th width="8%" class="left_bt2">被叫号码</th>
                                <th width="8%" class="left_bt2">开始时间</th>
                                <th width="8%" class="left_bt2">结束时间</th>
                                <th width="5%" class="left_bt2">时长</th>
                                <th width="5%" class="left_bt2">费用(分)</th>
                                <th width="5%" class="left_bt2">省市</th>
                                <th width="8%" class="left_bt2">扣量标志</th>
                            </tr>
                            <s:iterator value="list" id="bean">
                                <tr class='t2'>
                                    <td>${bean.id}</td>
                                    <td>${bean.caller}</td>
                                    <td>${bean.called}</td>
                                    <td><fmt:formatDate value="${bean.btime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td><fmt:formatDate value="${bean.etime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>${bean.time}</td>
                                    <td>${bean.fee}</td>
                                    <td>${bean.province}_${bean.city}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${bean.type=='1'}">
                                                扣量
                                                <font color="blue">
                                                    &nbsp;<a href="caller!bufa.action?pageView.caller=${bean.caller}&pageView.id=${bean.id}">补发</a>
                                                </font>
                                            </c:when>
                                            <c:otherwise>
                                                <font color="red">普通</font>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>

            </table>

        </td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
    </tr>
    <tr>
        <td height="20"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td >临时表</td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
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
                                <th width="8%" class="left_bt2">ID</th>
                                <th width="8%" class="left_bt2">主叫号码</th>
                                <th width="8%" class="left_bt2">被叫号码</th>
                                <th width="8%" class="left_bt2">开始时间</th>
                                <th width="8%" class="left_bt2">结束时间</th>
                                <th width="5%" class="left_bt2">时长</th>
                                <th width="5%" class="left_bt2">费用(分)</th>
                                <th width="8%" class="left_bt2">扣量标志</th>
                            </tr>
                            <s:iterator value="list2" id="bean">
                                <tr class='t2'>
                                    <td>${bean.id}</td>
                                    <td>${bean.caller}</td>
                                    <td>${bean.called}</td>
                                    <td><fmt:formatDate value="${bean.btime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td><fmt:formatDate value="${bean.etime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>${bean.time}</td>
                                    <td>${bean.fee}</td>
                                    <td>同步[${bean.sendnum}]次
                                        <c:if test="${bean.sendnum >= 3}">
                                            <font color="blue">
                                                &nbsp;<a href="caller!sync.action?pageView.caller=${bean.caller}&pageView.id=${bean.id}">重新同步</a>
                                            </font>
                                        </c:if>
                                    </td>
                                </tr>
                            </s:iterator>
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

<script src="${stx}/js/jquery-1.7.1.min.js"></script>
<script src="${stx}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    function confirmdel(id) {
        if (confirm("确认删除?")) {
            location.href = "cpnum!delete.action?pageView.id=" + id;
        }
    }
    function confirmUpdate(id) {
        location.href = "cpnum!add.action?pageView.id=" + id ;
    }
</script>
