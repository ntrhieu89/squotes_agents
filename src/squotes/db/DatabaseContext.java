package squotes.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DatabaseContext {
	private static final String SQL_URL = "jdbc:mysql://localhost/tempquotes?useAffectedRows=true";			// url for mysql
	private static final String USERNAME = "hieunguyen";			// mysql username
	private static final String PASSWORD = "hieunguyen123";		// mysql password
	
	private static final Logger log = Logger.getLogger(DatabaseContext.class.getName());
	
	public DatabaseContext() {
		// loads the JDBC driver
		String driverName = "com.mysql.jdbc.Driver"; 	// driver for MySql
		try {
			Class.forName(driverName); 
		} catch (ClassNotFoundException ex) {
			log.error("JDBC driver for MySQL not found");
		}
	}
	
	private Connection openConnection() {
		Connection conn = null;
		
		// creates a connection to the database
		try {
			conn = DriverManager.getConnection(SQL_URL, USERNAME, PASSWORD);
			conn.setAutoCommit(true);
		} catch (SQLException ex) {
			log.error(ex.toString());
		}
		
		return conn;
	}
	
	private void close(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error(e.toString());
		}
	}
	
	public TempQuote getUnpostedQuote() {
		Connection conn = null;
		
		try {
			conn = openConnection();
			
			if (conn == null)
				return null;
			
			Statement stmt = conn.createStatement();
			String query = "SELECT * from temp WHERE isposted=0";
			ResultSet rs = null;
			
			rs = stmt.executeQuery(query);
			
			if (rs == null)
				return null;		
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String content = rs.getString(2);
				String author = rs.getString(3);
				boolean isPosted = rs.getBoolean(4);
				
				return new TempQuote(id, content, author, isPosted);
			}
		} catch (SQLException e) {
			log.error(e.toString());
		} finally {
			close(conn);
		}
		
		return null;
	}
	
	public boolean setAQuoteAsPosted(int id) {
		Connection conn = null;
		
		try {
			conn = openConnection();
			
			Statement stmt = conn.createStatement();
			String query = "UPDATE temp set isposted=1 where id='" + id + "'";
			
			int affectedRow = stmt.executeUpdate(query);
			
			if (affectedRow == 1)			
				return true;
			else 
				return false;
		} catch (SQLException e) {
			log.error(e.toString());
			e.printStackTrace();
		} finally {
			close(conn);
		}
		
		return false;
	}
}