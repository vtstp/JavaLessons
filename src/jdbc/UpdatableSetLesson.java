package jdbc;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.sql.*;

/**
 * Created by max on 2/8/17.
 */
public class UpdatableSetLesson {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XMLStreamException, TransformerException, XPathExpressionException, SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/Lessons";
        String username = "root";
        String password = "1";
        Class.forName("com.mysql.jdbc.Driver");
        try(Connection conn = DriverManager.getConnection(url, username, password);
            Statement stat = conn.createStatement()) {
            stat.execute("drop table IF EXISTS Books");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))");
            stat.executeUpdate("INSERT INTO Books (name) VALUES ('Inferno')");
            stat.executeUpdate("INSERT INTO Books (name) VALUES ('DaVinchi Code')");
            stat.executeUpdate("INSERT INTO Books (name) VALUES ('Solomon key')");

            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement preparedStatement = conn.prepareStatement("select * from Books", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("select * from Books");
            while(resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getString("name"));
            }
            resultSet.last();
            resultSet.updateString("name", "angels and demons");
            resultSet.updateRow();

            resultSet.moveToInsertRow();
            resultSet.updateString("name", "inserted");
            resultSet.insertRow();
            resultSet.moveToCurrentRow();

            resultSet.absolute(2);
            resultSet.deleteRow();

            resultSet.beforeFirst();
            while(resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getString("name"));
            }
        }
    }
}
