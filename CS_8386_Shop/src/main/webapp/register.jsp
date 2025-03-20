<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 17/02/2025
  Time: 4:17 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Đăng ký</title>
  <%--  Nhúng CSS  --%>
  <style>
    :root {
      --primary: #0d6efd;
    }

    *, *:after, *:before {
      box-sizing: border-box;
    }

    body {
      background: rgba(192, 208, 243, 0.8) !important;
      font-family: 'Roboto', sans-serif;
    }

    .panda {
      position: relative;
      width: 200px;
      margin: 50px auto;
    }

    .face {
      width: 200px;
      height: 200px;
      background: #fff;
      border-radius: 100%;
      margin: 50px auto;
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.15);
      z-index: 50;
      position: relative;
    }

    .ear {
      position: absolute;
      width: 80px;
      height: 80px;
      background: #000;
      z-index: 5;
      border: 10px solid #fff;
      left: -15px;
      top: -15px;
      border-radius: 100%;
    }

    .ear:after {
      content: '';
      position: absolute;
      width: 80px;
      height: 80px;
      background: #000;
      z-index: 5;
      border: 10px solid #fff;
      left: 125px;
      top: -15px;
      border-radius: 100%;
    }

    .eye-shade {
      background: #000;
      width: 50px;
      height: 80px;
      margin: 10px;
      position: absolute;
      top: 35px;
      left: 25px;
      transform: rotate(220deg);
      border-radius: 25px / 20px 30px 35px 40px;
    }

    .eye-shade.rgt {
      transform: rotate(140deg);
      left: 105px;
    }

    .eye-white {
      position: absolute;
      width: 30px;
      height: 30px;
      border-radius: 100%;
      background: #fff;
      z-index: 500;
      left: 40px;
      top: 80px;
      overflow: hidden;
    }

    .eye-white.rgt {
      right: 40px;
      left: auto;
    }

    .eye-ball {
      position: absolute;
      width: 0px;
      height: 0px;
      left: 20px;
      top: 20px;
      max-width: 10px;
      max-height: 10px;
      transition: all 0.1s;
    }

    .eye-ball:after {
      content: '';
      background: #000;
      position: absolute;
      border-radius: 100%;
      right: 0;
      bottom: 0px;
      width: 20px;
      height: 20px;
    }

    .nose {
      position: absolute;
      height: 20px;
      width: 35px;
      bottom: 40px;
      left: 0;
      right: 0;
      margin: auto;
      border-radius: 50px 20px / 30px 15px;
      transform: rotate(15deg);
      background: #000;
    }

    .body {
      background: #fff;
      position: absolute;
      top: 200px;
      left: -20px;
      border-radius: 100px 100px 100px 100px / 126px 126px 96px 96px;
      width: 250px;
      height: 282px;
      box-shadow: 0 5px 10px rgba(0, 0, 0, 0.3);
    }

    .hand {
      width: 40px;
      height: 30px;
      border-radius: 50px;
      box-shadow: 0 2px 3px rgba(0, 0, 0, 0.15);
      background: #000;
      margin: 5px;
      position: absolute;
      top: 70px;
      left: -25px;
    }

    .hand:after,
    .hand:before {
      content: '';
      position: absolute;
      width: 40px;
      height: 30px;
      border-radius: 50px;
      box-shadow: 0 2px 3px rgba(0, 0, 0, 0.15);
      background: #000;
      left: -5px;
      top: 11px;
    }

    .hand:before {
      top: 26px;
    }

    .hand.rgt {
      left: auto;
      right: -25px;
    }

    .hand.rgt:after,
    .hand.rgt:before {
      left: auto;
      right: -5px;
    }

    .foot {
      top: 400px;
      left: -80px;
      position: absolute;
      background: #000;
      z-index: 1400;
      box-shadow: 0 5px 5px rgba(0, 0, 0, 0.2);
      border-radius: 40px 40px 39px 40px / 26px 26px 63px 63px;
      width: 82px;
      height: 120px;
    }

    .foot:after {
      content: '';
      width: 55px;
      height: 65px;
      background: #222;
      border-radius: 100%;
      position: absolute;
      bottom: 10px;
      left: 0;
      right: 0;
      margin: auto;
    }

    .finger {
      position: absolute;
      width: 25px;
      height: 35px;
      background: #222;
      border-radius: 100%;
      top: 10px;
      right: 5px;
    }

    .finger:after,
    .finger:before {
      content: '';
      position: absolute;
      width: 20px;
      height: 35px;
      border-radius: 100%;
      background: #222;
      right: 30px;
      top: 0;
    }

    .finger:before {
      right: 55px;
      top: 5px;
    }

    .foot.rgt {
      left: auto;
      right: -80px;
    }

    .foot.rgt .finger {
      left: 5px;
      right: auto;
    }

    .foot.rgt .finger:after {
      left: 30px;
      right: auto;
    }

    .foot.rgt .finger:before {
      left: 55px;
      right: auto;
    }

    .panda-form {
      display: none;
      max-width: 500px;
      padding: 30px 50px;
      background: #fff;
      height: auto;
      margin: auto;
      display: block;
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.15);
      transition: transform 0.3s;
      position: relative;
      transform: translateY(-120px);
      z-index: 500;
      border: 1px solid #eee;
    }

    .panda-form p {
      font-size: 1rem;
      color: #555;
      margin-left: 140px;
      margin-top: 30px;
    }

    .panda-form a {
      margin-left: 165px;
      color: var(--primary);
      font-weight: bold;
    }

    .panda-form.up {
      transform: translateY(-200px);
    }

    .panda-form.wrong-entry {
      animation: panda-shake 0.3s;
      border: 1px solid red;
    }

    @keyframes panda-shake {
      0% {
        transform: translateX(0);
      }
      25% {
        transform: translateX(-5px);
      }
      50% {
        transform: translateX(5px);
      }
      75% {
        transform: translateX(-5px);
      }
      100% {
        transform: translateX(0);
      }
    }

    .panda-h1 {
      text-align: center;
      font-size: 3rem;
      margin-bottom: 20px;
      color: var(--primary);
      font-family: 'Dancing Script', cursive;
    }

    .panda-btn {
      background: #fff;
      padding: 5px;
      width: 190px;
      height: 50px;
      font-size: 1.2rem;
      border: 1px solid var(--primary);
      margin-top: 20px;
      margin-left: 130px;
      transition: all 0.3s;
      box-shadow: inset 0 50px var(--primary);
      color: #fff;
    }

    .panda-btn:hover {
      box-shadow: inset 0 0 var(--primary);
      color: var(--primary);
      background: #fff;
      font-weight: bold;
    }

    .panda-btn:focus {
      outline: none;
    }

    .form-group {
      position: relative;
      font-size: 15px;
      color: #666;
    }

    .form-group + .form-group {
      margin-top: 30px;
    }

    .form-label {
      position: absolute;
      z-index: 1;
      left: 0;
      top: 5px;
      transition: all 0.3s ease;
    }

    .form-control {
      width: 100%;
      position: relative;
      z-index: 3;
      height: 35px;
      background: none;
      border: none;
      padding: 5px 0;
      transition: all 0.3s ease;
      border-bottom: 1px solid #777;
      color: #555;
    }

    .form-control:invalid {
      outline: none;
    }

    .form-control:focus,
    .form-control:valid {
      outline: none;
      box-shadow: 0 1px 5px var(--primary);
      border-color: var(--primary);
    }

    .form-control:focus + .form-label,
    .form-control:valid + .form-label {
      font-size: 12px;
      color: var(--primary);
      transform: translateY(-15px);
    }

  </style>
  <%--  BOOTSTRAP --%>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>

