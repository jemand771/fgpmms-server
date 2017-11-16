package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import things.SQLHandler;

public class SettingsManager {

	private static int[] sek1MarkThresholds;
	private static int[] sek2MarkThresholds;
	private static HashMap<String, Integer> subjectIDMap;
	
	public static void retrieveFromDatabase(SQLHandler handler) throws SQLException{

		sek1MarkThresholds = new int[7];
		sek2MarkThresholds = new int[16];
		subjectIDMap = new HashMap<>();
		
		for(int i = 1; i < 7; i++){
			String sql = "SELECT * FROM settings_percentages_sek1 WHERE mark = " + i;
			ResultSet set = handler.executeQuerry(sql);
			set.next();
			sek1MarkThresholds[i] = set.getInt("percentage");
		}
		
		for(int i = 0; i < 16; i++){
			String sql = "SELECT * FROM settings_percentages_sek2 WHERE mark = " + i;
			ResultSet set = handler.executeQuerry(sql);
			set.next();
			sek2MarkThresholds[i] = set.getInt("percentage");
		}
		
		String subjQuery = "SELECT * FROM SUBJECTS";
		ResultSet subjSet = handler.executeQuerry(subjQuery);
		while(subjSet.next()){
			subjectIDMap.put(subjSet.getString("shortname"), subjSet.getInt("id"));
		}
	}
	
	public static int getSek1Threshold(int mark){
		return sek1MarkThresholds[mark];
	}
	
	public static int getSek2Threshold(int mark){
		return sek2MarkThresholds[mark];
	}
	
	public static int getSubjectID(String shortCode){
		return subjectIDMap.get(shortCode);
	}
}
