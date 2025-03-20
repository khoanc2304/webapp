package service.order;

import model.Order;
import model.OrderDetail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class EmailService {
    private final String fromEmail = "vantai04102004@gmail.com"; // Email gửi
    private final String password = "dnqm esun iwhr jeib"; // Mật khẩu ứng dụng

    public void sendOrderConfirmation(String toEmail, String subject, Order order) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Máy chủ SMTP của Gmail
        props.put("mail.smtp.port", "587"); // Cổng SMTP
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Bật TLS

        // Xác thực tài khoản email
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            String encodedSubject = MimeUtility.encodeText(subject, "UTF-8", "B");
            message.setSubject(encodedSubject);

            // Tạo nội dung HTML cho email
            String htmlBody = buildOrderConfirmationEmail(order);

            // Sử dụng HTML content và UTF-8 charset
            message.setContent(htmlBody, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);
            System.out.println("Email đã được gửi thành công!");
        } catch (MessagingException e) {
            e.printStackTrace();  // In ra lỗi nếu có
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    // Phương thức để tạo nội dung HTML cho email
    private String buildOrderConfirmationEmail(Order order) {
        // Định dạng ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String orderDate = dateFormat.format(order.getOrderDate());

        // Định dạng tiền tệ
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String totalPrice = decimalFormat.format(order.getTotalPrice());

        // Bắt đầu chuỗi HTML
        StringBuilder htmlBody = new StringBuilder();
        htmlBody.append("<!DOCTYPE html>")
                .append("<html lang='vi'>")
                .append("<head>")
                .append("<meta charset='UTF-8'>")
                .append("<style>")
                .append("body { font-family: 'Roboto', sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }")
                .append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }")
                .append(".confirmation-card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); text-align: center; }")
                .append(".confirmation-icon { color: #4CAF50; font-size: 50px; margin-bottom: 10px; }")
                .append(".order-details { background: #fff; padding: 15px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); margin-top: 20px; }")
                .append(".continue-shopping-btn { display: inline-block; background-color: #007bff; color: #fff; font-weight: 600; padding: 10px 20px; border-radius: 8px; text-decoration: none; margin-top: 15px; }")
                .append(".continue-shopping-btn:hover { background-color: #0056b3; }")
                .append(".table-order-details { width: 100%; border-collapse: collapse; margin-top: 10px; }")
                .append(".table-order-details th, .table-order-details td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }")
                .append(".table-order-details th { background-color: #f8f9fa; }")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class='container'>")
                .append("<div class='confirmation-card'>")
                .append("<div class='confirmation-icon'>✔</div>")
                .append("<h2>Đặt hàng thành công!</h2>")
                .append("<p>Cảm ơn bạn đã mua sắm tại 8386 Shop.</p>")
                .append("<p>Mã đơn hàng của bạn là: <strong>#").append(order.getOrderID()).append("</strong></p>")
                .append("<p>Chúng tôi sẽ gửi thông báo đến email của bạn khi đơn hàng được xử lý.</p>")
                .append("<a href='http://localhost:8080//product' class='continue-shopping-btn'>Tiếp tục mua sắm</a>")
                .append("</div>")
                .append("<div class='order-details'>")
                .append("<h4>Chi tiết đơn hàng</h4>")
                .append("<div style='display: flex; justify-content: space-between;'>")
                .append("<div>")
                .append("<p><strong>Ngày đặt hàng:</strong> ").append(orderDate).append("</p>")
                .append("<p><strong>Trạng thái:</strong> ").append(order.getOrderStatus()).append("</p>")
                .append("</div>")
                .append("<div>")
                .append("<p><strong>Tổng tiền:</strong> ").append(totalPrice).append(" VNĐ</p>")
                .append("</div>")
                .append("</div>")
                .append("<table class='table-order-details'>")
                .append("<thead>")
                .append("<tr>")
                .append("<th>Sản phẩm</th>")
                .append("<th>Số lượng</th>")
                .append("<th>Đơn giá</th>")
                .append("<th>Thành tiền</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        // Thêm chi tiết đơn hàng
        List<OrderDetail> orderDetails = order.getOrderDetail();
        if (orderDetails != null) {
            for (OrderDetail item : orderDetails) {
                String price = decimalFormat.format(item.getPrice());
                String totalItemPrice = decimalFormat.format(item.getPrice() * item.getQuantity());
                htmlBody.append("<tr>")
                        .append("<td>").append(item.getProductName()).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td>").append(price).append(" VNĐ</td>")
                        .append("<td>").append(totalItemPrice).append(" VNĐ</td>")
                        .append("</tr>");
            }
        }

        htmlBody.append("</tbody>")
                .append("</table>")
                .append("</div>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return htmlBody.toString();
    }
}
