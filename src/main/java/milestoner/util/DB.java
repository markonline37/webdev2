package milestoner.util;
import com.sun.rowset.CachedRowSetImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DB {
    //Using WAMP to host the MySQL database
    public static String url = "localhost:9001";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "";
    private static String DB_HOST = "localhost";
    private static String DB_PORT = "3306";
    private static String DB_NAME = "milestonerdb";
    private static String DB_CONNECTION = "jdbc:mysql://"+DB_HOST+":"+DB_PORT+"/" + DB_NAME;

    private static Connection conn = null;

    public static void connect() throws SQLException, ClassNotFoundException {
        //https://stackoverflow.com/questions/30651830/use-jdbc-mysql-connector-in-intellij-idea - Li Rao's answer
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e){
            System.out.println("Error - no JDBC driver");
            throw e;
        }

        try {
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        }catch(SQLException e){
            System.out.println("Connection failed: " + e);
            throw e;
        }
    }

    public static void disconnect() throws SQLException {
        try {
            if(conn != null && !conn.isClosed()){
                conn.close();
            }
        } catch(SQLException e){
            throw e;
        }
    }

    //run update on database only - no return.
    public static void dbQuery(String s)throws SQLException, ClassNotFoundException{
        Statement stmt = null;
        try {
            connect();
            stmt = conn.createStatement();
            stmt.executeUpdate(s);
        } catch (SQLException e){
            throw e;
        }finally {
            if(stmt != null){
                stmt.close();
            }
        }
        disconnect();
    }

    //run query - get back string
    public static String dbString(String s) throws SQLException, ClassNotFoundException{
        Statement stmt = null;
        String query = null;
        try {
            connect();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()){
                query = rs.getString(1);
            }
        } catch(SQLException e){
            System.out.println(e);
            throw e;
        } finally {
            if(stmt !=null){
                stmt.close();
            }
        }
        disconnect();
        return query;
    }

    //run query - get back array
    public static List<String> dbArray(String sqlStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        List<String> query = new ArrayList<String>();
        try {
            connect();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt);
            while(rs.next()) {
                query.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Problem occurred at execute SQL statement: " + e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        disconnect();
        return query;
    }

    //run query - get back boolean
    public static boolean dbBoolean(String sqlStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        boolean b = false;
        try {
            connect();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt);
            b = rs.next()? true : false;

        } catch (SQLException e) {
            System.out.println("Problem occurred at execute SQL statement: " + e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        disconnect();
        return b;
    }

    //resultset
    public static ResultSet dbRS(String queryStmt) throws SQLException, ClassNotFoundException
    {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            connect();
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(queryStmt);
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQueryRS operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            disconnect();
        }
        return crs;
    }
}
