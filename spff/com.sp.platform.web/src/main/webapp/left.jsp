<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>增值业务管理系统</title>
    <link href="${stx}/css/skin.css" rel="stylesheet" type="text/css" />
    <link href="${stx}/css/left.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="100%" height="280" border="0" cellpadding="0" cellspacing="0" bgcolor="#EEF2FB">
    <tr>
        <td width="182" valign="top">
            <div id="container">
                <s:iterator value="list" id="menu">
                    <h1 class="type"><a href="javascript:void(0)">${menu.name}</a></h1>
                    <div class="content">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td><img src="${stx}/images/menu_topline.gif" width="182" height="5"/></td>
                            </tr>
                        </table>
                        <ul class="MM">
                            <s:iterator value="#menu.children" id="subMenu">
                                <li><a href="${subMenu.url}" target="main">${subMenu.name}</a></li>
                            </s:iterator>
                        </ul>
                    </div>
                </s:iterator>

                <h1 class="type"><a href="javascript:void(0)">其它设置</a></h1>
                <div class="content">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td><img src="${stx}/images/menu_topline.gif" width="182" height="5"/></td>
                        </tr>
                    </table>
                    <ul class="MM">
                        <s:if test="%{#session.SESSION_SCS_USER==null}">
                            <li><a href="javascript:topLogout();">重新登陆</a></li>
                        </s:if>
                        <s:else>
                            <li><a href="${stx}/manage/cp!updatePasswd.action" target="main">修改密码</a></li>
                        </s:else>
                    </ul>
                </div>
            </div>
            <script src="${stx}/js/prototype.lite.js" type="text/javascript"></script>
            <script src="${stx}/js/moo.fx.js" type="text/javascript"></script>
            <script src="${stx}/js/moo.fx.pack.js" type="text/javascript"></script>

            <script type="text/javascript">
                var contents = document.getElementsByClassName('content');
                var toggles = document.getElementsByClassName('type');

                var myAccordion = new fx.Accordion(
                        toggles, contents, {opacity: true, duration: 400}
                );
                myAccordion.showThisHideOpen(contents[0]);
            </script>
        </td>
    </tr>
</table>
</body>
</html>

<script type="text/javascript">
    function topLogout(){
        top.location = "logout.action";
        return false;
    }
</script>