<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 20/03/2025
  Time: 12:39 CH
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 20/03/2025
  Time: 12:39 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản lý đơn hàng</title>

  <!-- Google Fonts (Roboto) -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">

  <!-- Bootstrap 5.3 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />

  <style>
    body {
      font-family: 'Roboto', sans-serif;
      background-color: #f4f4f9;
    }
    .main-container {
      margin-top: 4rem; /* Đồng bộ với listProducts.jsp */
    }
    .fade-in {
      animation: fadeIn 0.5s ease-in; /* Thêm hiệu ứng fade-in */
    }
    @keyframes fadeIn {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }
    .table-responsive {
      margin-top: 20px;
    }
    .table th, .table td {
      vertical-align: middle;
      text-align: center; /* Căn giữa nội dung */
    }
    .table th {
      background-color: #f8f9fa; /* Màu nền cho tiêu đề bảng */
    }
    .table-hover tbody tr:hover {
      background-color: #e9ecef; /* Hiệu ứng hover */
    }
    .status-select {
      width: 150px;
    }
    .btn-sm {
      padding: 0.25rem 0.5rem; /* Đồng bộ kích thước nút */
      font-size: 0.875rem;
    }
    .btn + .btn {
      margin-left: 0.5rem; /* Khoảng cách giữa các nút */
    }
    .badge {
      font-size: 0.875rem; /* Đồng bộ kích thước badge */
    }
  </style>
</head>
<body>
<!-- Gọi sidebar -->
<jsp:include page="../../common/sidebar.jsp" />
<!-- Gọi toast -->
<jsp:include page="../../common/toast.jsp" />
<div class="main-container">
  <div class="row">
    <main class="fade-in" id="page-title">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Quản lý đơn hàng</h1>
      </div>

      <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
            ${errorMessage}
        </div>
      </c:if>

      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead class="table-dark">
          <tr>
            <th>Mã đơn hàng</th>
            <th>Tên khách hàng</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Địa chỉ</th>
            <th>Ngày đặt hàng</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="order" items="${orders}">
            <tr>
              <td>${order.orderID}</td>
              <td>${order.fullName}</td>
              <td>${order.email}</td>
              <td>${order.phone}</td>
              <td>${order.address}</td>
              <td>
                <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm:ss" />
              </td>
              <td>
                <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="" groupingUsed="true" maxFractionDigits="0" /> VNĐ
              </td>
              <td>
                <span class="badge rounded-pill
                  <c:choose>
                    <c:when test="${order.orderStatus == 'Pending'}">bg-warning</c:when>
                    <c:when test="${order.orderStatus == 'Processing'}">bg-primary</c:when>
                    <c:when test="${order.orderStatus == 'Completed'}">bg-success</c:when>
                    <c:when test="${order.orderStatus == 'Cancelled'}">bg-danger</c:when>
                  </c:choose>
                  fs-6 px-2 py-1">
                    ${order.orderStatus}
                </span>
              </td>
              <td>
                <form action="orders" method="post" style="display: inline;">
                  <input type="hidden" name="action" value="updateStatus">
                  <input type="hidden" name="orderId" value="${order.orderID}">
                  <select name="orderStatus" class="form-select status-select">
                    <option value="Pending" ${order.orderStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                    <option value="Processing" ${order.orderStatus == 'Processing' ? 'selected' : ''}>Processing</option>
                    <option value="Completed" ${order.orderStatus == 'Completed' ? 'selected' : ''}>Completed</option>
                    <option value="Cancelled" ${order.orderStatus == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                  </select>
                  <button style="margin-top: 5px" type="submit" class="btn btn-primary btn-sm">Cập nhật</button>
                </form>
<%--                <a href="${pageContext.request.contextPath}/admin/orderDetails?orderId=${order.orderID}" class="btn btn-info btn-sm">Xem chi tiết</a>--%>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>