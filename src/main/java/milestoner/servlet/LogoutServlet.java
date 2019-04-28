package milestoner.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LogoutServlet extends BaseServlet {


    static final Logger LOG = LoggerFactory.getLogger(LogoutServlet.class);

    public LogoutServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(response.encodeRedirectURL("/project"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        UserFuncs.clearCurrentUser(request);
        response.sendRedirect(response.encodeRedirectURL("/login"));
    }
}