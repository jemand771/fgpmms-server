package things;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLHandler {

	private Connection conn;
	boolean debug = false;

	public SQLHandler(Connection conn){
		this.conn = conn;
	}
	
	public SQLHandler(Connection conn, boolean debug){
		this.conn = conn;
		this.debug = debug;
	}
	
	
	public ResultSet executeQuerry(String query) {
		if(debug) System.out.println("Executing " + query);
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			// stmt.close();
			return rset;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean execute(String query){
		if(debug) System.out.println("Executing " + query);
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			return true;
			// stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
}
