
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
//
<html>
<head>
  <title>Update User</title>
</head>
<body>
<!-- Gọi sidebar -->
<jsp:include page="../../common/sidebar.jsp" />
<!-- Gọi toast -->
<jsp:include page="../../common/toast.jsp" />

<form action="${pageContext.request.contextPath}/users?action=updateUser" method="post" style="margin-top: 4rem">
  <div class="mb-3">
    <label for="username" class="form-label">Tên đăng nhập</label>
    <input type="text" id="username" name="username" class="form-control" value="${username != null ? username : user.username}" readonly>
  </div>

  <div class="mb-3">
    <label for="fullName" class="form-label">Họ và tên</label>
    <input type="text" id="fullName" name="fullName" class="form-control" value="${fullName != null ? fullName : user.fullName}">
  </div>

  <div class="mb-3">
    <label for="email" class="form-label">Email</label>
    <input type="email" id="email" name="email" class="form-control" value="${email != null ? email : user.email}">
  </div>

  <div class="mb-3">
    <label for="phone" class="form-label">Số điện thoại</label>
    <input type="text" id="phone" name="phone" class="form-control" vvalue="${phone != null ? phone : user.phone}">
  </div>

  <div class="mb-3">
    <label for="address" class="form-label">Địa chỉ</label>
    <input type="text" id="address" name="address" class="form-control" value="${address != null ? address : user.address}">
  </div>

  <div class="mb-3">
    <label for="avatar" class="form-label">Avatar (URL)</label>
    <input type="text" id="avatar" name="avatar" class="form-control" value="${avatar != null ? avatar : user.avatar}">
  </div>

  <div class="mb-3">
    <label for="userStatus" class="form-label">Trạng thái</label>
    <select id="userStatus" name="userStatus" class="form-select">
      <option value="active" ${userStatus != null && userStatus == 'active' ? 'selected' : ''}>Active</option>
      <option value="suspended" ${userStatus != null && userStatus == 'inactive' ? 'selected' : ''}>Suspended</option>
    </select>
  </div>

  <div class="mb-3">
    <label for="userRole" class="form-label">Vai trò</label>
    <select id="userRole" name="userRole" class="form-select">
      <option value="" disabled selected>-- Chọn Role --</option>
      <option value="admin" ${user.userRole == 'admin' ? 'selected' : ''}>Admin</option>
      <option value="manager" ${user.userRole == 'manager' ? 'selected' : ''}>Manager</option>
      <option value="employee" ${user.userRole == 'employee' ? 'selected' : ''}>Employee</option>
      <option value="customer" ${user.userRole == 'customer' ? 'selected' : ''}>Customer</option>
    </select>
  </div>

  <div class="mb-3">
    <label for="userCreatedDate" class="form-label">Ngày tạo tài khoản</label>
    <input type="text" id="userCreatedDate" name="userCreatedDate" class="form-control" value="${user.userCreatedDate}" readonly>
  </div>

  <div class="mb-3">
    <label for="userUpdatedDate" class="form-label">Ngày cập nhật tài khoản</label>
    <input type="text" id="userUpdatedDate" name="userUpdatedDate" class="form-control" value="${user.userUpdatedDate}" readonly>
  </div>

  <div class="d-flex justify-content-between">
    <button type="submit" class="btn btn-primary">Lưu Thay Đổi</button>
    <a href="${pageContext.request.contextPath}/users?action=listUsers" class="btn btn-secondary">
      Quay lại
    </a>
  </div>
</form>

</body>
</html>
