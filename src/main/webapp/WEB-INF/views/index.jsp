<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%
    final String path = request.getContextPath();
    final String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<head>
    <meta charset="utf-8"/>
    <base href="<%=basePath%>">
    <title>jsp页面</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
</head>
<body>
    您好！${principal}< br />
    您好！${principal.JwtUser}< br />
    您好！${principal.JwtUser.username}< br />
    <br> 权限测试页面
    <sec:authorize access="hasAuthority('edit')">
        拥有edit权限
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        你是ROLE_ADMIN角色
    </sec:authorize>
</body>
</html>