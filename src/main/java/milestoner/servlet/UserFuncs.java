package milestoner.servlet;

import lombok.NonNull;
import milestoner.util.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;


class UserFuncs {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(UserFuncs.class);

    private static String USER_ID = "";

    private UserFuncs() {}

    static void setCurrentUser(HttpServletRequest request, @NonNull String email) {
        HttpSession session = request.getSession(true);
        DB db = new DB();
        String temp = "";
        try{
            temp = db.dbString("SELECT id FROM user WHERE email = '"+email+"'");
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException t){
            t.printStackTrace();
        }
        session.setAttribute(USER_ID, temp);
    }

    static void clearCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.removeAttribute(USER_ID);
    }

    private static String getString(HttpServletRequest request, String input) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "";
        }
        String val = (String)session.getAttribute(input);
        return val == null ? "" : val;
    }

    static String getUserID(HttpServletRequest request) {
        return getString(request, USER_ID);
    }
}