<body>
<%--<!-- Gọi Sidebar -->--%>
<%--<jsp:include page="common/sidebar.jsp" />--%>

<%--<!-- Gọi Toast -->--%>
<%--<%@ include file="common/toast.jsp" %>--%>

<!-- Main Content -->
<div class="main-content" style="margin-top: 6rem">
  <div class="panda">
    <div class="ear"></div>
    <div class="face">
      <div class="eye-shade"></div>
      <div class="eye-white">
        <div class="eye-ball"></div>
      </div>
      <div class="eye-shade rgt"></div>
      <div class="eye-white rgt">
        <div class="eye-ball"></div>
      </div>
      <div class="nose"></div>
      <div class="mouth"></div>
    </div>
    <div class="body"> </div>
    <div class="foot" style="top: 470px">
      <div class="finger"></div>
    </div>
    <div class="foot rgt" style="top: 470px">
      <div class="finger"></div>
    </div>
  </div>

  <form action="${pageContext.request.contextPath}/register" method="post" class="panda-form bg-white p-4 shadow rounded">
    <div class="hand"></div>
    <div class="hand rgt"></div>
    <h1 class="panda-h1">Register</h1>
    <div class="form-group mb-3">
      <label for="username" class="form-label">Tài khoản:</label>
      <input type="text" id="username" name="username" class="form-control" value="${username}" required="required" placeholder="Nhập tài khoản">
    </div>
    <div class="form-group">
      <label for="password" class="form-label">Mật khẩu:</label>
      <input type="password" id="password" name="password" class="form-control" value="${password}" required="required" placeholder="Nhập mật khẩu">
    </div>
    <div class="form-group">
      <label for="fullName" class="form-label">Tên:</label>
      <input type="text" id="fullName" name="fullName" class="form-control" value="${fullName}" required="required" placeholder="Nhập họ và tên">
    </div>
    <div class="form-group mb-3">
      <label for="email" class="form-label">Email:</label>
      <input type="text" id="email" name="email" class="form-control" value="${email}" required="required" placeholder="Nhập email">
    </div>
    <button type="submit" class="panda-btn">Đăng ký</button>
  </form>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
  // Hiệu ứng khi nhấn vào input password
  $('#password').focusin(function() {
    $('form').addClass('up');
  });

  $('#password').focusout(function() {
    $('form').removeClass('up');
  });

  // Hiệu ứng mắt Panda
  $(document).on("mousemove", function(event) {
    var dw = $(document).width() / 15;
    var dh = $(document).height() / 15;
    var x = (event.pageX / dw);
    var y = (event.pageY / dh);
    $('.eye-ball').css({
      width: x + 'px',
      height: y + 'px'
    });
  });
</script>
</body>
</html>