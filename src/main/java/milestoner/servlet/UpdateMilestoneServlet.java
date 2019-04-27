package milestoner.servlet;


import milestoner.util.DB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class UpdateMilestoneServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String value = request.getParameter("checkbox");
        DB db = new DB();

        try{
            if(value.length() > 2){
                char r = value.charAt(value.length()-2);
                String t = "" + r;
                if(t.equals("s")){
                    String s = value.substring(0, value.length() - 6);
                    ProjectPageServlet.reload = db.dbString("SELECT project FROM milestone WHERE id = '"+s+"'");
                    db.dbQuery("UPDATE milestone SET isfinished = '1', enddate = NOW() WHERE id = '"+s+"'");
                } else {
                    String s = value.substring(0, value.length() - 5);
                    db.dbQuery("UPDATE milestone SET isfinished = '0' WHERE id = '"+s+"'");
                }
            }

        } catch (SQLException r){
            r.printStackTrace();
        } catch (ClassNotFoundException t){
            t.printStackTrace();
        } finally {
            response.sendRedirect(response.encodeRedirectURL("/project"));
        }
    }
}