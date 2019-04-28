package milestoner.servlet;
import milestoner.util.DB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class DisableShareServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }

        String value = request.getParameter("toggleID");
        String user = UserFuncs.getUserID(request);
        ProjectPageServlet.reload = value;
        DB db = new DB();

        try{
            db.dbQuery("UPDATE project SET isshared = '0' WHERE id = '"+value+"' AND user = '"+user+"'");
        } catch (SQLException r){
            r.printStackTrace();
        } catch (ClassNotFoundException t){
            t.printStackTrace();
        } finally {
            response.sendRedirect(response.encodeRedirectURL("/project"));
        }
    }
}