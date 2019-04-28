package milestoner.servlet;
import milestoner.util.DB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class PostMilestoneServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(response.encodeRedirectURL("/project"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }

        String projectID = request.getParameter("pmProjectID");
        ProjectPageServlet.reload = projectID;
        String projectTitle = request.getParameter("pmTitle");
        String projectDescription = request.getParameter("pmDescription");
        String projectDay = request.getParameter("pmDay");
        String projectMonth = request.getParameter("pmMonth");
        String projectYear = request.getParameter("pmYear");
        String userID = UserFuncs.getUserID(request);

        String date = projectYear + "-" + projectMonth + "-" + projectDay + " 12:00:00";

        DB db = new DB();
        try{
            db.dbQuery("INSERT INTO milestone (userid, title, description, project, intendeddate) VALUES ('"+userID+"', '"+projectTitle+"', '"+projectDescription+"', '"+projectID+"', '"+date+"')");
        } catch (SQLException r){
            r.printStackTrace();
        } catch (ClassNotFoundException t){
            t.printStackTrace();
        } finally {
            response.sendRedirect(response.encodeRedirectURL("/project"));
        }
    }
}