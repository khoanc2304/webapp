
<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 17/02/2025
  Time: 11:16 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<!-- Kiểm tra người dùng đã đăng nhập chưa -->
<c:if test="${empty sessionScope.loggedInUser}">
  <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>


<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Create User</title>

  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<!-- Gọi sidebar -->
<jsp:include page="../../common/sidebar.jsp" />
<!-- Gọi toast -->
<jsp:include page="../../common/toast.jsp" />

<div class="main-content" style="margin-top: 4rem">
  <form action="${pageContext.request.contextPath}/users?action=createUser" method="post">
    <h2 class="mb-4">Tạo Người Dùng Mới</h2>
    <div class="mb-3">
      <label for="username" class="form-label">Tên tài khoản:</label>
      <input type="text" class="form-control" id="username" name="username" value="${param.username}" required>
    </div>

    <div class="mb-3">
      <label for="password" class="form-label">Mật khẩu:</label>
      <input type="password" class="form-control" id="password" name="password" value="${param.password}" required>
    </div>

    <div class="mb-3">
      <label for="fullName" class="form-label">Họ và tên:</label>
      <input type="text" class="form-control" id="fullName" name="fullName" value="${param.fullName}" required>
    </div>

    <div class="mb-3">
      <label for="email" class="form-label">Email:</label>
      <input type="email" class="form-control" id="email" name="email" value="${param.email}" required>
    </div>

    <div class="mb-3">
      <label for="phone" class="form-label">Số điện thoại:</label>
      <input type="text" class="form-control" id="phone" name="phone" value="${param.phone}" required>
    </div>

    <div class="mb-3">
      <label for="userRole" class="form-label">Vai trò:</label>
      <select class="form-select" id="userRole" name="userRole" required>
        <option value="" disabled selected>-- Chọn Role --</option>
        <option value="customer" ${'customer' == param.userRole ? 'selected' : ''}>Customer</option>
        <option value="employee" ${'employee' == param.userRole ? 'selected' : ''}>Employee</option>
        <option value="manager" ${'manager' == param.userRole ? 'selected' : ''}>Manager</option>
        <option value="admin" ${'admin' == param.userRole ? 'selected' : ''}>Admin</option>
      </select>
    </div>
    <div class="d-flex justify-content-between">
      <button type="submit" class="btn btn-primary">Tạo Người Dùng</button>
      <a href="${pageContext.request.contextPath}/users?action=listUsers" class="btn btn-secondary">
        Quay lại
      </a>
    </div>
  </form>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
