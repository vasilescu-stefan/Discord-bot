package bot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


public class Database {
    
    private static final String URL = "jdbc:mysql://localhost:3306/discord_db";
    private static final String USER = "stefan";
    private static final String PASSWORD = "password";
    private static Connection connection = null;

    private Database(){}

    public static void createConnection(){
        try{
            if( connection != null )
                throw new SQLException("Connection already created!");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(){
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        return connection;
    }

    public static void insertArticle(Template template) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO discord_table (Titlu, Content, PageID) VALUES(?, ?, ?)");
            statement.setString(1, template.getTitle());
            if(template.getContent().length()>1900)
            {
                statement.setString(2, template.getContent().substring(0,1900));
            }
            else
            {
                statement.setString(2, template.getContent());
            }
            statement.setInt(3, template.getPageId());
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Template searchTitles(String title) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM discord_table WHERE Titlu = ?");
            statement.setString(1, title);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Template t=new Template();
                t.setTitle(result.getString("Titlu"));
                t.setContent(result.getString("Content"));
                t.setPageId(result.getInt("PageId"));
                return t;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
