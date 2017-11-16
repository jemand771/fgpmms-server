package things;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import tools.StatementBuilder;

public class MarkEvent {

	private int id;
	private SQLHandler handler;
	
	public MarkEvent(SQLHandler handler, int id){
		this.handler = handler;
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
	
	public Mark getUsersMark(User user){
		return new Mark(user, this, handler);
	}
	
	public static int register(SQLHandler handler, Group group, String name, String descTeacher, String descStudent, int weight, boolean isExam, boolean hasCustomMarks, long date, long dateAnnounced, long dateEditable) throws SQLException{
		
		HashMap<String, String> map = new HashMap<>();
		map.put("group_id", String.valueOf(group.getID()));
		map.put("name", name);
		map.put("is_exam", isExam ? "1" : "0");
		map.put("custom", hasCustomMarks ? "1" : "0");
		map.put("weight", String.valueOf(weight));
		map.put("date", String.valueOf(date));
		map.put("announced", String.valueOf(dateAnnounced));
		map.put("editable", String.valueOf(dateEditable));
		map.put("desc_teacher", descTeacher);
		map.put("desc_student", descStudent);
		
		return register(handler, map);
	}
	
	public static int register(SQLHandler handler, HashMap<String, String> properties) throws SQLException{

		String sql = new StatementBuilder("markevents").buildInsert(properties);
		handler.execute(sql);
		sql = "SELECT LAST_INSERT_ID();";
		ResultSet set = handler.executeQuerry(sql);
		set.next();
		int id = set.getInt(1);
		System.out.println("NEXT ID: " + id);
		return id;
	}
}
