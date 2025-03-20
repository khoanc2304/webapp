<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 13/03/2025
  Time: 9:54 SA
  To change this template use File | Settings | File Templates.
--%>
<%--%@ page import="model.ProductReview" %>--%>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>8386 Shop - Chi tiết sản phẩm</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">

    <style>
        /* Reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Arial', sans-serif;
        }

        body {
            background-color: #f8f9fa;
        }

        /* Header */
        .header {
            background: linear-gradient(90deg, #007bff, #00c4ff);
            padding: 1rem 2rem;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
            position: sticky;
            top: 0;
            z-index: 1030;
        }

        .header .logo img {
            height: 70px;
            transition: transform 0.3s ease;
            border-radius: 20px;
        }

        .header .logo img:hover {
            transform: scale(1.1);
        }

        .search-box {
            display: flex;
            align-items: center;
            background: #ffffff;
            padding: 10px 20px;
            border-radius: 25px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 400px;
            transition: all 0.4s ease;
        }

        .search-box input {
            border: none;
            outline: none;
            background: transparent;
            font-size: 16px;
            width: 100%;
            color: #333;
        }

        .search-box button {
            background: none;
            border: none;
            font-size: 18px;
            color: #2a5298;
            cursor: pointer;
            transition: color 0.3s;
        }

        .search-box button:hover {
            color: #ff6f00; /* Màu cam nổi bật khi hover */
        }

        .search-box:focus-within {
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
            width: 450px;
        }


        .nav-links .nav-link {
            color: #fff;
            font-weight: 500;
            padding: 0.75rem 1.5rem;
            border-radius: 25px;
            transition: all 0.3s ease;
        }

        .nav-links .nav-link:hover {
            background: rgba(255, 255, 255, 0.25);
            color: #fff;
        }

        .cart-badge {
            background: #ff2d55;
            font-size: 12px;
            width: 22px;
            height: 22px;
            line-height: 22px;
        }

        /* Bố cục chính */
        .main-content {
            padding-top: 100px;
        }

        .product-container {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
        }

        .product-title {
            font-size: 1.8rem;
            font-weight: bold;
        }

        .price {
            font-size: 1.8rem;
            font-weight: bold;
            color: #007bff;
        }

        /* Sidebar - Chính sách bán hàng */
        .sidebar-box {
            margin-top: 103px;
            background: white;
            padding: 15px;
            border-radius: 10px;
            box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
            position: absolute;
            top: 100px;
        }

        .policy-list {
            list-style: none;
            padding: 0;
        }

        .policy-list li {
            padding: 5px 0;
            font-size: 0.95rem;
        }

        .policy-list a {
            text-decoration: none;
            color: #007bff;
        }

        .policy-list a:hover {
            text-decoration: underline;
        }

        /* Phần quà tặng */
        .gift-box {
            margin-top: 20px;
            padding: 15px;
            border: 2px dashed #ff4d4d;
            border-radius: 10px;
            background: #fff3f3;
        }

        .gift-box h5 {
            color: #d9534f;
            font-weight: bold;
        }

        .gift-box ul {
            list-style: none;
            padding: 0;
        }

        .gift-box ul li {
            font-size: 0.95rem;
            padding: 5px 0;
        }

        /* Logo & tên công ty */
        .company-info {
            text-align: center;
            margin-bottom: 15px;
        }

        .company-info img {
            width: 60px;
            height: auto;
            margin-bottom: 5px;
        }

        .company-info h6 {
            font-weight: bold;
            color: #333;
        }
        .review-container {
            margin-top: 30px;
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
        }

        /* Tiêu đề đánh giá */
        .review-container h5 {
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 15px;
        }

        /* Tổng điểm trung bình */
        .rating-summary {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 1.5rem;
            font-weight: bold;
            color: #f5c518;
        }

        .rating-summary .average-rating {
            font-size: 2.5rem;
            font-weight: bold;
        }

        /* Biểu đồ đánh giá */
        .rating-bar-container {
            display: flex;
            align-items: center;
            gap: 10px;
            margin: 5px 0;
        }

        .rating-bar {
            flex: 1;
            height: 10px;
            background: #ddd;
            border-radius: 5px;
            position: relative;
        }

        .rating-bar .filled {
            height: 100%;
            background: #f5c518;
            border-radius: 5px;
            position: absolute;
            left: 0;
        }

        /* Hộp đánh giá cá nhân */
        .review-box {
            border-bottom: 1px solid #ddd;
            padding: 15px 0;
        }

        .review-box:last-child {
            border-bottom: none;
        }

        .review-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .review-header strong {
            font-size: 1rem;
            color: #333;
        }

        .review-stars {
            color: #f5c518;
        }

        .review-comment {
            font-size: 1rem;
            color: #555;
            margin: 5px 0;
        }

        .review-date {
            font-size: 0.85rem;
            color: #777;
        }


        .review-actions button {
            padding: 10px 15px;
            font-size: 1rem;
            border-radius: 5px;
            cursor: pointer;
        }
        .review-actions .btn {
            min-width: 70px;  /* Đảm bảo kích thước tối thiểu */
            padding: 8px 12px; /* Tạo khoảng cách đồng đều */
            font-size: 14px; /* Đảm bảo font đồng bộ */
            text-align: center; /* Canh giữa chữ */
            border-radius: 5px; /* Bo góc nhẹ để nhìn đẹp hơn */
        }

        .btn-warning {
            background-color: #ffc107;
            border: none;
            color: #212529;
        }

        .btn-danger {
            background-color: #dc3545;
            border: none;
            color: white;
        }

        .btn-warning:hover {
            background-color: #e0a800;
        }

        .btn-danger:hover {
            background-color: #c82333;
        }

        /* Đảm bảo căn chỉnh vị trí nút theo hàng ngang */
        .review-box {
            display: flex;
            flex-direction: column;
            gap: 5px; /* Khoảng cách giữa các nút */
        }

        .review-actions {
            display: flex;
            gap: 5px; /* Khoảng cách giữa nút Sửa và Xóa */
        }

        .btn-primary {
            background: #007bff;
            color: white;
            border: none;
        }

        .btn-outline-primary {
            background: white;
            color: #007bff;
            border: 1px solid #007bff;
        }




        footer {
            background: linear-gradient(135deg, #1e3a8a, #3b82f6);
            color: #f8f9fa;
            padding: 30px 0;
        }

        footer h6 {
            font-size: 1rem;
            margin-bottom: 15px;
        }

        footer ul {
            padding: 0;
        }

        footer ul li {
            list-style: none;
            margin-bottom: 8px;
        }

        footer ul li a {
            color: #bbb;
            text-decoration: none;
            transition: color 0.3s;
        }

        footer ul li a:hover {
            color: #007bff;
            text-decoration: underline;
        }

        footer .border-top {
            border-color: rgba(255, 255, 255, 0.2) !important;
        }
        .text-center img{
            height: 50px;
            border-radius: 50px;
        }
        .btn-cart {
            background: #007bff;
            padding: 0.4rem 1rem;
            font-size: 0.9rem;
            border-radius: 20px;
        }
        .btn-cart:hover{
            background: #1e3a8a;
            color:#212529 ;
        }
        .icon-circle {
            position: fixed;
            bottom: 80px; /* Điều chỉnh vị trí */
            right: 20px; /* Điều chỉnh vị trí */
            background-color: #1877F2; /* Màu xanh Facebook */
            color: white;
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            font-size: 24px;
            cursor: pointer;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
            transition: transform 0.2s, background-color 0.3s;
            text-decoration: none;
        }

        .icon-circle i {
            color: white;
        }

        .icon-circle:hover {
            transform: scale(1.1);
            background-color: #166FE5; /* Màu hover đậm hơn */
        }
        /* Hộp chat */
        .chat-popup {
            display: none;
            position: fixed;
            bottom: 80px;
            right: 20px;
            width: 320px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
            border: 1px solid #ddd;
        }

        /* Header chat */
        .chat-header {
            background: #007bff;
            color: white;
            padding: 10px;
            border-radius: 10px 10px 0 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .chat-header .close-btn {
            background: none;
            border: none;
            color: white;
            font-size: 18px;
            cursor: pointer;
        }

        /* Nội dung chat */
        .chat-body {
            padding: 10px;
            height: 300px; /* Tăng chiều cao */
            overflow-y: auto;
        }

        /* Tin nhắn từ người dùng (bên phải) */
        .user-message {
            background: #007bff;
            color: white;
            padding: 8px 12px;
            border-radius: 10px;
            max-width: 75%;
            align-self: flex-end;
            text-align: right;
        }

        /* Tin nhắn từ chatbot (bên trái) */
        .bot-message {
            background: #f1f1f1;
            color: black;
            padding: 8px 12px;
            border-radius: 10px;
            max-width: 75%;
            align-self: flex-start;
            text-align: left;
        }

        /* Bố cục tin nhắn */
        .messages {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }
        /* Footer chat */
        .chat-footer {
            display: flex;
            border-top: 1px solid #ddd;
            padding: 5px;
        }

        .chat-footer input {
            flex: 1;
            padding: 8px;
            border: none;
            outline: none;
        }

        .chat-footer button {
            background: #007bff;
            color: white;
            border: none;
            padding: 8px;
            cursor: pointer;
        }

        /* Nút mở chat */
        .chat-icon {
            position: fixed;
            bottom: 20px;
            right: 20px;
            background-color: #007bff;
            color: white;
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            font-size: 24px;
            cursor: pointer;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
            transition: transform 0.2s;
        }

        .chat-icon:hover {
            transform: scale(1.1);
        }
    </style>
</head>
<body>

<!-- Header -->
<!-- Header -->
<header class="header py-3">
    <div class="container d-flex align-items-center justify-content-between">
        <a href="${pageContext.request.contextPath}/product" class="logo">
            <img src="https://scontent.fdad3-5.fna.fbcdn.net/v/t1.15752-9/480148661_9011683048954377_8966679042114836256_n.jpg?stp=dst-jpg_s480x480_tt6&_nc_cat=109&ccb=1-7&_nc_sid=0024fc&_nc_ohc=59i_wTGvZt4Q7kNvgG1OR4k&_nc_oc=Adi6BVy88iPaGJmoV-DO0OWECBCkY2Ty_kHigT43IS5jeouOT48j6PAGtKjHEXj1ChwxMaUAoPUWa54FCMJEmz4z&_nc_ad=z-m&_nc_cid=0&_nc_zt=23&_nc_ht=scontent.fdad3-5.fna&oh=03_Q7cD1gF0WgRyL1OUp36J4UCDBM0YKOuIuydm1fDlCT8VluNHYw&oe=67E81504" alt="8386 Shop Logo">
        </a>
        <div class="search-box">
            <form action="search" method="GET" style="display: flex; width: 100%;">
                <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm..." value="${param.keyword}">
                <button type="submit"><i class="fas fa-search"></i></button>
            </form>
        </div>
        <nav class="nav-links d-flex align-items-center gap-3">
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    <div class="d-flex align-items-center gap-2">
                        <img src="${sessionScope.loggedInUser.avatar}" alt="Avatar" class="rounded-circle" style="width: 40px; height: 40px;">
                        <span class="text-white fw-bold">${sessionScope.loggedInUser.fullName}</span>
                        <a href="logout" class="nav-link text-white"><i class="fas fa-sign-out-alt me-1"></i> Đăng xuất</a>
                    </div>
                </c:when>
                <c:when test="${not empty sessionScope.googleName}">
                    <div class="d-flex align-items-center gap-2">
                        <img src="${sessionScope.googlePicture}" alt="Avatar" class="rounded-circle" style="width: 40px; height: 40px;">
                        <span class="text-white fw-bold">${sessionScope.googleName}</span>
                        <a href="logout" class="nav-link text-white"><i class="fas fa-sign-out-alt me-1"></i> Đăng xuất</a>
                    </div>
                </c:when>
                <c:when test="${not empty sessionScope.facebookName}">
                    <div class="d-flex align-items-center gap-2">
                        <img src="${not empty sessionScope.facebookPicture ? sessionScope.facebookPicture : 'https://via.placeholder.com/40'}" alt="Avatar" class="rounded-circle" style="width: 40px; height: 40px;">
                        <span class="text-white fw-bold">${sessionScope.facebookName}</span>
                        <a href="logout" class="nav-link text-white"><i class="fas fa-sign-out-alt me-1"></i> Đăng xuất</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="loginPage.jsp" class="nav-link"><i class="fas fa-user me-1"></i> Đăng Nhập</a>
                </c:otherwise>
            </c:choose>
            <a href="#" class="nav-link"><i class="fas fa-bell me-1"></i> Thông báo</a>
            <a href="cart" class="nav-link position-relative">
                <i class="fas fa-shopping-cart me-1"></i> Giỏ hàng
            </a>
            <a href="#" class="nav-link"><i class="fas fa-headset me-1"></i> Hỗ trợ</a>
        </nav>
    </div>
</header>



<!-- Main Container -->
<div class="container main-content">
    <div class="row">
        <!-- Chi tiết sản phẩm -->
        <div class="col-lg-8">
            <div class="product-container">
                <div class="row">
                    <div class="col-md-5">
                        <img src="${product.imageURL}" class="img-fluid border rounded" alt="${product.name}">
                    </div>

                    <div class="col-md-7">
                        <h2 class="product-title">${product.name}</h2>
                        <p class="price">
                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="" groupingUsed="true" /> VNĐ
                        </p>
                        <p class="product-info">${product.description}</p>

                        <div class="mt-4">
                            <a href="cart?action=add&productID=${product.productID}" class="btn btn-primary me-2">MUA NGAY</a>
                            <a href="<c:url value='cart'>
                <c:param name='action' value='addToCart'/>
                <c:param name='productID' value='${product.productID}'/>
                <c:param name='name' value='${fn:escapeXml(product.name)}'/>
                <c:param name='price' value='${product.price}'/>
                <c:param name='imageURL' value='${fn:escapeXml(product.imageURL)}'/>
            </c:url>"
                               class="btn btn-outline-primary" onclick="addToCartSuccess(event)">
                                Thêm vào giỏ hàng
                            </a>
                            <script>
                                function addToCartSuccess(event) {
                                    event.preventDefault();
                                    var url = event.target.closest('a').href;
                                    fetch(url, { method: 'GET' })
                                        .then(response => response.text())
                                        .then(data => {
                                            alert("Thêm vào giỏ hàng thành công!");
                                        })
                                        .catch(error => {
                                            alert("Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng.");
                                        });
                                }

                                if (window.location.hash === "#_=") {
                                    window.history.replaceState({}, document.title, window.location.pathname);
                                }
                            </script>
                        </div>
                    </div>
                </div>

                <!-- Phần quà tặng -->
                <div class="gift-box">
                    <h5>🎁 Bạn sẽ nhận được:</h5>
                    <ul>
                        <li>🖱️ Chuột HP Z3700 Wireless</li>
                        <li>🎧 Tai nghe HyperX Cloud Stinger Core II</li>
                        <li>🎒 Túi đựng laptop Targus 15.6"</li>
                        <li>🎟️ Giảm 200.000đ khi mua phần mềm M365 Personal</li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Chính sách bán hàng -->
        <div class="col-lg-4">
            <div class="sidebar-box">
                <div class="company-info">
                    <img src="https://scontent.fdad3-5.fna.fbcdn.net/v/t1.15752-9/480148661_9011683048954377_8966679042114836256_n.jpg?stp=dst-jpg_s480x480_tt6&_nc_cat=109&ccb=1-7&_nc_sid=0024fc&_nc_ohc=59i_wTGvZt4Q7kNvgFf-FDS&_nc_oc=Adh8p3x7XEI_SzFP-8Ykz7Ra_zO3wWy4G3sBIX-_6-Senj6wHCeIdlpjtL6sr8JsUiF1PjyRFW0s3n1AE3FD9yHE&_nc_ad=z-m&_nc_cid=0&_nc_zt=23&_nc_ht=scontent.fdad3-5.fna&oh=03_Q7cD1gGVnz_C_WVmf68RcLgZtv4XyTIzlRVIxRtQ5sA7OALVLw&oe=67E8F604" alt="8386 SHOP">
                    <h6>CÔNG TY CỔ PHẦN THƯƠNG MẠI DỊCH VỤ 8386 SHOP ✅</h6>
                </div>
                <h6 class="fw-bold">Chính sách bán hàng</h6>
                <ul class="policy-list">
                    <li>🚚 Miễn phí giao hàng từ 5 triệu <a href="#">Xem chi tiết</a></li>
                    <li>✔️ Cam kết hàng chính hãng 100%</li>
                    <li>🔄 Đổi trả trong vòng 10 ngày <a href="#">Xem chi tiết</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>



<div class="container mt-4">
    <div class="row">
        <div class="col-lg-8">
            <div class="review-container">
                <h5>Đánh giá sản phẩm</h5>

                <!-- Form chỉ hiển thị nếu người dùng chưa từng đánh giá -->
                <c:if test="${not empty sessionScope.userId or not empty sessionScope.googleEmail or not empty sessionScope.facebookEmail}">
                    <form action="product?action=add-review" method="post" class="mb-3">
                        <input type="hidden" name="productID" value="${product.productID}">
                        <label for="rating">Đánh giá:</label>
                        <select name="rating" id="rating" class="form-select w-auto d-inline-block">
                            <option value="5">5 ★</option>
                            <option value="4">4 ★</option>
                            <option value="3">3 ★</option>
                            <option value="2">2 ★</option>
                            <option value="1">1 ★</option>
                        </select>
                        <textarea name="comment" class="form-control mt-2" placeholder="Nhập đánh giá của bạn..." required></textarea>
                        <button type="submit" class="btn btn-primary mt-2">Gửi đánh giá</button>
                    </form>
                </c:if>


                <!-- Danh sách đánh giá -->
                <c:choose>
                    <c:when test="${not empty reviews}">
                        <c:forEach var="review" items="${reviews}">
                            <div class="review-box" id="reviewBox-${review.reviewID}">
                                <div class="review-header">
                                    <strong>Người dùng #${review.customerID}</strong>
                                    <span class="review-stars">
                <c:forEach begin="1" end="${review.rating}">&#9733;</c:forEach>
                <c:forEach begin="${review.rating + 1}" end="5">&#9734;</c:forEach>
            </span>
                                </div>

                                <!-- Hiển thị nội dung đánh giá -->
                                <p class="review-comment" id="commentText-${review.reviewID}">${review.comment}</p>

                                <!-- Form sửa đánh giá (ẩn mặc định) -->
                                <form id="editForm-${review.reviewID}" action="edit-review" method="post" class="d-none">
                                    <input type="hidden" name="reviewID" value="${review.reviewID}">
                                    <input type="hidden" name="productID" value="${product.productID}">
                                    <textarea name="newComment" class="form-control">${review.comment}</textarea>
                                    <button type="submit" class="btn btn-sm btn-success mt-2" onclick="saveEdit(${review.reviewID})">Lưu</button>
                                    <button type="button" class="btn btn-sm btn-secondary mt-2" onclick="cancelEdit(${review.reviewID})">Hủy</button>
                                </form>

                                <span class="review-date">
            <fmt:formatDate value="${review.reviewDate}" pattern="dd/MM/yyyy HH:mm" />
        </span>
                                <!-- Nút sửa/xóa -->
                                <div class="review-actions">
                                    <c:if test="${sessionScope.userId == review.customerID || sessionScope.userRole == 'admin'}">
                                        <form action="product?action=delete-review" method="post" class="d-inline" onsubmit="return confirmDelete();">
                                            <input type="hidden" name="reviewID" value="${review.reviewID}">
                                            <input type="hidden" name="productID" value="${product.productID}">
                                            <button type="submit" class="btn btn-sm btn-danger">Xóa</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>

                        <script>
                            function editReview(reviewID) {
                                // Ẩn đoạn văn bản cũ
                                document.getElementById("commentText-" + reviewID).style.display = "none";
                                // Hiện ô sửa đánh giá
                                document.getElementById("editForm-" + reviewID).classList.remove("d-none");
                            }

                            function cancelEdit(reviewID) {
                                // Hiện lại đoạn văn bản cũ
                                document.getElementById("commentText-" + reviewID).style.display = "block";
                                // Ẩn ô sửa đánh giá
                                document.getElementById("editForm-" + reviewID).classList.add("d-none");
                            }

                            function saveEdit(reviewID) {
                                // Lấy giá trị từ textarea
                                var newComment = document.querySelector("#editForm-" + reviewID + " textarea").value;

                                // Cập nhật nội dung hiển thị
                                document.getElementById("commentText-" + reviewID).innerText = newComment;

                                // Hiện lại đoạn văn bản cũ
                                document.getElementById("commentText-" + reviewID).style.display = "block";
                                // Ẩn ô sửa đánh giá
                                document.getElementById("editForm-" + reviewID).classList.add("d-none");
                            }

                            function confirmDelete() {
                                return confirm("Bạn có chắc muốn xóa đánh giá này?");
                            }
                        </script>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">Chưa có đánh giá nào.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<!-- Nút mở chat -->
<div id="chat-button" class="chat-icon">
    <i class="bi bi-chat-fill"></i>
</div>
<a  href="https://www.facebook.com/v.tafii" class="icon-circle">
    <i class="bi bi-facebook"></i>
</a>
<!-- Hộp chat -->
<div id="chatbox" class="chat-popup">
    <div class="chat-header">
        <span>Chatbot Hỗ Trợ Mua Hàng</span>
        <button id="close-chat" class="close-btn">&times;</button>
    </div>
    <div class="chat-body">
        <div class="messages" id="response"></div>
    </div>
    <div class="chat-footer">
        <input type="text" id="userInput" placeholder="Nhập tin nhắn...">
        <button id="send-chat">Gửi</button>
    </div>
</div>





</body>
<!-- Footer -->
<!-- Footer -->
<footer class="bg-dark text-light mt-5 pt-4 pb-2">
    <div class="container">
        <div class="row">
            <!-- Hỗ trợ khách hàng -->
            <div class="col-md-3">
                <h6 class="fw-bold text-uppercase">Hỗ trợ Khách hàng</h6>
                <ul class="list-unstyled">
                    <li><a href="#" class="text-light">Thẻ ưu đãi</a></li>
                    <li><a href="#" class="text-light">Hướng dẫn mua online</a></li>
                    <li><a href="#" class="text-light">Ưu đãi doanh nghiệp</a></li>
                    <li><a href="#" class="text-light">Chính sách trả góp</a></li>
                    <li><a href="#" class="text-light">Dịch vụ sửa chữa</a></li>
                </ul>
            </div>

            <!-- Chính sách mua hàng -->
            <div class="col-md-3">
                <h6 class="fw-bold text-uppercase">Chính sách mua hàng</h6>
                <ul class="list-unstyled">
                    <li><a href="#" class="text-light">Điều kiện giao dịch</a></li>
                    <li><a href="#" class="text-light">Chính sách bảo hành</a></li>
                    <li><a href="#" class="text-light">Chính sách đổi trả</a></li>
                    <li><a href="#" class="text-light">Chính sách bảo mật</a></li>
                    <li><a href="#" class="text-light">Quy định đặt cọc</a></li>
                </ul>
            </div>

            <!-- Thông tin về cửa hàng -->
            <div class="col-md-3">
                <h6 class="fw-bold text-uppercase">Thông tin 8386 Shop</h6>
                <ul class="list-unstyled">
                    <li><a href="#" class="text-light">Giới thiệu 8386 Shop</a></li>
                    <li><a href="#" class="text-light">Hệ thống cửa hàng</a></li>
                    <li><a href="#" class="text-light">Trung tâm bảo hành</a></li>
                    <li><a href="#" class="text-light">Hỏi đáp</a></li>
                    <li><a href="#" class="text-light">Tuyển dụng</a></li>
                </ul>
            </div>

            <!-- Liên hệ -->
            <div class="col-md-3">
                <h6 class="fw-bold text-uppercase">Liên hệ</h6>
                <ul class="list-unstyled">
                    <li>Email: <a href="mailto:cskh@8386shop.vn" class="text-light">cskh@8386shop.vn</a></li>
                    <li>Hotline: <a href="tel:18006867" class="text-light">18006867</a></li>
                    <li>Facebook: <a href="#" class="text-light">8386 Shop</a></li>
                    <li>Zalo: <a href="#" class="text-light">OA 8386 Shop</a></li>
                </ul>
            </div>
        </div>

        <!-- Phương thức thanh toán -->
        <div class="text-center mt-4">
            <h6 class="fw-bold text-uppercase">Phương thức thanh toán</h6>
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqTUF7qFMSIVVx1kJ7OIvLlIcX_g9E36llUg&s" alt="Phương thức thanh toán" class="img-fluid" style="max-width: 300px;">
        </div>

        <!-- Bản quyền -->
        <div class="text-center mt-3 border-top pt-3">
            <p class="mb-0 text-light">&copy; 1997 - 2024 CÔNG TY CỔ PHẦN THƯƠNG MẠI - DỊCH VỤ 8386 SHOP. All Rights Reserved.</p>
        </div>
    </div>
</footer>
<script>
    document.getElementById("chat-button").addEventListener("click", function () {
        document.getElementById("chatbox").style.display = "block";
    });

    document.getElementById("close-chat").addEventListener("click", function () {
        document.getElementById("chatbox").style.display = "none";
    });

    document.getElementById("send-chat").addEventListener("click", function () {
        let input = document.getElementById("chat-input");
        if (input.value.trim() !== "") {
            let messages = document.querySelector(".messages");
            let newMessage = document.createElement("div");
            newMessage.textContent = input.value;
            messages.appendChild(newMessage);
            input.value = "";
        }
    });
    document.getElementById("chat-button").addEventListener("click", function () {
        document.getElementById("chatbox").style.display = "block";
    });

    document.getElementById("close-chat").addEventListener("click", function () {
        document.getElementById("chatbox").style.display = "none";
    });

    document.getElementById("send-chat").addEventListener("click", function () {
        sendMessage();
    });

    async function sendMessage() {
        const inputField = document.getElementById('userInput');
        const input = inputField.value.trim();
        const responseDiv = document.getElementById('response');

        if (!input) {
            return;
        }

        // Hiển thị tin nhắn người dùng (bên phải)
        const userMessage = document.createElement('div');
        userMessage.classList.add('user-message');
        userMessage.textContent = input;
        responseDiv.appendChild(userMessage);

        // Xóa nội dung ô nhập sau khi gửi
        inputField.value = '';

        // Hiển thị trạng thái "Đang trả lời..."
        const loadingMessage = document.createElement('div');
        loadingMessage.classList.add('bot-message');
        loadingMessage.textContent = "Đang trả lời...";
        responseDiv.appendChild(loadingMessage);

        try {
            const response = await fetch("https://openrouter.ai/api/v1/chat/completions", {
                method: "POST",
                headers: {
                    "Authorization": "Bearer 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",
                    "HTTP-Referer": "http://localhost:8080/",
                    "X-Title": "HelloServlet",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    "model": "deepseek/deepseek-r1:free",
                    "messages": [{"role": "user", "content": input}]
                })
            });

            const data = await response.json();
            console.log("API Response:", data); // Debug response

            // Xóa tin nhắn "Đang trả lời..."
            responseDiv.removeChild(loadingMessage);

            // Thêm tin nhắn phản hồi từ chatbot (bên trái)
            const botMessage = document.createElement('div');
            botMessage.classList.add('bot-message');
            botMessage.textContent = data.choices?.[0]?.message?.content || 'Không có phản hồi.';
            responseDiv.appendChild(botMessage);

        } catch (error) {
            console.error("Fetch error:", error);
            responseDiv.removeChild(loadingMessage);
            const errorMessage = document.createElement('div');
            errorMessage.classList.add('bot-message');
            errorMessage.textContent = 'Lỗi: ' + error.message;
            responseDiv.appendChild(errorMessage);
        }

        // Cuộn xuống tin nhắn mới nhất
        responseDiv.scrollTop = responseDiv.scrollHeight;
    }
</script>
</html>