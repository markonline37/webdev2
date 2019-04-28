package milestoner.servlet;

import milestoner.util.DB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ProjectServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(response.encodeRedirectURL("/project"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }

        String projectName = request.getParameter("newProject");
        if(projectName.equals("") || projectName == null){
            response.sendRedirect(response.encodeRedirectURL("/project"));
        }
        DB db = new DB();

        try{
            String temp = "";
            boolean test = true;
            do{
                temp = gen();
                if(!db.dbBoolean("SELECT sharevalue FROM project WHERE sharevalue = '"+temp+"'")){
                    test = false;
                }
            } while(test);
            db.dbQuery("INSERT INTO project (user, title, sharevalue) VALUES ('"+UserFuncs.getUserID(request)+"', '"+projectName+"', '"+temp+"')");
        } catch (SQLException r){
            r.printStackTrace();
        } catch (ClassNotFoundException t){
            t.printStackTrace();
        } finally {
            response.sendRedirect(response.encodeRedirectURL("/project"));
        }
    }

    String gen(){
        int n = 16;
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for(int i = 0; i < n; i++){
            int index = (int)(AlphaNumericString.length()*Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
