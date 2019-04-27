package milestoner.servlet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import milestoner.util.DB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ProjectServlet extends BaseServlet {

    private final String PROJECT_TEMPLATE = "project.mustache";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectName = request.getParameter("newProject");
        DB db = new DB();

        try{
            db.dbQuery("INSERT INTO project (user, title) VALUES ('"+UserFuncs.getUserID(request)+"', '"+projectName+"')");
        } catch (SQLException r){
            r.printStackTrace();
        } catch (ClassNotFoundException t){
            t.printStackTrace();
        } finally {
            response.sendRedirect(response.encodeRedirectURL("/project"));
        }
    }
}
