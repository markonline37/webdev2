package milestoner.servlet;

import milestoner.util.DB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkHandlerServlet extends BaseServlet {
    String projectID = "";
    String projectTitle = "";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String temp = request.getParameter("id");
        try{
            DB db = new DB();
            ResultSet rs = db.dbRS("SELECT * FROM project WHERE sharevalue = '"+temp+"'");
            if(!rs.next()){
                String sb = header();
                sb+="<h1>No Project Found</h1>\n" +
                        "<div class=\"col-5\"><a href=\"/project\">Return to Milerstoner</a></div>";
                sb+=footer();
                issue(HTML_UTF_8, HttpServletResponse.SC_OK, sb.getBytes(CHARSET_UTF8), response);
            } else {

                projectID = rs.getString("id");
                projectTitle = rs.getString("title");
                String sb = header();
                sb+=active(db);
                sb+=completed(db);
                sb+=footer();
                issue(HTML_UTF_8, HttpServletResponse.SC_OK, sb.getBytes(CHARSET_UTF8), response);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException t){
            t.printStackTrace();
        }
    }

    String header(){
        String header = "<!DOCTYPE html>";
        header+="<html  lang=\"en\">";
        header+="<title>Milestoner</title>";
        header+="<head>";
        header+="<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">";
        header+="<style>" +
                ".a {text-align:left;margin:auto;}";
        header+=".error {color: red;}";
        header+=".tempTable {text-align: left;}";
        header+=".tab {overflow: hidden;border: 1px solid #ccc;background-color: #f1f1f1;}";
        header+=".tab button {background-color: inherit;float: left;border: none;outline: none;cursor: pointer;padding: 14px 16px;transition: 0.3s;font-size: 17px;}";
        header+=".tab button:hover {background-color: #ddd;}";
        header+=".tab span {background-color: inherit;float: left;border: none;outline: none;cursor: pointer;padding: 14px 16px;transition: 0.3s;font-size: 17px;}";
        header+=".tab span:hover {background-color: #ddd;}" +
                "h1 {text-align: left;}" +
                "image {float: left;text-align:left}";
        header+=".hidden {display: none;}";
        header+=".show {display: block;}";
        header+=".milestone {border-style: solid; padding-left: 10px; text-align:left;margin-left:10px;}";
        header+=".holder { padding-left: 10px; text-align:left;margin-left:10px;}";
        header+=".tab button.active {background-color: #ccc;}";
        header+="[class*=\"col-\"] {width: 100%;}";
        header+="[class*=\"col-\"] {float: left;text-align:left}";
        header+=".row::after {content: \"\";clear: both;display: table;}";
        header+=".tabcontent {display: none;padding: 6px 12px;border: 1px solid #ccc;border-top: none;}";
        header+="@media only screen and (min-width: 600px) {  [class*=\"col-\"] {text-align: center;}  .col-5 {width:50%;text-align:left;}  .col-ten {width:100%;text-align:left}  .col-10 {width:100%;}  .col-2 {width:20%;}  .col-8 {width:80%;}  }";
        header+="</style>";
        header+="</head>";
        header+="<body>\n";
        header+="<div class=\"rows\">\n";
        header+="<div class=\"col-10\"><h1>Milestoner</h1></div>\n";
        header+="<div class=\"col-10\">\n";
        return header;
    }

    String footer(){
        return "</div></div></body></html>";
    }

    String active(DB db){
        String t ="<div class=\"col-5\">\n" +
                "<h2>Active</h2>\n";
        try{
            ResultSet rs = db.dbRS("SELECT * FROM milestone WHERE project = '"+projectID+"' AND isfinished = '0'");
            if(!rs.next()){
                t+="<div class=\"holder\">No active milestones</div>";
            }else{
                rs.beforeFirst();
                while(rs.next()){
                    t+=     "<div class=\"milestone\">\n" +
                            "<h3>"+rs.getString("title")+"</h3>\n"+
                            "<p>"+rs.getString("description")+"</p>\n";
                    String ts = rs.getString("startdate");
                    ts = ts.substring(0, 10);
                    String y = rs.getString("intendeddate");
                    y = y.substring(0, 10);
                    t+=     "<table class=\"tempTable\"><tr><th></th><th>(YYYY-MM-DD)</th></tr>" +
                            "<tr><th>Date Added:</th><th>"+ts+"</th></tr>\n" +
                            "<tr><th>Intended Due Date:</th><th>"+y+"</th></tr>" +
                            "<tr><th><br></th></tr>" +
                            "</table></div><br>\n";
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException x){
            x.printStackTrace();
        }
        t+="</div>\n";
        return t;
    }

    String completed(DB db){
        String t = "<div class=\"col-5\">\n" +
                "<h2>Completed</h2>";
        try{
            ResultSet rs = db.dbRS("SELECT * FROM milestone WHERE project = '"+projectID+"' AND isfinished = '1'");
            if(!rs.next()){
                t+="<div class=\"holder\">No active milestones</div>";
            }else{
                rs.beforeFirst();
                while(rs.next()){
                    t+=     "<div class=\"milestone\">\n" +
                            "<h3>"+rs.getString("title")+"</h3>\n"+
                            "<p>"+rs.getString("description")+"</p>\n";
                    String ts = rs.getString("startdate");
                    ts = ts.substring(0, 10);
                    String y = rs.getString("intendeddate");
                    y = y.substring(0, 10);
                    String s = rs.getString("enddate");
                    s = s.substring(0, 9);
                    t+=     "<table class=\"tempTable\"><tr><th></th><th>(YYYY-MM-DD)</th></tr>" +
                            "<tr><th>Date Added:</th><th>"+ts+"</th></tr>\n" +
                            "<tr><th>Intended Due Date:</th><th>"+y+"</th></tr>" +
                            "<tr><th>Completed Date:</th><th>"+s+"</th></tr>" +
                            "</table></div><br>\n";
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException x){
            x.printStackTrace();
        }
        t+="</div>\n";
        return t;
    }
}
