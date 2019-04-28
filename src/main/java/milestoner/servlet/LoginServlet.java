package milestoner.servlet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import milestoner.util.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);

    private final String LOGIN_TEMPLATE = "login.mustache";

    public LoginServlet(){

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        showView(response, LOGIN_TEMPLATE);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean error = false;
        class Error {
            String error1 = "";
            String error2 = "";
            String email = "";
        }
        Error e = new Error();
        if(email == null || email.length() == 0){
            error = true;
            e.error1 = "Email cannot be blank";
        } else if (email.length() >= 128){
            error = true;
            e.error1 = "Email is too long";
        } else {
            //if no errors - save email for reloads in case errors follow.
            e.email = email;
        }
        if(password == null || password.length() == 0){
            error = true;
            e.error2 = "Password cannot be blank";
        } else if (password.length() >= 72){
            error = true;

        }
        if(!error){
            try {
                String t = getPassword(email);
                if(t.equals("false")){
                    error = true;
                    e.error1 = "Email address not found";
                } else {
                    BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), t);
                    if(result.verified){
                        UserFuncs.setCurrentUser(request, email);
                        response.sendRedirect(response.encodeRedirectURL("/project"));
                    } else {
                        error = true;
                        e.error2 = "Passwords do not match";
                    }
                }

            } catch (SQLException t){
                t.printStackTrace();
            } catch (ClassNotFoundException r){
                r.printStackTrace();
            }
        }
        showView(response, LOGIN_TEMPLATE, e);
    }

    String getPassword(String e) throws SQLException, ClassNotFoundException{
        DB db = new DB();
        boolean check = db.dbBoolean("SELECT * FROM user WHERE email = '"+e+"'");
        if(check){
            return db.dbString("SELECT password FROM user WHERE email = '"+e+"'");
        } else {
            return "false";
        }
    }
}