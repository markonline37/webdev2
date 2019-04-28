package milestoner.servlet;

import milestoner.util.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProjectPageServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(ProjectPageServlet.class);

    static String reload = "";
    static boolean showError = false;
    static String errorText = "";
    String username = "";
    String userID = "";
    List<String> projectID = new ArrayList<>();
    List<String> projectTitle = new ArrayList<>();
    List<String> projectShare = new ArrayList<>();

    public ProjectPageServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }


        try {
            DB db = new DB();
            userID = UserFuncs.getUserID(request);
            username = db.dbString("SELECT username FROM user WHERE id = '"+userID+"'");
            projectID = db.dbArray("SELECT id FROM project WHERE user = '"+userID+"'");
            projectShare = db.dbArray("SELECT isshared FROM project WHERE user = '"+userID+"'");

            if(reload.equals("")){
                if(projectID.size() > 0){
                    reload = projectID.get(0);
                } else {
                    reload = "";
                }
            }
            projectTitle = db.dbArray("SELECT title FROM project WHERE user = '"+userID+"'");
            String temp = header(db);
            temp+=footer();
            issue(HTML_UTF_8, HttpServletResponse.SC_OK, temp.getBytes(CHARSET_UTF8), response);
        } catch (SQLException t){
            t.printStackTrace();
        } catch (ClassNotFoundException r){
            r.printStackTrace();
        }
    }

    String header(DB db){
        String header = "<!DOCTYPE html>";
        header+="<html  lang=\"en\">";
        header+="<title>Milestoner</title>";
        header+="<head>";
        header+="<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">";
        header+="<style>";
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
        header+="<div class=\"col-8\"><h1>Milestoner</h1></div>\n";
        header+="<div class=\"col-2\">\n";
        header+="<p>Hi there "+username+"</p>\n";
        header+="<form action=\"/logout\" method=\"POST\">\n";
        header+="<button type=\"submit\">Logout</button>\n";
        header+="</form>\n";
        header+="</div>\n";
        header+="<div class=\"col-10\">\n";
        /*
        -----------------------------------------------------------------------
        Menu
         */
        header+="<div class=\"tab\">\n";
        for(int i = 0; i < projectID.size(); i++){
            header+="<button \n";
            if(projectID.get(i).equals(reload)){
                header+="id=\"defaultOpen\"\n";
            }
            header+=" class=\"tablinks\" onclick=\"changeProject(event, '"+projectTitle.get(i)+"')\">"+projectTitle.get(i)+"</button>\n";
        }
        /*
        -----------------------------------------------------------------------
        New Project Form
         */
        header+="<button class=\"\" onclick=\"loadNewProjectForm()\">New Project<i class=\"fa fa-plus\" aria-hidden=\"true\"></i></button>\n";
        header+="<span><div id=\"newProjectDiv\" class=\"hidden\">" +
                "<form action=\"/newProject\" name=\"newProjectForm\" onsubmit=\"return validateNewProjectForm()\" method=\"POST\">" +
                "<input type=\"text\" id=\"newProjectInput\" name=\"newProject\" placeholder=\"Project Name\">" +
                "<input type=\"submit\" value=\"Create\">" +
                "</form>" +
                "<div class=\"error\" id=\"newProjectError\"></div></div></span>\n";
        header+="</div>\n";
        header+="</div>\n";
        if(showError){
            header+="<div class=\"col-10\"><table><tr><th><img class=\"image\"src=\"https://i.imgur.com/9S6KHrj.gif\" alt=\"Hacker Man\" style=\"width:54px;height:85px;\"></th><th><div class=\"error\">Uh Uh Uh<br>"+errorText+"</div></th></tr></table></div>\n";
        }
        /*
        -----------------------------------------------------------------------
        New Milestone Form
         */
        for(int i = 0; i < projectID.size(); i++){
            header+="<div id='"+projectTitle.get(i)+"' class=\"tabcontent\">\n";
            header+="<div class=\"col-5\">\n";
            header+="<table><tr><th><br><button id=\""+projectID.get(i)+"_newItem\" class=\"\">New Milestone<i class=\"fa fa-plus\" aria-hidden=\"true\"></i></button>\n";
            header+="<div id=\""+projectID.get(i)+"_hiddendiv\" class=\"hidden\">\n" +
                    "<form action=\"/postMilestone\" onsubmit=\"return validateNewMilestoneForm("+projectID.get(i)+")\" name=\"postMilestoneForm"+projectID.get(i)+"\" method=\"POST\">\n" +
                    "<input type=\"hidden\" name=\"pmProjectID\" value=\""+projectID.get(i)+"\">\n" +
                    "Title:<input type=\"text\" name=\"pmTitle\" placeholder=\"Enter Title\"><br>\n" +
                    "Description:<input type=\"text\" name=\"pmDescription\" placeholder=\"Enter Description\"><br>\n" +
                    "Intended Due Date:<input type=\"text\" name=\"pmDay\" placeholder=\"Day\"><input type=\"text\" name=\"pmMonth\" placeholder=\"Month\"><input type=\"text\" name=\"pmYear\" placeholder=\"Year\"><br>" +
                    "<input type=\"submit\" value=\"Create\">\n" +
                    "</form>\n" +
                    "<div class=\"error\" id=\"me_"+projectID.get(i)+"\"></div>" +
                    "</div></th></tr></table>\n";
            /*
            -----------------------------------------------------------------------
            Each active milestone
             */
            header+="<h2>Active</h2>\n";
            try{
                ResultSet rsMilestones = db.dbRS("SELECT * FROM milestone WHERE userid = '"+userID+"' AND isfinished = '0' AND project = '"+projectID.get(i)+"'");
                if(!rsMilestones.next()){
                    header+="<div class=\"holder\">No active milestones</div>";
                }else{
                    rsMilestones.beforeFirst();
                    while(rsMilestones.next()){
                        header+="<div id=\"milestone_"+rsMilestones.getString("id")+"\" class=\"milestone\">\n";
                        header+="<button onclick=\"submitEdit("+rsMilestones.getString("id")+")\">Edit Milestone</button>\n";
                        header+="<h3>"+rsMilestones.getString("title")+"</h3>\n";
                        header+="<p>"+rsMilestones.getString("description")+"</p>\n";
                        String t = rsMilestones.getString("startdate");
                        t = t.substring(0, 10);
                        String y = rsMilestones.getString("intendeddate");
                        y = y.substring(0, 10);
                        header+="<table class=\"tempTable\"><tr><th></th><th>(YYYY-MM-DD)</th></tr><tr><th>Date Added:</th><th>"+t+"</th></tr>\n" +
                                "<tr><th>Intended Due Date:</th><th>"+y+"</th></tr>\n";
                        header+="<tr><th>Make Completed:</th><th><form action=\"/updateMilestone\" name=\"updateMilestoneForm\" method=\"POST\"><input type=\"checkbox\" onChange=\"this.form.submit()\" name=\"checkbox\" value=\""+rsMilestones.getString("id")+"_false\"></form></th></tr>\n";
                        header+="</table>\n";
                        header+="<form action=\"/deleteMilestone\" onsubmit=\"return validateDelete()\" name=\"deleteMilestoneForm\" method=\"POST\">\n" +
                                "<input type=\"hidden\" name=\"deleteID\" value=\""+rsMilestones.getString("id")+"\">\n" +
                                "<button type=\"submit\">Delete <i class=\"fa fa-trash\" aria-hidden=\"true\"></i></button>\n" +
                                "</form><br>\n";
                        String date = rsMilestones.getString("intendeddate");
                        String year = date.substring(0, 4);
                        String month = date.substring(5, 7);
                        String day = date.substring(8, 10);
                        header+="</div>" +
                                "<div id=\"edit_"+rsMilestones.getString("id")+"\" class=\"hidden\">\n" +
                                "<form action=\"/improveMilestone\" onsubmit=\"return validateImprove("+rsMilestones.getString("id")+")\" name=\""+rsMilestones.getString("id")+"_edit_form\" method=\"POST\">\n" +
                                "   <h3>Edit Milestone</h3>" +
                                "   <table>" +
                                "   <input type=\"hidden\" name=\"milestoneID\" value=\""+rsMilestones.getString("id")+"\">" +
                                "   <input type=\"hidden\" name=\"projectID\" value=\""+projectID.get(i)+"\">" +
                                "   <tr><th>Title:</th><th><input type=\"text\" name=\"improveTitle\" value=\""+rsMilestones.getString("title")+"\"></th></tr>\n" +
                                "   <tr><th>Description:</th><th><input type=\"text\" name=\"improveDescription\" value=\""+rsMilestones.getString("description")+"\"></th></tr>\n" +
                                "   <tr><th>Intended Due Date:</th><th></th></tr>" +
                                "   <tr><th>Day: </th><th><input type=\"text\" name=\"improveDay\" value=\""+day+"\"></th></tr>" +
                                "   <tr><th>Month: </th><th><input type=\"text\" name=\"improveMonth\" value=\""+month+"\"><br></th></tr>" +
                                "   <tr><th>Year: </th><th><input type=\"text\" name=\"improveYear\" value=\""+year+"\"></th></tr>" +
                                "   <tr><th><input type=\"submit\" value=\"Submit Edit\"></th><th><button type=\"button\" onclick=\"cancelEdit("+rsMilestones.getString("id")+")\">Cancel Edit</button></th></tr>\n" +
                                "   </table>\n" +
                                "</form>\n" +
                                "<div id=\"edit_error_"+rsMilestones.getString("id")+"\"></div>" +
                                "</div>\n" +
                                "<br>\n";
                    }
                }
            } catch (SQLException e){
                e.printStackTrace();
            } catch (ClassNotFoundException t){
                t.printStackTrace();
            }
            header+="</div>\n";

            header+="<div class=\"col-5\">\n" +
                    "<br><table><tr>";
            //delete project form
            header+="<th><form action=\"/deleteProject\" onsubmit=\"return validateDeleteProject()\" name=\"deleteProjectForm\" method=\"POST\">\n" +
                    "<input type=\"hidden\" name=\"deleteID\" value=\""+projectID.get(i)+"\">\n" +
                    "<button type=\"submit\">Delete Entire Project <i class=\"fa fa-trash\" aria-hidden=\"true\"></i></button>\n" +
                    "</form></th>\n";
            //share project

            if(projectShare.get(i).equals("0")){
                header+="<th><form action=\"/enableShare\" name=\"toggleShareForm method=\"POST\">\n" +
                        "<input type=\"hidden\" name=\"toggleID\" value=\""+projectID.get(i)+"\">\n" +
                        "<button type=\"submit\">Share Project <i class=\"fa fa-share-alt\" aria-hidden=\"true\"></i></button>\n" +
                        "</form></th>";
            } else {
                header+="<th><form action=\"/disableShare\" name=\"toggleShareForm method=\"POST\">\n" +
                        "<input type=\"hidden\" name=\"toggleID\" value=\""+projectID.get(i)+"\">\n" +
                        "<button type=\"submit\">Stop Sharing Project <i class=\"fa fa-share-alt\" aria-hidden=\"true\"></i></button>\n" +
                        "</form></th>";
                try {
                    String temp = db.dbString("SELECT sharevalue FROM project WHERE id = '"+projectID.get(i)+"'");
                    String url = DB.url + "/share?id="+temp;
                    header+="<th><input type=\"text\" value=\""+url+"\" id=\""+projectID.get(i)+"_share"+"\"></th>\n" +
                            "<th><button onclick=\"shareFunction('"+projectID.get(i)+"_share"+"')\">Copy Link</button></th>\n";
                } catch (SQLException e){
                    e.printStackTrace();
                } catch (ClassNotFoundException t){
                    t.printStackTrace();
                }
            }
            header+="</tr></table>";
            /*
            -----------------------------------------------------------------------
            Each completed milestone
             */
            header+="<h2>Completed</h2>\n";
            try{
                ResultSet rs = db.dbRS("SELECT * FROM milestone WHERE userid = '"+userID+"' AND isfinished = '1' AND project = '"+projectID.get(i)+"'");
                if(!rs.next()){
                    header+="<div class=\"holder\">No completed milestones yet</div>";
                }
                rs.beforeFirst();
                while(rs.next()){
                    header+="<div id=\"milestone_"+rs.getString("id")+"\" class=\"milestone\">\n";
                    header+="<h3>"+rs.getString("title")+"</h3>\n";
                    header+="<p>"+rs.getString("description")+"</p>\n";
                    String t = rs.getString("startdate");
                    t = t.substring(0, 10);
                    String y = rs.getString("intendeddate");
                    y = y.substring(0, 10);
                    header+="<table class=\"tempTable\"><tr><th></th><th>(YYYY-MM-DD)</th></tr><tr><th>Date Added:</th><th>"+t+"</th></tr>\n" +
                            "<tr><th>Intended Due Date:</th><th>"+y+"</th></tr>\n";
                    String s = rs.getString("enddate");
                    s = s.substring(0, 9);
                    header+="<tr><th>Completed Date:</th><th>"+s+"</th></tr>\n";
                    header+="<tr><th>Make Active:</th><th><form action=\"/updateMilestone\" name=\"updateMilestoneForm\" method=\"POST\"><input type=\"checkbox\" onChange=\"this.form.submit()\" name=\"checkbox\" value=\""+rs.getString("id")+"_true\"></form></th></tr>\n";
                    header+="</table>\n" +
                            "<form action=\"/deleteMilestone\" onsubmit=\"return validateDelete()\" name=\"deleteMilestoneForm\" method=\"POST\">\n" +
                            "<input type=\"hidden\" name=\"deleteID\" value=\""+rs.getString("id")+"\">\n" +
                            "<button type=\"submit\">Delete <i class=\"fa fa-trash\" aria-hidden=\"true\"></i></button>\n" +
                            "</form><br>\n";
                    header+="</div><br>\n";
                }
            } catch (SQLException e){
                e.printStackTrace();
            } catch (ClassNotFoundException t){
                t.printStackTrace();
            }
            header+="</div>\n";
            header+="</div>\n";
        }

        header+="</div>\n\n"; //close rows
        return header;
    }



    String footer(){
        String footer = "</body>\n";
        footer+="<script>\n";
        /*
        -----------------------------------------------------------------------
                JavaScript
         */
        if(projectID.size()>0){
            footer+="window.onload = function() {\n";
            for(int i = 0; i < projectID.size(); i++){
                footer+="document.getElementById(\'"+projectID.get(i)+"_newItem\').addEventListener(\"click\", function(){ document.getElementById(\""+projectID.get(i)+"_hiddendiv\").classList.toggle(\"show\"); });\n";
            }
            footer+="};\n\n";
        }

        if(projectID.size()>0){
            footer+="document.getElementById(\"defaultOpen\").click();\n\n";
        }

        footer+="function changeProject(evt, cityName) {\n";
        footer+="   var i, tabcontent, tablinks;\n";
        footer+="   tabcontent = document.getElementsByClassName(\"tabcontent\");\n";
        footer+="   for (i = 0; i < tabcontent.length; i++) {\n" +
                "       tabcontent[i].style.display = \"none\";\n" +
                "   }\n";
        footer+="   tablinks = document.getElementsByClassName(\"tablinks\");\n";
        footer+="   for (i = 0; i < tablinks.length; i++) {\n" +
                "       tablinks[i].className = tablinks[i].className.replace(\" active\", \"\");\n" +
                "   }\n";
        footer+="   document.getElementById(cityName).style.display = \"block\";\n";
        footer+="   evt.currentTarget.className += \" active\";\n";
        footer+="};\n\n";
        footer+="function loadNewProjectForm(){\n" +
                "    document.getElementById('newProjectDiv').classList.toggle('show');\n" +
                "};\n\n" +
                "function validateNewProjectForm(){\n" +
                "   var x = document.forms[\"newProjectForm\"][\"newProject\"].value;\n" +
                "   if (x == \"\") {\n" +
                "       document.getElementById('newProjectError').innerHTML=\"Project Name cannot be empty\";\n" +
                "       return false;\n" +
                "   }\n" +
                "};\n\n";
        footer+="function shareFunction(input){\n" +
                "var text = document.getElementById(input);\n" +
                "text.select();\n" +
                "document.execCommand(\"copy\");\n" +
                "alert(\"Copied link to clipboard: \"+ text.value);" +
                "}\n\n";
        footer+="function validateNewMilestoneForm(projectID){\n" +
                "   var title = \"postMilestoneForm\"+projectID;\n" +
                "   var x = document.forms[title][\"pmTitle\"].value;\n" +
                "   var c = document.forms[title][\"pmDay\"].value;\n" +
                "   var v = document.forms[title][\"pmMonth\"].value;\n" +
                "   var b = document.forms[title][\"pmYear\"].value;\n" +
                "   if (x == \"\" || c == \"\" || v == \"\" || b == \"\") {\n" +
                "       var ele = \"me_\"+projectID;" +
                "       document.getElementById(ele).innerHTML=\"Milestone Title, Day, Month and Year cannot be empty\";\n" +
                "       return false;\n" +
                "   }\n" +
                "};\n" +
                "function validateDelete(){\n" +
                "   var r = confirm(\"Are you sure you want to permanently delete milestone?\");\n" +
                "   if(r==true){return true;} else { return false; }\n" +
                "};\n\n" +
                "function validateDeleteProject(){\n" +
                "   var r = confirm(\"Are you sure you want to permanently delete project and milestones?\");\n" +
                "   if(r==true){return true;} else { return false; }\n" +
                "};\n\n" +
                "" +
                "function cancelEdit(input){\n" +
                "   document.getElementById(\"edit_\"+input).classList.remove('show');\n" +
                "   document.getElementById(\"edit_\"+input).classList.add('hidden');\n" +
                "   document.getElementById(\"milestone_\"+input).classList.remove('hidden');\n" +
                "   document.getElementById(\"milestone_\"+input).classList.add('milestone');\n" +
                "};\n" +
                "function submitEdit(input){\n" +
                "   document.getElementById(\"edit_\"+input).classList.remove('hidden');\n" +
                "   document.getElementById(\"edit_\"+input).classList.add('milestone');\n" +
                "   document.getElementById(\"milestone_\"+input).classList.remove('milestone');\n" +
                "   document.getElementById(\"milestone_\"+input).classList.add('hidden');\n" +
                "};\n" +
                "function validateImprove(input){\n" +
                "var divname = input+\"_edit_form\";\n" +
                "var x = document.forms[divname][\"improveTitle\"].value;\n" +
                "var c = document.forms[divname][\"improveDay\"].value;\n" +
                "var v = document.forms[divname][\"improveMonth\"].value;\n" +
                "var b = document.forms[divname][\"improveYear\"].value;\n" +
                "   if (x == \"\" || c == \"\" || v == \"\" || b == \"\") {\n" +
                "       var div2 = \"edit_error_\"+input;\n" +
                "       document.getElementById(div2).innerHTML=\"Milestone Title, Day, Month and Year cannot be empty\";\n" +
                "       return false;" +
                "   }\n" +
                "}\n";
        footer+="</script>\n";
        footer+="</html>\n";
        return footer;
    }
}
