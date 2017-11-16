package things.register;

import java.util.HashMap;

import things.Group;
import things.MarkEvent;
import things.SQLHandler;

public class MarkEventRegistrator {

	private SQLHandler handler;
	
	private Group group;
	private boolean isExam, customMarks;
	private int weight;
	private String descTeacher, descStudent, name;
	private long dateDue, dateAnnounce, dateEditable;
	
	private boolean registred = false;
	
	
	
	public MarkEventRegistrator(SQLHandler handler){
		this.handler = handler;
	}

	
	// setters
	public void setCustomMarks(boolean customMarks) {
		this.customMarks = customMarks;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setDescTeacher(String descTeacher) {
		this.descTeacher = descTeacher;
	}

	public void setDescStudent(String descStudent) {
		this.descStudent = descStudent;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDateDue(long dateDue) {
		this.dateDue = dateDue;
	}

	public void setDateAnnounce(long dateAnnounce) {
		this.dateAnnounce = dateAnnounce;
	}

	public void setDateEditable(long dateEditable) {
		this.dateEditable = dateEditable;
	}

	public void setIsExam(boolean isExam){
		this.isExam = isExam;
	}
	
	public void setGroup(Group group){
		this.group = group;
	}
	
	
	public void register(){
		if(registred) return;
		// register event
		HashMap<String, String> values = new HashMap<>();
	}
	//TODO more methods
	//TODO how to get auto-assignes id once registered
}
