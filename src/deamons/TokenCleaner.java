package deamons;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimerTask;

public class TokenCleaner extends TimerTask {

	
	Connection conn;
	long lifespan;
	boolean debug;
	
	public TokenCleaner(Connection conn, long lifespan, boolean debug) {

		this.conn = conn;
		this.lifespan = lifespan;
		this.debug = debug;
	}
	
	public TokenCleaner(Connection conn, long lifespan) {
		this(conn, lifespan, false);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		long current = System.currentTimeMillis();
		current -= lifespan;
		
		String sql = "DELETE FROM tokens WHERE last_used < " + current;
		if(debug) System.out.println("now deleting tokens older than " + current);
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
