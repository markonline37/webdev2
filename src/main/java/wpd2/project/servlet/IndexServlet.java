package wpd2.project.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wpd2.project.model.Topic;



public class IndexServlet extends BaseServlet {

    public IndexServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toPrint = "<h1>Temporary Homepage</h1><p>Welcome to the homepage, todo: add login etc</p>";
        issue(HTML_UTF_8, HttpServletResponse.SC_OK, toPrint.getBytes(CHARSET_UTF8), response);
    }
}
