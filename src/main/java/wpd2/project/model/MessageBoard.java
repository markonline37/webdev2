package wpd2.project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kat on 06/03/2018.
 */
public class MessageBoard {
    private String name;
    private List<String> topics;

    public MessageBoard() {

        this.topics = new ArrayList<String>();
    }

    public String getName() {
        return this.name;
    }

    public List<String> getTopics() {
        return this.topics;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setTopics(List<String> t) {
        this.topics = t;
    }


    public String toString() {
        return ("name: " + name + ", topics: " + topics.toString());
    }
}
