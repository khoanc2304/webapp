<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Brand List</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
        integrity="sha512-..." crossorigin="anonymous" referrerpolicy="no-referrer" />
  <!-- FontAwesome for icons -->
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <style>
    /* Thêm CSS để điều chỉnh kích thước ảnh */
    .brand-image {
      max-width: 100px;
      max-height: 100px;
      object-fit: contain;
    }
    .modal-body {
      display: flex;
      justify-content: center;
      align-items: center;
      text-align: center;
      min-height: 100px;
  </style>
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
        <h1 class="h2">Danh Sách Thương Hiệu</h1>
      </div>

      <!-- Button Thêm Thương Hiệu -->
      <a href="${pageContext.request.contextPath}/brand?action=add" class="btn btn-success btn-md mb-3">
        <i class="fas fa-plus"></i> Thêm Thương Hiệu
      </a>

      <!-- Search Form -->
      <form action="${pageContext.request.contextPath}/brand?action=list" method="get">
        <div class="input-group" style="max-width: 500px;">
          <input type="text" class="form-control" id="searchBrand" name="searchBrand"
                 placeholder="Nhập thông tin thương hiệu" value="${param.searchBrand}"
                 aria-label="Tìm thương hiệu">
          <button class="btn btn-primary" type="submit">
            <i class="fas fa-search"></i> Tìm kiếm
          </button>
          <!-- Nút quay lại nếu có tìm kiếm -->
          <c:if test="${not empty param.searchBrand}">
            <a href="${pageContext.request.contextPath}/brand?action=list"
               class="btn btn-secondary ms-2">
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </c:if>
        </div>
      </form>

      <!-- Brand Table -->
      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead>
          <tr>
            <th>STT</th>
            <th>Tên thương hiệu</th>
            <th>Quốc gia</th>
            <th>Ảnh</th>
            <th>Hành động</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="brand" items="${brands}" varStatus="loop">
            <tr>
              <td>${loop.count}</td>
              <td>${brand.brandName}</td>
              <td>${brand.country}</td>
              <td>
                <c:if test="${not empty brand.imageURL}">
                  <img src="${brand.imageURL}" alt="${brand.brandName} Image" class="brand-image">
                </c:if>
                <c:if test="${empty brand.imageURL}">
                  <span>No image available</span>
                </c:if>
              </td>
              <td>
<%--                <a href="${pageContext.request.contextPath}/brand?action=view&brandID=${brand.brandID}" class="btn btn-primary btn-sm me-1">--%>
<%--                  <i class="fas fa-eye"></i> Xem--%>
<%--                </a>--%>
                <a href="${pageContext.request.contextPath}/brand?action=edit&brandID=${brand.brandID}" class="btn btn-secondary btn-sm me-1">
                  <i class="fas fa-pen"></i> Sửa
                </a>
                <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModal" data-brandID="${brand.brandID}" data-brandName="${brand.brandName}">
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
        <h1 class="modal-title fs-5" id="deleteModalLabel">Xác Nhận Xóa Thương Hiệu</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body ">
        Bạn có chắc chắn muốn xóa thương hiệu <strong id="brandNameDisplay"></strong> không?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
        <form id="deleteBrandForm" action="${pageContext.request.contextPath}/brand?action=delete" method="post" style="display:inline;">
          <input type="hidden" name="brandID" id="brandIDToDelete">
          <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
        </form>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
  var deleteButtons = document.querySelectorAll('[data-bs-toggle="modal"]');
  deleteButtons.forEach(function(button) {
    button.addEventListener('click', function() {
      var brandID = button.getAttribute('data-brandID');
      var brandName = button.getAttribute('data-brandName');
      document.getElementById('brandIDToDelete').value = brandID;
      document.getElementById('brandNameDisplay').textContent = brandName;
    });
  });
</script>
</body>
</html>