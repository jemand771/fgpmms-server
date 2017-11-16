package tools.external;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

import things.Image;
import things.Mark;
import things.MarkEvent;
import things.SQLHandler;
import things.User;
import tools.MarkAverager;
import tools.StatementBuilder;

public class StupidTester {

	// static String dbip = "192.168.178.7";
	static String dbip = "127.0.0.1";
	static String dbport = "3306";
	static String dbname = "bell";
	static String url = "jdbc:mysql://" + dbip + ":" + dbport + "/" + dbname;
	static String username = "java";
	static String password = "password";

	public static void main(String[] args) throws SQLException, IOException{


		System.out.println(System.getProperty("user.dir"));
		System.out.println("Hello");

		System.out.print("Connecting database...");
		Connection connection;
		try {
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("OK!");
		} catch (SQLException e) {
			System.out.println("FAILED! :(");
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		SQLHandler handler = new SQLHandler(connection, true);
		
		System.out.println("Executing madness!");
		
		HashMap<String, String> map = new HashMap<>();
		map.put("id", "123");
		map.put("firstname", "max");
		map.put("lastname", "mustermann");
		
//		System.out.println(new StatementBuilder("users").buildInsert(map));
		
		
//		System.out.println(UUID.randomUUID());
		
//		System.out.println("hello".substring(0, 2));
		
		UUID me_uuid = UUID.fromString("3e98161f-0ffa-4d90-8ad7-addc4f807bc2");
		UUID lukas_uuid = UUID.fromString("5f044e6d-9342-47e5-9abf-14d8aa74260e");
		
		User me = new User(handler, me_uuid);
		User lukas = new User(handler, lukas_uuid);

		File file = new File("glowboard.PNG");
		FileInputStream imageInFile = new FileInputStream(file);
		byte imageData[] = new byte[(int) file.length()];
		imageInFile.read(imageData);

		// Converting Image byte array into Base64 String
//		String imageDataString = Base64.getEncoder().encodeToString(imageData);
//		long time = System.currentTimeMillis();
//		for(int i = 0; i < 1000; i++){
//			System.out.println(i + 1 + " / " + 1000);
//			Image img = new Image(handler, Image.upload(handler, imageDataString, "PNG"));
//		}
//		time = System.currentTimeMillis() - time;
//		System.out.println(time);
		
//		MarkEvent event = new MarkEvent(handler, 1);
//		MarkEvent event2 = new MarkEvent(handler, 2);
//		MarkAverager avg = new MarkAverager();
//		
//		Mark m = new Mark(me, event, handler);
//		m.setPercentage(100);
//		avg.addMark(m);
//		Mark m2 = new Mark(lukas, event2, handler);
//		m2.setPercentage(55);
//		avg.addMark(m2);
//		System.out.println(avg.getPercentage());
	}
}
