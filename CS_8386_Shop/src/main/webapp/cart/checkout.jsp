<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>8386 Shop - Thanh toán</title>

    <!-- Bootstrap 5.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">

    <style>
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
            color: #ff6f00;
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

        /* Checkout Section */
        .checkout-section {
            padding: 2rem 0;
            background-color: #f9f9f9;
        }

        .checkout-form {
            background: #fff;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .cart-summary {
            background: #fff;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .cart-item {
            display: flex;
            align-items: center;
            padding: 1rem 0;
            border-bottom: 1px solid #e0e0e0;
        }

        .cart-item img {
            width: 80px;
            height: auto;
            margin-right: 1rem;
        }

        .cart-item-details h5 {
            font-size: 1.1rem;
            font-weight: 500;
            margin-bottom: 0.3rem;
        }

        .cart-item-details .price {
            font-size: 1rem;
            font-weight: 600;
            color: #333;
        }

        .summary-details p {
            display: flex;
            justify-content: space-between;
            font-size: 1rem;
            margin-bottom: 0.5rem;
        }

        .summary-details .total {
            font-size: 1.5rem;
            font-weight: 700;
            color: #ff2d55;
        }

        .checkout-btn {
            background-color: #007bff;
            color: #fff;
            font-weight: 600;
            padding: 0.75rem 2rem;
            border-radius: 8px;
            text-transform: uppercase;
            width: 100%;
        }

        .checkout-btn:hover {
            background-color: #0056b3;
        }

        .error-message {
            color: red;
            font-size: 0.9rem;
            display: none;
        }
    </style>
</head>
<body>

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

