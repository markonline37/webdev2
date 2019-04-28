package milestoner.servlet;

import milestoner.util.DB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ImproveMilestoneServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(response.encodeRedirectURL("/project"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }

import java.util.ArrayList;
import java.util.Arrays;

public class ImproveMilestoneServlet extends BaseServlet {

    private final String PROJECT_TEMPLATE = "project.mustache";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String milestoneID = request.getParameter("milestoneID");
        String projectID = request.getParameter("projectID");
        ProjectPageServlet.reload = projectID;
        String projectTitle = request.getParameter("improveTitle");
        String projectDescription = request.getParameter("improveDescription");
        String projectDay = request.getParameter("improveDay");
        String projectMonth = request.getParameter("improveMonth");
        String projectYear = request.getParameter("improveYear");
        String userID = UserFuncs.getUserID(request);


        String date = projectYear + "-" + projectMonth + "-" + projectDay + " 12:00:00";

        DB db = new DB();
        try{
            String s = "UPDATE milestone SET title = '"+projectTitle+"', description = '"+projectDescription+"', intendeddate = '"+date+"' WHERE id = '"+milestoneID+"' AND userid = '"+userID+"'";
            db.dbQuery(s);
        } catch (SQLException r){
            r.printStackTrace();
        } catch (ClassNotFoundException t){
            t.printStackTrace();
        } finally {
            response.sendRedirect(response.encodeRedirectURL("/project"));
        }
    }
}