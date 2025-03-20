<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 13/03/2025
  Time: 9:57 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cập nhật sản phẩm</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h2 class="text-center">Cập nhật sản phẩm</h2>

  <c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-danger">${sessionScope.errorMessage}</div>
    <c:remove var="errorMessage" scope="session"/>
  </c:if>

  <form action="products?action=updateProduct" method="post">
    <input type="hidden" name="productId" value="${product.productID}">

    <div class="mb-3">
      <label for="brandId" class="form-label">Thương hiệu (Brand ID)</label>
      <input type="number" class="form-control" id="brandId" name="brandId" value="${product.brandID}" required>
    </div>

    <div class="mb-3">
      <label for="name" class="form-label">Tên sản phẩm</label>
      <input type="text" class="form-control" id="name" name="name" value="${product.name}" required>
    </div>

    <div class="mb-3">
      <label for="price" class="form-label">Giá sản phẩm</label>
      <input type="number" step="0.01" class="form-control" id="price" name="price" value="${product.price}" required>
    </div>

    <div class="mb-3">
      <label for="stock" class="form-label">Số lượng tồn kho</label>
      <input type="number" class="form-control" id="stock" name="stock" value="${product.stock}" required>
    </div>
    <div class="mb-3">
      <label for="productStatus" class="form-label">Trạng thái sản phẩm</label>
      <select class="form-control" id="productStatus" name="productStatus">
        <option value="available" ${product.productStatus == 'available' ? 'selected' : ''}>Available</option>
        <option value="unavailable" ${product.productStatus == 'unavailable' ? 'selected' : ''}>Unavailable</option>
      </select>
    </div>
    <div class="mb-3">
      <label for="description" class="form-label">Mô tả sản phẩm</label>
      <textarea class="form-control" id="description" name="description" rows="3">${product.description}</textarea>
    </div>

    <div class="mb-3">
      <label for="imageUrl" class="form-label">URL hình ảnh</label>
      <input type="text" class="form-control" id="imageUrl" name="imageUrl" value="${product.imageURL}">
    </div>

    <button type="submit" class="btn btn-success">Cập nhật sản phẩm</button>
    <a href="products?action=listProducts" class="btn btn-secondary">Quay lại</a>
  </form>
  <script>
    document.querySelector("form").addEventListener("submit", function() {
      alert("Giá trị trạng thái đang được gửi: " + document.getElementById("productStatus").value);
    });
  </script>
</div>
</body>
</html>