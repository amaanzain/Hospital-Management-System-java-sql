
package project;
import java.sql.*;

/**
 *
 * @author zaina
 */
public class ConnectionProvider {
    public static Connection getCon(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/hospitalmanagementsystem","root","1234");
            return con;
        } catch (Exception e) {
            return null;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
} 
    
    
//    Connection c;
//    Statement s;
//    public ConnectionProvider () {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            c = DriverManager.getConnection("jdbc:mysql:///hospitalmanagementsystem", "root", "1234");
//            s = c.createStatement();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//}
//
