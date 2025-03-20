<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 13/03/2025
  Time: 10:01 SA
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
  <title>Chi tiết sản phẩm</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <div class="d-flex justify-content-between mb-3">
    <h1>Chi tiết sản phẩm: ${product.name}</h1>
    <a href="${pageContext.request.contextPath}/product?action=listProducts" class="btn btn-secondary">
      Quay lại
    </a>
  </div>

  <!-- Product Details -->
  <div class="row">
    <div class="col-md-6">
      <img src="${product.imageURL}" alt="Product Image" class="img-fluid" />
    </div>
    <div class="col-md-6">
      <table class="table table-bordered">
        <tr>
          <th>Tên sản phẩm</th>
          <td>${product.name}</td>
        </tr>
        <tr>
          <th>Giá</th>
          <td>
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫" groupingUsed="true"/>
          </td>
        </tr>
        <tr>
          <th>Tồn Kho</th>
          <td>
            ${product.stock}
          </td>
        </tr>
        <%--                <tr>--%>
        <%--                    <th>Trạng thái</th>--%>
        <%--                    <td>--%>
        <%--                        <span class="badge ${product.stock > 0 ? 'bg-success' : 'bg-danger'}">--%>
        <%--                            ${product.stock > 0 ? 'Available' : 'Out of Stock'}--%>
        <%--                        </span>--%>
        <%--                    </td>--%>
        <%--                </tr>--%>
        <tr>
          <th>Trạng thái</th>
          <td>
            <c:choose>
              <c:when test="${product.productStatus eq 'available'}">
                <span class="badge bg-success">Available</span>
              </c:when>
              <c:otherwise>
                <span class="badge bg-danger">Unavailable</span>
              </c:otherwise>
            </c:choose>
          </td>

        </tr>
        <tr>
          <th>Miêu tả</th>
          <td>${product.description}</td>
        </tr>
        <tr>
          <th>Ngày tạo</th>
          <td><fmt:formatDate value="${createdDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
        </tr>
        <tr>
          <th>Ngày cập nhật</th>
          <td><fmt:formatDate value="${updatedDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
        </tr>

        <tr>
          <th>Thương hiệu</th>
          <td>${product.brandName}</td>
        </tr>
      </table>
    </div>
  </div>

  <!-- Action Buttons -->
  <div class="d-flex justify-content-between">
    <a href="${pageContext.request.contextPath}/product?action=updateProduct&productID=${product.productID}" class="btn btn-warning">
      Sửa sản phẩm
    </a>
    <button class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal" data-productID="${product.productID}">
      Xóa sản phẩm
    </button>
  </div>
</div>

<!-- Delete Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="deleteModalLabel">Xác nhận xóa sản phẩm</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        Bạn có chắc chắn muốn xóa sản phẩm này không?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
        <form id="deleteProductForm" action="${pageContext.request.contextPath}/product?action=deleteProduct" method="post">
          <input type="hidden" name="productID" id="productIDToDelete">
          <button type="submit" class="btn btn-danger">Xóa</button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script>
  // Get product ID to delete
  const deleteButtons = document.querySelectorAll('[data-bs-toggle="modal"]');
  deleteButtons.forEach(button => {
    button.addEventListener('click', function() {
      const productID = button.getAttribute('data-productID');
      document.getElementById('productIDToDelete').value = productID;
    });
  });
</script>

</body>
</html>

