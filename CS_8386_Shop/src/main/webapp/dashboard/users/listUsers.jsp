
<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 17/02/2025
  Time: 11:02 SA
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
  <title>Danh sách người dùng</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
        integrity="sha512-..." crossorigin="anonymous" referrerpolicy="no-referrer" />
  <!-- FontAwesome for icons -->
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <style></style>
</head>
<body>
<!-- Gọi sidebar -->
<jsp:include page="../../common/sidebar.jsp" />
<!-- Gọi toast -->
<jsp:include page="../../common/toast.jsp" />


<div class="main-container" style="margin-top: 4rem">
  <div class="row">
    <!-- Main content -->
    <main class="fade-in" id="page-title">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Danh Sách Người Dùng</h1>
      </div>

      <!-- Button Thêm Người Dùng -->
      <a href="${pageContext.request.contextPath}/users?action=createUser" class="btn btn-success btn-md mb-3">
        <i class="fas fa-user-plus"></i> Thêm Người Dùng
      </a>

      <%-- Search --%>
      <form action="${pageContext.request.contextPath}/users?action=listUsers" method="get">
        <div class="input-group" style="max-width: 500px;">
          <input type="text" class="form-control" id="searchUser" name="searchUser"
                 placeholder="Nhập thông tin người dùng"
                 value="${param.searchUser}"
                 aria-label="Tìm người dùng">

          <button class="btn btn-primary" type="submit">
            <i class="fas fa-search"></i> Tìm kiếm
          </button>

          <!-- Nút quay lại nếu có tìm kiếm -->
          <c:if test="${not empty param.searchUser}">
            <a href="${pageContext.request.contextPath}/users?action=listUsers"
               class="btn btn-secondary ms-2">
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </c:if>
        </div>
      </form>

      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead>
          <tr>
            <th>STT</th>
            <th>Tài khoản</th>
            <th>Tên</th>
            <th>Role</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="user" items="${listUsers}" varStatus="loop">
            <tr>
              <td>${loop.count}</td>
              <td>${user.username}</td>
              <td>${user.fullName}</td>
              <td>
                      <span class="badge rounded-pill
                          <c:choose>
                              <c:when test="${user.userRole == 'admin'}">bg-danger</c:when>
                              <c:when test="${user.userRole == 'manager'}">bg-warning</c:when>
                              <c:when test="${user.userRole == 'employee'}">bg-primary</c:when>
                              <c:otherwise>bg-success</c:otherwise>
                          </c:choose>
                       fs-6 px-2 py-1">
                          ${user.userRole}
                      </span>
              </td>
              <td>
                    <span class="badge rounded-pill
                      <c:choose>
                        <c:when test="${user.userStatus == 'active'}">bg-success</c:when>
                        <c:when test="${user.userStatus == 'suspended'}">bg-danger</c:when>
                      </c:choose>
                      fs-6 px-2 py-1">
                        ${user.userStatus}
                    </span>
              </td>
              <td>
                <a href="/users?action=viewUser&username=${user.username}" class="btn btn-primary btn-sm me-1">
                  <i class="fas fa-eye"></i> Xem
                </a>

                <a href="${pageContext.request.contextPath}/users?action=updateUser&username=${user.username}" class="btn btn-secondary btn-sm me-1">
                  <i class="fas fa-pen"></i> Sửa
                </a>

                <!-- Nút Xóa, hiển thị Modal -->
                <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                        data-username="${user.username}">
                  <i class="fas fa-trash-alt"></i> Xóa
                </button>

              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</div>

<!-- Modal -->
<div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="staticBackdropLabel">Xác Nhận Xóa Người Dùng</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Bạn có chắc chắn muốn xóa người dùng <strong id="usernameDisplay"></strong>  không?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>

        <!-- Form xóa người dùng -->
        <form id="deleteUserForm" action="${pageContext.request.contextPath}/users?action=deleteUser" method="post" style="display:inline;">
          <input type="hidden" name="action" value="deleteUser">
          <input type="hidden" name="username" id="usernameToDelete">
          <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
        </form>
      </div>
    </div>
  </div>
</div>

<%--Js modal--%>
<script>
  var deleteButtons = document.querySelectorAll('[data-bs-toggle="modal"]');
  deleteButtons.forEach(function(button) {
    button.addEventListener('click', function() {
      var username = button.getAttribute('data-username');
      document.getElementById('usernameToDelete').value = username;
      document.getElementById('usernameDisplay').textContent = username;
      document.getElementById('usernameToDelete').value = username;
    });
  });
</script>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
