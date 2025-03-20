<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Brand Form</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<!-- Gọi sidebar -->
<jsp:include page="../../common/sidebar.jsp" />
<!-- Gọi toast -->
<jsp:include page="../../common/toast.jsp" />
<div class="container mt-5">
  <h2>${brand != null ? 'Edit Brand' : 'Add New Brand'}</h2>
  <form action="${pageContext.request.contextPath}/brand?action=${brand != null ? 'edit' : 'add'}" method="post">
    <c:if test="${brand != null}">
      <input type="hidden" name="brandID" value="${brand.brandID}">
    </c:if>
    <div class="mb-3">
      <label for="brandName" class="form-label">Brand Name</label>
      <input type="text" class="form-control" id="brandName" name="brandName" value="${brand.brandName}" required>
    </div>
    <div class="mb-3">
      <label for="country" class="form-label">Country</label>
      <input type="text" class="form-control" id="country" name="country" value="${brand.country}" required>
    </div>
    <div class="mb-3">
      <label for="imageURL" class="form-label">Image URL</label>
      <input type="text" class="form-control" id="imageURL" name="imageURL" value="${brand.imageURL}">
    </div>
    <button type="submit" class="btn btn-primary">${brand != null ? 'Update' : 'Save'}</button>
    <a href="${pageContext.request.contextPath}/brand?action=list" class="btn btn-secondary">Back</a>
  </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>