package wpd2.project.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DemoServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(DemoServlet.class);
    private static final long serialVersionUID = -7461821901454655091L;

    private final String name;

    public DemoServlet(String n) {
        this.name = n;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toPrint = "<html><body><p>Welcome, your name is  " + name + " and your path is " + request.getRequestURI()+
                "</p></body></html>"    ;
        issue(HTML_UTF_8, HttpServletResponse.SC_OK, toPrint.getBytes(CHARSET_UTF8), response);
    }
}