<!-- Checkout Section -->
<section class="checkout-section">
    <div class="container">
        <h2 class="mb-4 fw-bold text-primary">Thông tin thanh toán</h2>
        <div class="row">
            <!-- Checkout Form -->
            <div class="col-md-8">
                <div class="checkout-form">
                    <h4>Thông tin khách hàng</h4>
                    <form id="checkoutForm" action="${pageContext.request.contextPath}/checkout" method="post" onsubmit="return validateForm()">
                    <!-- Họ tên -->
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Họ và tên *</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" required>
                        <div id="fullNameError" class="error-message">Vui lòng nhập họ tên.</div>
                    </div>

                    <!-- Số điện thoại -->
                    <div class="mb-3">
                        <label for="phone" class="form-label">Số điện thoại *</label>
                        <input type="tel" class="form-control" id="phone" name="phone" pattern="[0-9]{10}" required placeholder="Nhập số điện thoại 10 số">
                        <div id="phoneError" class="error-message">Vui lòng nhập số điện thoại hợp lệ (10 số).</div>
                    </div>

                    <!-- Địa chỉ -->
                    <div class="mb-3">
                        <label for="address" class="form-label">Địa chỉ *</label>
                        <input type="text" class="form-control" id="address" name="address" required placeholder="Nhập địa chỉ cụ thể (ví dụ: Số nhà, Phường, Quận, Thành phố)">
                        <div id="addressError" class="error-message">Vui lòng nhập địa chỉ.</div>
                    </div>

                    <!-- Email -->
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <c:choose>
                            <c:when test="${not empty sessionScope.googleEmail}">
                                <input type="email" class="form-control" id="email" name="email" value="${sessionScope.googleEmail}" placeholder="Nhập email (tùy chọn)">
                            </c:when>
                            <c:when test="${not empty sessionScope.facebookEmail}">
                                <input type="email" class="form-control" id="email" name="email" value="${sessionScope.facebookEmail}" placeholder="Nhập email (tùy chọn)">
                            </c:when>
                            <c:when test="${not empty sessionScope.loggedInUser}">
                                <input type="email" class="form-control" id="email" name="email" value="${sessionScope.loggedInUser.email}" placeholder="Nhập email (tùy chọn)">
                            </c:when>
                            <c:otherwise>
                                <input type="email" class="form-control" id="email" name="email" placeholder="Nhập email (tùy chọn)">
                            </c:otherwise>
                        </c:choose>
                        <div id="emailError" class="error-message">Vui lòng nhập email hợp lệ.</div>
                    </div>

                    <!-- Phương thức giao hàng -->
                    <div class="mb-3">
                        <label class="form-label">Phương thức giao hàng *</label>
                        <div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" id="deliveryStandard" name="deliveryMethod" value="standard" checked required>
                                <label class="form-check-label" for="deliveryStandard">Giao tiêu chuẩn - 25.000đ</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" id="deliveryFast" name="deliveryMethod" value="fast">
                                <label class="form-check-label" for="deliveryFast">Giao nhanh - 50.000đ</label>
                            </div>
                        </div>
                        <div id="deliveryError" class="error-message">Vui lòng chọn phương thức giao hàng.</div>
                    </div>

                    <!-- Ghi chú đơn hàng -->
                    <div class="mb-3">
                        <label for="note" class="form-label">Ghi chú đơn hàng</label>
                        <textarea class="form-control" id="note" name="note" rows="3" placeholder="Nhập ghi chú (nếu có)"></textarea>
                    </div>

                    <!-- Phương thức thanh toán -->
                    <div class="mb-3">
                        <label class="form-label">Phương thức thanh toán *</label>
                        <div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" id="paymentCash" name="paymentMethod" value="cash" checked required>
                                <label class="form-check-label" for="paymentCash">Thanh toán khi nhận hàng (COD)</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" id="paymentOnline" name="paymentMethod" value="online">
                                <label class="form-check-label" for="paymentOnline">Thanh toán online</label>
                            </div>
                        </div>
                        <div id="paymentError" class="error-message">Vui lòng chọn phương thức thanh toán.</div>
                    </div>

                    <button type="submit" class="btn checkout-btn">Xác nhận thanh toán</button>
                </form>
                </div>
            </div>

            <!-- Cart Summary -->
            <div class="col-md-4">
                <div class="cart-summary">
                    <h4>Tóm tắt đơn hàng</h4>
                    <c:if test="${empty sessionScope.cart.items}">
                        <p>Giỏ hàng của bạn hiện tại trống.</p>
                    </c:if>
                    <c:forEach var="item" items="${sessionScope.cart.items}">
                        <div class="cart-item">
                            <img src="${item.imageURL}" alt="${item.name}" class="img-fluid">
                            <div class="cart-item-details">
                                <h5>${item.name}</h5>
                                <p>Số lượng: ${item.cartQuantity}</p>
                                <p class="price"><fmt:formatNumber value="${item.price * item.cartQuantity}" type="currency" currencySymbol="" groupingUsed="true"  maxFractionDigits="0" /> VNĐ</p>
                            </div>
                        </div>
                    </c:forEach>
                    <div class="summary-details">
                        <p>Tổng tạm tính <span><fmt:formatNumber value="${sessionScope.cart.totalPrice}" type="currency" currencySymbol="" groupingUsed="true"  maxFractionDigits="0" /> VNĐ</span></p>
                        <p>Phí vận chuyển <span id="shippingFee">25.000 VNĐ</span></p>
                        <p class="total">Thành tiền <span id="totalAmount"><fmt:formatNumber value="${sessionScope.cart.totalPrice + 25000}" type="currency" currencySymbol="" groupingUsed="true"  maxFractionDigits="0" />VNĐ</span></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script>
    // Validate form
    function validateForm() {
        let isValid = true;
        console.log("isValid: ", isValid);
        return isValid;
        // Reset error messages
        document.querySelectorAll('.error-message').forEach(error => error.style.display = 'none');

        // Check full name
        const fullName = document.getElementById('fullName').value.trim();
        if (!fullName) {
            document.getElementById('fullNameError').style.display = 'block';
            isValid = false;
        }

        // Check phone
        const phone = document.getElementById('phone').value.trim();
        const phonePattern = /^[0-9]{10}$/;
        if (!phone || !phonePattern.test(phone)) {
            document.getElementById('phoneError').style.display = 'block';
            isValid = false;
        }

        // Check address
        const address = document.getElementById('address').value.trim();
        if (!address) {
            document.getElementById('addressError').style.display = 'block';
            isValid = false;
        }

        // Check email (optional but validate if entered)
        const email = document.getElementById('email').value.trim();
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email && !emailPattern.test(email)) {
            document.getElementById('emailError').style.display = 'block';
            isValid = false;
        }

        // Check delivery method
        const deliveryMethod = document.querySelector('input[name="deliveryMethod"]:checked');
        if (!deliveryMethod) {
            document.getElementById('deliveryError').style.display = 'block';
            isValid = false;
        } else {
            const shippingFee = deliveryMethod.value === 'standard' ? 25000 : 50000;
            document.getElementById('shippingFee').textContent = shippingFee.toLocaleString('vi-VN') + 'đ';
            const totalAmount = ${sessionScope.cart.totalPrice} + shippingFee;
            document.getElementById('totalAmount').textContent = totalAmount.toLocaleString('vi-VN') + 'đ';
        }

        // Check payment method
        const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
        if (!paymentMethod) {
            document.getElementById('paymentError').style.display = 'block';
            isValid = false;
        }

        return isValid;
    }

    // Update shipping fee and total when delivery method changes
    document.querySelectorAll('input[name="deliveryMethod"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const shippingFee = this.value === 'standard' ? 25000 : 50000;
            document.getElementById('shippingFee').textContent = shippingFee.toLocaleString('vi-VN') + 'đ';
            const totalAmount = ${sessionScope.cart.totalPrice} + shippingFee;
            document.getElementById('totalAmount').textContent = totalAmount.toLocaleString('vi-VN') + 'đ';
        });
    });
</script>
</body>
</html>