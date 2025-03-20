<%@ page import="utils.validate.Constants" %>
<%@ page import="java.net.URLEncoder" %><%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 17/02/2025
  Time: 4:18 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
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
            display: block;
            max-width: 500px;
            padding: 30px 50px;
            background: #fff;
            height: auto;
            margin: auto;
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

        /* Ensure proper alignment of the buttons */


    </style>



    <%--  BOOTSTRAP --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
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
        <div class="foot">
            <div class="finger"></div>
        </div>
        <div class="foot rgt">
            <div class="finger"></div>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/login" method="post" class="panda-form bg-white p-4 shadow rounded">
        <div class="hand"></div>
        <div class="hand rgt"></div>
        <h1 class="panda-h1">Login</h1>
        <div class="form-group">
            <label for="username" class="form-label">Username:</label>
            <input type="text" id="username" name="username" class="form-control" required="required" placeholder="Nhập tài khoản">
        </div>
        <div class="form-group">
            <label for="password" class="form-label">Password:</label>
            <input type="password" id="password" name="password" class="form-control" required="required" placeholder="Nhập mật khẩu">
        </div>
        <button type="submit" class="panda-btn">Login</button>
        <p>Don't have an account?</p>
        <a href="${pageContext.request.contextPath}/register.jsp">Register here !!!</a>
        <div class="row">
            <div class="col-12">
                <p style="margin-left: 170px" class="mt-6 mb-4">Or continue with</p>
                <div class="d-flex gap-2 flex-column">
                    <%
                        // Tạo URL đăng nhập Google
                        String googleLoginUrl = "https://accounts.google.com/o/oauth2/auth?" +
                                "scope=" + URLEncoder.encode(Constants.GOOGLE_SCOPE, "UTF-8") +
                                "&access_type=offline" +
                                "&include_granted_scopes=true" +
                                "&response_type=code" +
                                "&redirect_uri=" + URLEncoder.encode(Constants.GOOGLE_REDIRECT_URI, "UTF-8") +
                                "&client_id=" + Constants.GOOGLE_CLIENT_ID +
                                "&approval_prompt=force";
                    %>
                    <a style="margin-left: 10px" href="<%=googleLoginUrl%>" class="btn bsb-btn-xl btn-danger">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-google" viewBox="0 0 16 16">
                            <path d="M15.545 6.558a9.42 9.42 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.689 7.689 0 0 1 5.352 2.082l-2.284 2.284A4.347 4.347 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.792 4.792 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.702 3.702 0 0 0 1.599-2.431H8v-3.08h7.545z" />
                        </svg>
                        <span class="ms-2 fs-6 text-uppercase" style="color: #fff">Sign in With Google</span>
                    </a>
                    <a style="margin-left: 10px" href="https://www.facebook.com/v19.0/dialog/oauth?client_id=667150702479237&redirect_uri=http://localhost:8080/loginfacebookservlet&scope=public_profile,email&response_type=code&prompt=select_account" class="btn btn-primary">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-facebook" viewBox="0 0 16 16">
                            <path d="M16 8.049c0-4.446-3.582-8.05-8-8.05C3.58 0-.002 3.603-.002 8.05c0 4.017 2.926 7.347 6.75 7.951v-5.625h-2.03V8.05H6.75V6.275c0-2.017 1.195-3.131 3.022-3.131.876 0 1.791.157 1.791.157v1.98h-1.009c-.993 0-1.303.621-1.303 1.258v1.51h2.218l-.354 2.326H9.25V16c3.824-.604 6.75-3.934 6.75-7.951z" />
                        </svg>
                        <span class="ms-2 fs-6 text-uppercase" style="color: #fff">Sign in With Facebook</span>
                    </a>
                </div>
            </div>
        </div>
    </form>
</div>
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/vi_VN/sdk.js#xfbml=1&version=v19.0&appId=667150702479237"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js" integrity="sha384-cuYeSxntonz0PPNlHhBs68uyIAVpIIOZZ5JqeqvYYIcEL727kskC66kF92t6Xl2V" crossorigin="anonymous"></script>
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
