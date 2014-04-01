<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<tr>
    <td background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
    <td bgcolor="#F7F8F9">
        <table width="100%" height="1" border="0" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">
            <tr>
                <td></td>
            </tr>
        </table>
    </td>
    <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
</tr>
<tr>
    <td background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
    <td bgcolor="#F7F8F9">
        <table width="80%">
            <tr>
                <td width="2%" height="31">&nbsp;</td>
                <td width="51%" class="left_txt">
                    <img src="${stx}/images/icon-mail2.gif" width="16" height="11">&nbsp;客户服务邮箱：345426873@qq.com
                </td>
                <td height="31">&nbsp;</td>
                <td height="31" align="center"><span class="left_txt" id=clock></span></td>
            </tr>
        </table>
    </td>
    <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
</tr>
<tr>
    <td valign="bottom" background="${stx}/images/mail_leftbg.gif">
        <img src="${stx}/images/buttom_left2.gif" width="17" height="17"/>
    </td>
    <td height="2" background="${stx}/images/buttom_bgs.gif">
    </td>
    <td valign="bottom" background="${stx}/images/mail_rightbg.gif">
        <img src="${stx}/images/buttom_right2.gif" width="16" height="17"/>
    </td>
</tr>
<script src="${stx}/js/Clock.js"></script>
<SCRIPT type=text/javascript>
    var clock = new Clock();
    clock.display(document.getElementById("clock"));
</SCRIPT>