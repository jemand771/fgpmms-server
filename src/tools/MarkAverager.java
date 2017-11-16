package tools;

import java.sql.SQLException;
import java.util.ArrayList;

import things.Mark;

public class MarkAverager {

	/*
	 * WeightCodes:
	 * 1 - normal
	 * 2 - double
	 * 3 - half
	 * 4 - third
	 * 5 - half-of (sth like 50% of endyear)
	 */
	
	private ArrayList<Mark> marks;
	
	
	public MarkAverager(){
		marks = new ArrayList<>();
	}
	
	public void addMark(Mark mark){
		marks.add(mark);
		try {
			System.out.println(mark.getWeightCode());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getPercentage() throws SQLException{
		
		
		double normalSum = 0;
		double normalCount = 0;
		double specialSum = 0;
		double specialCount = 0;

		for(int i = 0; i < marks.size(); i++){
			Mark m = marks.get(i);
			switch(m.getWeightCode()){
			case 1:
				normalSum += m.getPercentage();
				normalCount++;
				break;
				
			case 2:
				normalSum += m.getPercentage();
				normalCount++;
				normalSum += m.getPercentage();
				normalCount++;
				break;
				
			case 3:
				normalSum += (m.getPercentage() / 2);
				normalCount += (1 / 2);
				break;
				
			case 4:
				normalSum += (m.getPercentage() / 3);
				normalCount += (1 / 3);
				break;
			
			case 5:
				specialSum += m.getPercentage();
				specialCount++;
				break;
			}
		}	


		double nAvg = normalCount == 0 ? 0 : normalSum / normalCount;
		double sAvg = specialCount == 0 ? 0 : specialSum / specialCount;
		
		if(normalCount == 0 && specialCount == 0) return -1;
		if(normalCount == 0) return (int) Math.round(sAvg);
		if(specialCount == 0) return (int) Math.round(nAvg);
		return (int) Math.round((nAvg + sAvg) / 2);
	}
	
	public int getSek1Mark(){
		return 0;
	}
	
	public int getSek2Mark(){
		return 0;
	}
}
