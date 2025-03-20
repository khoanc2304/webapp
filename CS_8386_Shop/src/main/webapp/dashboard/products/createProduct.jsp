<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 13/03/2025
  Time: 9:56 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm sản phẩm mới</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h2 class="text-center">Thêm sản phẩm mới</h2>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger">${sessionScope.errorMessage}</div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <form action="products?action=createProduct" method="post">
        <div class="mb-3">
            <label for="brandId" class="form-label">Thương hiệu (Brand ID)</label>
            <input type="number" class="form-control" id="brandId" name="brandId" required>
        </div>

        <div class="mb-3">
            <label for="name" class="form-label">Tên sản phẩm</label>
            <input type="text" class="form-control" id="name" name="name" required>
        </div>

        <div class="mb-3">
            <label for="price" class="form-label">Giá sản phẩm</label>
            <input type="number" step="0.01" class="form-control" id="price" name="price" required>
        </div>

        <div class="mb-3">
            <label for="stock" class="form-label">Số lượng tồn kho</label>
            <input type="number" class="form-control" id="stock" name="stock" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Mô tả sản phẩm</label>
            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
        </div>

        <div class="mb-3">
            <label for="imageUrl" class="form-label">URL hình ảnh</label>
            <input type="text" class="form-control" id="imageUrl" name="imageUrl">
        </div>

        <button type="submit" class="btn btn-primary">Thêm sản phẩm</button>
        <a href="products?action=listProducts" class="btn btn-secondary">Quay lại</a>
    </form>
</div>
</body>
</html>
