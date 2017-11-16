package tools;

import java.util.ArrayList;
import java.util.HashMap;

public class StatementBuilder {
	
	String tablename;
	boolean insertSemicolon = true;
	
	public StatementBuilder(String tablename){
		this.tablename = tablename;
	}
	
	public StatementBuilder(String tablename, boolean insertSemicolon){
		this.tablename = tablename;
		this.insertSemicolon = insertSemicolon;
	}
	
	
	public String buildInsert(HashMap<String, String> values){
		
		String sql = "INSERT INTO " + tablename;
		String c1 = "(";
		String c2 = "(";
		for(int i = 0; i < values.size(); i++){
			if(i != 0){
				c1 += ", ";
				c2 += ", ";
			}
			String key = new ArrayList<String>(values.keySet()).get(i);
			String value = values.get(key);
			// c1 += "'" + key + "'";
			c1 += key;
			c2 += "'" + value + "'";
		}
		c1 += ")";
		c2 += ")";
		
		sql += " " + c1 + " VALUES " + c2;
		if(insertSemicolon) sql += ";";
		return sql;
	}
}
