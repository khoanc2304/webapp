<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 17/02/2025
  Time: 11:17 SA
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
  <title>Chi tiết người dùng</title>

  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    .card-table th {
      background-color: #f8f9fa;
      font-weight: bold;
    }
    .card-table td {
      font-size: 1.1rem;
    }
    .img-avatar {
      max-width: 100px;
      border-radius: 50%;
    }
  </style>
</head>
<body>

<!-- Gọi sidebar -->
<jsp:include page="../../common/sidebar.jsp" />
<!-- Gọi toast -->
<jsp:include page="../../common/toast.jsp" />

<div id="main-content" style="margin-top: 4rem">
  <div class="container">
    <%-- Hiển thị chi tiết người dùng --%>
    <c:if test="${not empty user}">
      <div class="card">
        <div class="card-header">
          <h4>Chi tiết người dùng: ${user.username}</h4>
        </div>
        <div class="card-body">
          <table class="table table-bordered table-striped card-table">
            <tr>
              <th>Tên đầy đủ</th>
              <td>${user.fullName}</td>
            </tr>
            <tr>
              <th>Email</th>
              <td>${user.email}</td>
            </tr>
            <tr>
              <th>Số điện thoại</th>
              <td>${user.phone}</td>
            </tr>
            <tr>
              <th>Địa chỉ</th>
              <td>${user.address}</td>
            </tr>
            <tr>
              <th>Avatar</th>
              <td><img src="${user.avatar}" alt="Avatar" class="img-avatar" /></td>
            </tr>
            <tr>
              <th>Ngày tạo</th>
              <td>${user.formattedCreatedDate}</td>
            </tr>
            <tr>
              <th>Ngày cập nhật</th>
              <td>${user.formattedUpdateDate}</td>
            </tr>
            <tr>
              <th>Trạng thái</th>
              <td>${user.userStatus}</td>
            </tr>
            <tr>
              <th>Vai trò</th>
              <td>${user.userRole}</td>
            </tr>
          </table>
        </div>
        <div class="card-footer d-flex justify-content-between">
          <a href="${pageContext.request.contextPath}/users?action=listUsers" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Quay lại
          </a>
          <a href="${pageContext.request.contextPath}/users?action=updateUser&username=${user.username}" class="btn btn-warning">
            <i class="fas fa-edit"></i> Sửa người dùng
          </a>
        </div>
      </div>
    </c:if>
  </div>
</div>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>