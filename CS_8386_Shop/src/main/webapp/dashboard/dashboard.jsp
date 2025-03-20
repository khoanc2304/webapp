<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 26/02/2025
  Time: 9:52 CH
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: OS
  Date: 26/02/2025
  Time: 9:52 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<%@ page import="java.util.*" %>

<!-- Kiểm tra người dùng đã đăng nhập chưa -->
<c:if test="${empty sessionScope.loggedInUser}">
    <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #f4f4f4, #e0e7ff);
            margin: 0;
            padding: 0;
            color: #333;
        }
        #main-content {
            margin-top: 4rem;
            text-align: center;
            color: #2c3e50;
            font-size: 1.5rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .dashboard {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 25px;
            padding: 30px;
        }
        .card {
            width: 220px;
            padding: 25px;
            border-radius: 15px;
            text-align: center;
            font-size: 1.2rem;
            font-weight: 600;
            color: #fff;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            position: relative;
            overflow: hidden;
        }
        .card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(45deg, rgba(255, 255, 255, 0.2), transparent);
            z-index: 0;
            transition: opacity 0.3s ease;
            opacity: 0;
        }
        .card:hover::before {
            opacity: 1;
        }
        .card:hover {
            transform: translateY(-10px);
            box-shadow: 0 12px 24px rgba(0, 0, 0, 0.25);
        }
        .card p {
            margin: 0;
            z-index: 1;
            position: relative;
        }
        .card a {
            display: block;
            margin-top: 15px;
            color: #fff;
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: 500;
            z-index: 1;
            position: relative;
            transition: color 0.3s ease;
        }
        .card a:hover {
            color: #f1f1f1;
        }
        .blue { background: linear-gradient(45deg, #007BFF, #00c4ff); }
        .green { background: linear-gradient(45deg, #28A745, #34d058); }
        .yellow { background: linear-gradient(45deg, #FFC107, #ffda6a); }
        .red { background: linear-gradient(45deg, #DC3545, #ff5765); }
        .chart-container {
            width: 80%;
            max-width: 900px;
            margin: 40px auto;
            background: #fff;
            padding: 20px;
            border-radius: 15px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
        }
        .chart-wrapper {
            display: flex;
            justify-content: space-between;
            gap: 20px;
            flex-wrap: wrap; /* Để các biểu đồ xuống dòng nếu màn hình nhỏ */
        }
        .chart-box {
            flex: 1;
            height: 400px; /* Chiều cao cho mỗi biểu đồ */
            min-width: 300px; /* Đảm bảo biểu đồ không bị quá nhỏ */
        }
        canvas {
            height: 100% !important;
        }
    </style>
</head>
<body>
<jsp:include page="../common/sidebar.jsp" />
<jsp:include page="../common/toast.jsp" />

<div id="main-content">
    <h4>Xin chào đến với Dashboard Admin</h4>
</div>

<div class="dashboard">
    <div class="card blue">
        <p><strong>Tổng số sản phẩm:</strong> <%= request.getAttribute("productCount") %></p>
        <p>Danh sách sản phẩm</p>
        <a href="${pageContext.request.contextPath}/product">Đi tới →</a>
    </div>
    <div class="card green">
        <p>9</p>
        <p>Danh sách đơn hàng</p>
        <a href="${pageContext.request.contextPath}/orders">Đi tới →</a>
    </div>
    <div class="card yellow">
        <p><strong>Tổng số user:</strong> <%= request.getAttribute("userCount") %></p>
        <p>Quản lý người dùng</p>
        <a href="${pageContext.request.contextPath}/users?action=listUsers">Đi tới →</a>
    </div>
    <div class="card red">
        <p>2</p>
        <p>Quản lý liên hệ</p>
        <a href="${pageContext.request.contextPath}/contacts">Đi tới →</a>
    </div>
</div>

<div class="chart-container">
    <div class="chart-wrapper">
        <div class="chart-box">
            <canvas id="stockChart"></canvas>
        </div>
        <div class="chart-box">
            <canvas id="stockPieChart"></canvas>
        </div>
    </div>
    <div class="chart-wrapper">
        <div class="chart-box">
            <canvas id="revenueChart"></canvas>
        </div>
    </div>
</div>

<script>
    // Lấy dữ liệu từ thuộc tính stockByBrand
    const stockData = <%= request.getAttribute("stockByBrand") %>;
    const labels = stockData.map(item => item.brand);
    const stocks = stockData.map(item => item.stock);

    // Biểu đồ cột (Bar Chart)
    const ctx = document.getElementById('stockChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Số lượng tồn kho theo thương hiệu',
                data: stocks,
                backgroundColor: 'rgba(54, 162, 235, 0.7)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1,
                hoverBackgroundColor: 'rgba(54, 162, 235, 0.9)',
                hoverBorderColor: 'rgba(54, 162, 235, 1)'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Số lượng tồn kho',
                        font: {
                            size: 14,
                            weight: 'bold'
                        }
                    },
                    grid: {
                        color: 'rgba(0, 0, 0, 0.05)'
                    },
                    ticks: {
                        font: {
                            size: 12
                        }
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Thương hiệu',
                        font: {
                            size: 14,
                            weight: 'bold'
                        }
                    },
                    grid: {
                        display: false
                    },
                    ticks: {
                        font: {
                            size: 12
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'top',
                    labels: {
                        font: {
                            size: 14,
                            weight: 'bold'
                        }
                    }
                },
                tooltip: {
                    backgroundColor: '#2c3e50',
                    titleFont: {
                        size: 14,
                        weight: 'bold'
                    },
                    bodyFont: {
                        size: 12
                    },
                    padding: 10,
                    cornerRadius: 5
                }
            },
            animation: {
                duration: 1500,
                easing: 'easeInOutQuart'
            }
        }
    });

    // Biểu đồ tròn (Pie Chart)
    const ctxPie = document.getElementById('stockPieChart').getContext('2d');
    new Chart(ctxPie, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                label: 'Phân phối tồn kho',
                data: stocks,
                backgroundColor: [
                    'rgba(54, 162, 235, 0.7)',  // ACER
                    'rgba(255, 99, 132, 0.7)',  // Apple
                    'rgba(75, 192, 192, 0.7)',  // ASUS
                    'rgba(255, 159, 64, 0.7)',  // Dell
                    'rgba(153, 102, 255, 0.7)', // GIGABYTE
                    'rgba(255, 206, 86, 0.7)',  // HP
                    'rgba(255, 205, 210, 0.7)', // LENOVO
                    'rgba(201, 203, 207, 0.7)'  // MSI
                ],
                borderColor: [
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 99, 132, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(255, 159, 64, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 205, 210, 1)',
                    'rgba(201, 203, 207, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'right',
                    labels: {
                        font: { size: 12 }
                    }
                },
                tooltip: {
                    backgroundColor: '#2c3e50',
                    titleFont: { size: 14, weight: 'bold' },
                    bodyFont: { size: 12 },
                    padding: 10,
                    cornerRadius: 5
                }
            },
            animation: {
                duration: 1500,
                easing: 'easeInOutQuart'
            }
        }
    });
</script>
</body>
</html>