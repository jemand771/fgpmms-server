package tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import things.SQLHandler;

public class UserTools {

	SQLHandler handler;
	
	public UserTools(SQLHandler handler) {
		this.handler = handler;
	}
	
	public UUID getUUIDfomName(String username) {

		String query = "SELECT uuid FROM users WHERE textid = '" + username + "'";
		ResultSet set = handler.executeQuerry(query);
		if (set == null)
			return null;
		try {
			if(!set.next()) return null;
			return UUID.fromString(set.getString("uuid"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
