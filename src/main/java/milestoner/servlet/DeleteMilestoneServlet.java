package milestoner.servlet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import milestoner.util.DB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class DeleteMilestoneServlet extends BaseServlet {

    private final String PROJECT_TEMPLATE = "project.mustache";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String value = request.getParameter("deleteID");
        String userID = UserFuncs.getUserID(request);
        DB db = new DB();

        try{
            db.dbQuery("DELETE FROM milestone WHERE id = '"+value+"' AND userid = '"+userID+"'");
        } catch (SQLException r){
            r.printStackTrace();
        } catch (ClassNotFoundException t){
            t.printStackTrace();
        } finally {
            response.sendRedirect(response.encodeRedirectURL("/project"));
        }
    }
}