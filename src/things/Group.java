package things;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Group {

	private int id;
	private SQLHandler handler;
	
	public Group(int id, SQLHandler handler){
		this.id = id;
		this.handler = handler;
	}
	
	public boolean exists() throws SQLException{
		
		String sql = "SELECT * FROM groups WHERE id = " + id;
		ResultSet set = handler.executeQuerry(sql);
		if(set.next()) return true;
		return false;
	}
	
	public ArrayList<User> getUsers() throws SQLException{
		
		ArrayList<User> result = new ArrayList<>();
		String sql = "SELECT * FROM gms WHERE group_id = " + id;
		ResultSet set = handler.executeQuerry(sql);
		while(set.next()){
			String uuid = set.getString("user_id");
			User user = new User(handler, UUID.fromString(uuid));
			result.add(user);
		}
		return result;
	}
	
	public int getID(){
		return id;
	}
}
