<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 13/03/2025
  Time: 9:57 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" %>

<c:if test="${empty sessionScope.loggedInUser}">
  <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Danh sách sản phẩm</title>
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
</head>
<body>
<!-- Gọi sidebar -->
<jsp:include page="../../common/sidebar.jsp" />
<!-- Gọi toast -->
<jsp:include page="../../common/toast.jsp" />

<div class="main-container" style="margin-top: 4rem">
  <div class="row">
    <main class="fade-in" id="page-title">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Danh Sách Sản Phẩm</h1>
      </div>

      <!-- Button Thêm Sản Phẩm -->
      <a href="${pageContext.request.contextPath}/product?action=createProduct" class="btn btn-success btn-md mb-3">
        <i class="fas fa-plus"></i> Thêm Sản Phẩm
      </a>

      <!-- Search Form -->
      <form action="${pageContext.request.contextPath}/product?action=listProducts" method="get">
        <div class="input-group" style="max-width: 500px;">
          <input type="text" class="form-control" id="searchProduct" name="searchProduct"
                 placeholder="Nhập thông tin sản phẩm" value="${param.searchProduct}"
                 aria-label="Tìm sản phẩm">
          <button class="btn btn-primary" type="submit">
            <i class="fas fa-search"></i> Tìm kiếm
          </button>
          <!-- Nút quay lại nếu có tìm kiếm -->
          <c:if test="${not empty param.searchProduct}">
            <a href="${pageContext.request.contextPath}/product?action=listProducts"
               class="btn btn-secondary ms-2">
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </c:if>
        </div>
      </form>

      <!-- Product Table -->
      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead>
          <tr>
            <th>STT</th>
            <th>Tên sản phẩm</th>
            <th>Giá</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="product" items="${productList}" varStatus="loop">
            <tr>
              <td>${loop.count}</td> <!-- Thay productID bằng STT giống user -->
              <td>${product.name}</td>
              <td>
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="VNĐ" groupingUsed="true"/>
              </td>
              <td>
                <span class="badge rounded-pill
                  <c:choose>
                    <c:when test="${product.productStatus eq 'available'}">bg-success</c:when>
                    <c:otherwise>bg-danger</c:otherwise>
                  </c:choose>
                  fs-6 px-2 py-1">
                    ${product.productStatus}
                </span>
              </td>
              <td>
                <a href="${pageContext.request.contextPath}/product?action=viewProduct&productID=${product.productID}" class="btn btn-primary btn-sm me-1">
                  <i class="fas fa-eye"></i> Xem
                </a>
                <a href="${pageContext.request.contextPath}/product?action=updateProduct&productId=${product.productID}" class="btn btn-secondary btn-sm me-1">
                  <i class="fas fa-pen"></i> Sửa
                </a>
                <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModal" data-productID="${product.productID}">
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

<!-- Delete Modal -->
<div class="modal fade" id="deleteModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="deleteModalLabel">Xác Nhận Xóa Sản Phẩm</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Bạn có chắc chắn muốn xóa sản phẩm <strong id="productNameDisplay"></strong> không?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
        <form id="deleteProductForm" action="${pageContext.request.contextPath}/product?action=deleteProduct" method="post" style="display:inline;">
          <input type="hidden" name="productID" id="productIDToDelete">
          <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script>
  var deleteButtons = document.querySelectorAll('[data-bs-toggle="modal"]');
  deleteButtons.forEach(function(button) {
    button.addEventListener('click', function() {
      var productID = button.getAttribute('data-productID');
      var productName = button.closest('tr').querySelector('td:nth-child(2)').textContent; // Lấy tên sản phẩm từ cột thứ 2
      document.getElementById('productIDToDelete').value = productID;
      document.getElementById('productNameDisplay').textContent = productName;
    });
  });
</script>
</body>
</html>