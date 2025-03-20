package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.UserGoogle;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import service.user.IUserService;
import service.user.UserService;
import utils.validate.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "logingoogleservlet", urlPatterns = {"/logingoogleservlet"})
public class LoginGoogleServlet extends HttpServlet {
    private final IUserService userService = new UserService();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            String accessToken = getToken(code); // Lấy access token
            UserGoogle userGoogle = getUserInfo(accessToken); // Lấy thông tin người dùng
            System.out.println("User Info from Google: " + userGoogle);


            if (userGoogle != null) {
                // Lưu thông tin người dùng vào session (không cần lưu vào database)
                HttpSession session = request.getSession(true);
                session.setAttribute("googleId", userGoogle.getId());
                session.setAttribute("googleName", userGoogle.getName());
                session.setAttribute("googleEmail", userGoogle.getEmail());
                session.setAttribute("googlePicture", userGoogle.getPicture());

                // Đăng nhập thành công và điều hướng người dùng
                session.setAttribute("successMessage", "Đăng nhập thành công");
                response.sendRedirect( "/");
            } else {
                HttpSession session = request.getSession(true);
                session.setAttribute("errorMessage", "Không thể lấy thông tin người dùng từ Google.");
                response.sendRedirect(request.getContextPath() + "/loginPage.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/loginPage.jsp");
        }
    }

    public static UserGoogle getUserInfo(String accessToken) throws IOException {
        String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        System.out.println("Raw response from Google: " + response); // In dữ liệu thô
        UserGoogle googlePojo = new Gson().fromJson(response, UserGoogle.class);
        System.out.println("Parsed UserGoogle: " + googlePojo); // In đối tượng đã parse
        return googlePojo;
    }

    private static String getToken(String code) throws ClientProtocolException, IOException {
        // call api to get token
        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }


}
