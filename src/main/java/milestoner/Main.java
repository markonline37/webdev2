package milestoner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import milestoner.servlet.*;

public class Main {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final int PORT = 9001;


    private Main() {
    }

    private void start() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "src/main/resources/webapp");

        IndexServlet ds = new IndexServlet();
        handler.addServlet(new ServletHolder(ds), "/");

        handler.addServlet(new ServletHolder(new ProjectPageServlet()), "/project");
        handler.addServlet(new ServletHolder(new LoginServlet()), "/login");
        handler.addServlet(new ServletHolder(new RegisterServlet()), "/register");
        handler.addServlet(new ServletHolder(new ProjectServlet()), "/newProject");
        handler.addServlet(new ServletHolder(new UpdateMilestoneServlet()), "/updateMilestone");
        handler.addServlet(new ServletHolder(new PostMilestoneServlet()), "/postMilestone");
        handler.addServlet(new ServletHolder(new DeleteMilestoneServlet()), "/deleteMilestone");
        handler.addServlet(new ServletHolder(new DeleteProjectServlet()), "/deleteProject");
        handler.addServlet(new ServletHolder(new ImproveMilestoneServlet()), "/improveMilestone");
        handler.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
        handler.addServlet(new ServletHolder(new DisableShareServlet()), "/disableShare");
        handler.addServlet(new ServletHolder(new EnableShareServlet()), "/enableShare");
        handler.addServlet(new ServletHolder(new LinkHandlerServlet()), "/share");

        server.start();
        LOG.info("Server started, will run until terminated");
        server.join();
    }

    public static void main(String[] args) {
        try {
            LOG.info("starting...");
            new Main().start();
        } catch (Exception e) {
            LOG.error("Unexpected error running: " + e.getMessage());
        }
    }
}
