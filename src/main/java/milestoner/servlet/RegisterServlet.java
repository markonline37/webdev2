package milestoner.servlet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import milestoner.util.DB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegisterServlet extends BaseServlet {
    private final String LOGIN_TEMPLATE = "login.mustache";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("registerUsername");
        String email = request.getParameter("registerEmail");
        String password = request.getParameter("registerPassword");

        boolean error = false;
        class Error {
            String error3 = "";
            String error4 = "";
            String error5 = "";
            String reguser = "";
            String regemail = "";
        }
        Error e = new Error();
        DB db = new DB();
        try{
            if(name.length() < 5 || name.length() > 32){
                error = true;
                e.error3 = "Name must be between 5 and 32 characters";
            } else {
                e.reguser = name;
            }
            if(email.length() < 5 || email.length() > 128){
                error = true;
                e.error4 = "Email must be between 5 and 128 characters";
            } else if(!isValid(email)){
                error = true;
                e.error4 = "Email is not valid";
            } else if(db.dbBoolean("SELECT * FROM user WHERE email = '"+email+"'")){
                error = true;
                e.error4 = "Email address already in use";
            } else {
                e.regemail = email;
            }
            if(password.length() < 6 || password.length() > 72){
                error = true;
                e.error5 = "Password must be between 6 and 72 characters";
            }
            if(error){
                showView(response, LOGIN_TEMPLATE, e);
            } else {
                String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                db.dbQuery("INSERT INTO user (USERNAME, email, password) VALUES ('"+name+"', '"+email+"', '"+bcryptHashString+"')");
                UserFuncs.setCurrentUser(request, email);
                response.sendRedirect(response.encodeRedirectURL("/project"));
            }
        } catch (SQLException r){
            r.printStackTrace();
        } catch (ClassNotFoundException t){
            t.printStackTrace();
        }
    }

    //https://www.geeksforgeeks.org/check-email-address-valid-not-java/
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
