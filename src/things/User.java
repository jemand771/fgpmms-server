package things;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class User {


	private UUID user;
	private SQLHandler handler;
	
	
	public User(SQLHandler handler, UUID uuid){
		this.handler = handler;
		user = uuid;		
	}
	
	
	public ArrayList<Mark> getAllMarksIn(Group group) throws SQLException{
		
		ArrayList<Mark> list = new ArrayList<>();
		String sql = "SELECT marks.id, markevents.name, marks.percentage, marks.user, markevents.id" + 
		" FROM markevents, marks WHERE marks.event = markevents.id"
		+ " AND marks.user = "
		+ quote(UUID())
		+ " AND markevents.group_id = "
		+ group.getID()
		+ ";";
		
		ResultSet set = handler.executeQuerry(sql);
		while(set.next()) {
			User user = new User(handler, this.user);
			MarkEvent event = new MarkEvent(handler, set.getInt("markevents.id"));
			Mark mark = new Mark(user, event, handler);
			list.add(mark);
		}
		return list;
	}
	
	public boolean validatePassword(String password) throws SQLException{
		
		String sql = "SELECT * FROM passwords WHERE user_id = " + quote(UUID()) + " AND password = " + quote(password);
		ResultSet set = handler.executeQuerry(sql);
		if(set.next()) return true;
		return false;
	}
	
	public boolean isMemberIn(Group group) throws SQLException{
		
		String sql = "SELECT * FROM gms WHERE user_id = " + quote(UUID()) + " AND group_id = " + group.getID();
		ResultSet set = handler.executeQuerry(sql);
		if(set.next()) return true;
		return false;
	}
	
	public void joinGroup(Group group) throws SQLException{
		if(isMemberIn(group)) return;
		String sql = "INSERT INTO gms (user_id, group_id) VALUES (" + quote(UUID()) + ", " + group.getID() + ")";
		handler.execute(sql);
	}
	
	public void leaveGroup(Group group) throws SQLException{
		if(!isMemberIn(group)) return;
		String sql = "DELETE FROM gms WHERE user_id = " + quote(UUID()) + " AND group_id = " + group.getID();
		handler.execute(sql);
	}
	
	private String quote(String text){
		return "'" + text + "'";
	}
	
	public String UUID(){
		return user.toString();
	}
	
	public Token getToken(){
		Token token;
		while(true){
			token = new Token(handler, UUID.randomUUID().toString(), this);
			if(!token.exists()) break;
		}
		token.activate();
		return token;
	}

	public boolean isSuspended() throws SQLException{
		String sql = "SELECT * FROM users WHERE uuid = " + quote(UUID());
		ResultSet set = handler.executeQuerry(sql);
		set.next();
		if(set.getBoolean("suspended") == false) return false;
		return true;
	}
	
	public boolean isActivated() throws SQLException{
		String sql = "SELECT * FROM users WHERE uuid = " + quote(UUID());
		ResultSet set = handler.executeQuerry(sql);
		set.next();
		if(set.getBoolean("activated") == false) return false;
		return true;		
	}
	
	public boolean isReady() throws SQLException{
		return (!isSuspended() && isActivated());
	}
	
	public void suspend(){
		String sql = "UPDATE users SET suspended = true WHERE uuid = " + quote(UUID());
		handler.execute(sql);
	}
	
	public void activate(){
		String sql = "UPDATE users SET activated = true WHERE uuid = " + quote(UUID());
		handler.execute(sql);
	}
	
	public void unSuspend(){
		String sql = "UPDATE users SET suspended = false WHERE uuid = " + quote(UUID());
		handler.execute(sql);
	}
	
	public void flagDelete(){
		String sql = "UPDATE users SET delete_flag = true WHERE uuid = " + quote(UUID());
		handler.execute(sql);
	}
	
	public String getFirstName() throws SQLException{
		String sql = "SELECT * FROM users WHERE uuid = " + quote(UUID());
		ResultSet set = handler.executeQuerry(sql);
		set.next();
		return set.getString("firstname");
	}
	
	public String getLastName() throws SQLException{
		String sql = "SELECT * FROM users WHERE uuid = " + quote(UUID());
		ResultSet set = handler.executeQuerry(sql);
		set.next();
		return set.getString("lastname");
	}
	
	public void deleteAllTokens(){
		String sql = "DELETE FROM tokens WHERE user = " + quote(UUID());
		handler.execute(sql);
	}
}