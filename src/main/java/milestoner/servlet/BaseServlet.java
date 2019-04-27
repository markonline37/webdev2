
package milestoner.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import milestoner.util.MustacheRenderer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class BaseServlet extends HttpServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    static final String HTML_UTF_8 = "text/html; charset=UTF-8";
    static final String PLAIN_TEXT_UTF_8 = "text/plain; charset=UTF-8";
    static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    static final String LOGIN_PAGE = "/login";

    private final MustacheRenderer mustache;

    BaseServlet() {
        mustache = new MustacheRenderer();
    }

    void issue(String mimeType, int returnCode, byte[] output, HttpServletResponse response) throws IOException {
        response.setContentType(mimeType);
        response.setStatus(returnCode);
        response.getOutputStream().write(output);
    }

    void showView(HttpServletResponse response, String templateName, Object model) throws IOException {
        String html = mustache.render(templateName, model);
        issue(HTML_UTF_8, HttpServletResponse.SC_OK, html.getBytes(CHARSET_UTF8), response);
    }

    void showView(HttpServletResponse response, String templateName) throws IOException {
        String html = mustache.render(templateName);
        issue(HTML_UTF_8, HttpServletResponse.SC_OK, html.getBytes(CHARSET_UTF8), response);
    }

    boolean authOK(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = UserFuncs.getUserID(request);
        if (username.equals("")){
            response.sendRedirect(response.encodeRedirectURL(LOGIN_PAGE));
            return false;
        }
        return true;
    }
}
