package things;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mark {

	private User user;
	private SQLHandler handler;
	private MarkEvent event;
	
	public Mark(User user, MarkEvent event, SQLHandler handler){
		this.user = user;
		this.handler = handler;
		this.event = event;
	}

	
	public int getWeightCode() throws SQLException{
		String sql = "SELECT * FROM marks, markevents WHERE user = '"
					+ user.UUID() + "' AND marks.event = " + event.getID()
					+ " AND marks.event = markevents.id";
		ResultSet set = handler.executeQuerry(sql);
		set.next();
		return set.getInt("weight");
	}
	
	public int getPercentage() throws SQLException{ // ALERT! check if exists before just getting stuff!!
		String sql = "SELECT * FROM marks WHERE user = '" + user.UUID() + "' AND event = " + event.getID();
		ResultSet set = handler.executeQuerry(sql);
		if(!set.next()) return -1;
		return set.getInt("percentage");
	}
	
	public boolean isSet() throws SQLException{
		String sql = "SELECT * FROM marks WHERE user = '" + user.UUID() + "' AND event = " + event.getID();
		ResultSet set = handler.executeQuerry(sql);
		if(!set.next()) return false;
		return true;
	}
	
	public void setPercentage(int percentage) throws SQLException{
		String sql = "";
		if(isSet()){
			// need to update cuz set already
			sql = "UPDATE marks SET percentage = " + percentage + " WHERE user = '" + user.UUID() + "' AND event = " + event.getID();
		}
		else{
			// insert it
			sql = "INSERT INTO marks (user, event, percentage) VALUES ('" + user.UUID() + "', " + event.getID() + ", " + percentage + ")";
		}
		handler.execute(sql);
	}
	
	public void setByPoints(double points){
		//TODO linked calculation to MarkEvent with maxPoints
	}
}
