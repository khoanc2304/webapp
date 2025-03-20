package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.UserFacebook;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import utils.validate.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "loginfacebookservlet", urlPatterns = {"/loginfacebookservlet"})
public class LoginFacebookServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            try {
                String accessToken = getToken(code); // Lấy access token
                UserFacebook userFacebook = getUserInfo(accessToken); // Lấy thông tin người dùng
                System.out.println("User Info from Facebook: " + userFacebook);

                if (userFacebook != null) {
                    // Lưu thông tin người dùng vào session
                    HttpSession session = request.getSession(true);
                    session.setAttribute("facebookId", userFacebook.getId());
                    session.setAttribute("facebookName", userFacebook.getName());
                    session.setAttribute("facebookEmail", userFacebook.getEmail());
                    session.setAttribute("facebookPicture", userFacebook.getPicture());
                    String pictureUrl = (userFacebook.getPicture() != null && userFacebook.getPicture().getData() != null)
                            ? userFacebook.getPicture().getData().getUrl()
                            : "";
                    session.setAttribute("facebookPicture", pictureUrl);
                    System.out.println("Facebook Picture URL saved in session: " + pictureUrl);
                    // Đăng nhập thành công và điều hướng người dùng
                    session.setAttribute("successMessage", "Đăng nhập thành công");
                    response.sendRedirect( "/"); // Redirect rõ ràng
                } else {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("errorMessage", "Không thể lấy thông tin người dùng từ Facebook.");
                    response.sendRedirect(request.getContextPath() + "/loginPage.jsp");
                }
            } catch (Exception e) {
                e.printStackTrace();
                HttpSession session = request.getSession(true);
                session.setAttribute("errorMessage", "Đã có lỗi xảy ra khi đăng nhập: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/loginPage.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/loginPage.jsp");
        }
    }
    public static String getToken(String code) throws ClientProtocolException, IOException {
        String response = Request.Post(Constants.FACEBOOK_LINK_GET_TOKEN)
                .bodyForm(
                        Form.form()
                                .add("client_id", Constants.FACEBOOK_CLIENT_ID)
                                .add("client_secret", Constants.FACEBOOK_CLIENT_SECRET)
                                .add("redirect_uri", Constants.FACEBOOK_REDIRECT_URI)
                                .add("code", code)
                                .build()
                )
                .execute().returnContent().asString();
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        if (jobj.has("access_token")) {
            String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
            return accessToken;
        } else {
            throw new IOException("Không thể lấy access token: " + response);
        }
    }
    public static UserFacebook getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = Constants.FACEBOOK_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
        if (jsonResponse.has("error")) {
            throw new IOException("Lỗi lấy thông tin người dùng: " + jsonResponse.get("error").toString());
        }
        UserFacebook fbAccount = new Gson().fromJson(response, UserFacebook.class);
        if (fbAccount.getPicture() != null && fbAccount.getPicture().getData() != null) {
            String pictureUrl = fbAccount.getPicture().getData().getUrl();
            System.out.println("Facebook User Picture: " + pictureUrl);
        }
        return fbAccount;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
