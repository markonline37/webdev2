package wpd2.project;

import wpd2.project.servlet.DemoServlet;
import wpd2.project.servlet.IndexServlet;
import wpd2.project.servlet.MessageBoardServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.project.servlet.TopicServlet;

public class Runner {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private static final int PORT = 9003;

    private final String name;

    private Runner(String name) {
        this.name = name;
    }

    private void start() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "src/main/resources/webapp");

        IndexServlet index = new IndexServlet();
        handler.addServlet(new ServletHolder(index), "/");

        /*DemoServlet demoServlet = new DemoServlet(name);
        handler.addServlet(new ServletHolder(demoServlet), "/demo");

        MessageBoardServlet messageBoardServlet = new MessageBoardServlet();
        handler.addServlet(new ServletHolder(messageBoardServlet), "/messages");

        TopicServlet topicServlet = new TopicServlet();
        handler.addServlet(new ServletHolder(topicServlet), "/topics");*/

        server.start();
        LOG.info("Server started, will run until terminated");
        server.join();

    }

    public static void main(String[] args) {
        try {
            LOG.info("server starting...");
            new Runner("Demo").start();
        } catch (Exception e) {
            LOG.error("Unexpected error running: " + e.getMessage());
        }
    }
}
