package application.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBd {
	
	public static Connection connection;
	

	
	public static Connection connectToDatabase() {
    	try {
    		String url = "jdbc:mysql://localhost:3306/ensao";
    		String username = "root";
    		String password = "";
    		connection = DriverManager.getConnection(url, username, password);
    	} catch (SQLException e) {
    		System.out.println("Error connecting to database.");
    		e.printStackTrace();
    	}
    	
    	return connection;
    }
}
