package wpd2.project.servlet;

import wpd2.project.model.MessageBoard;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class MessageBoardServlet extends BaseServlet {


    //good practice to declare the template that is populated as a constant, why?
    //declare your template here
    private static final String MESSAGE_BOARD_TEMPLATE = "mb.mustache";
    //servlet can be serialized
    private static final long serialVersionUID = 687117339002032958L;

    public MessageBoardServlet()  {
    }

    //right now, setting the data for the page by hand, later that comes from a data store
    //helper method to create a MessageBoard object, which provides the data shown on the message board page
    private Object getObject() {
        MessageBoard mb = new MessageBoard();
        mb.setName("Simple Message Board");
        mb.setTopics(Arrays.asList("HTML", "JavaScript", "CSS", "event driven architecture"));
        return mb;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        showView(response, MESSAGE_BOARD_TEMPLATE, getObject());
    }
}
