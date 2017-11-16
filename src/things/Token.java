package things;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Token {

	private SQLHandler handler;
	private String token;
	private User boundUser;
	
	public Token(SQLHandler handler, String token, User boundUser) {
		this.handler = handler;
		this.token = token;
		this.boundUser = boundUser;
	}
	
	public User getBoundUser(){
		return boundUser;
	}
	
	public static User userBytoken(SQLHandler handler, String token) throws SQLException{
		
		String sql = "SELECT * FROM tokens WHERE token = '" + token + "';";
		ResultSet set = handler.executeQuerry(sql);
		set.next();
		return new User(handler, UUID.fromString(set.getString("user")));
	}
	
	public String getString(){
		return token;
	}
	
	public void use(){
		if(!this.exists()) return;
		String stamp = String.valueOf(System.currentTimeMillis());
		String query = "UPDATE tokens SET last_used = " + stamp + " WHERE token = '" + token + "'";
		handler.execute(query);
	}
	
	public boolean exists(){
		String query = "SELECT * FROM tokens WHERE token = '" + token + "'";
		ResultSet set = handler.executeQuerry(query);
		try {
			if(set.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void activate(){
		String stamp = String.valueOf(System.currentTimeMillis());
		System.out.println("Token: " + token);
		String query = "INSERT INTO tokens (user, token, created, last_used) VALUES (" + "'" + boundUser.UUID() + "'" + ", '" + token + "', " + stamp + ", " + stamp + ");";
		handler.execute(query);
		use();
	}
}
