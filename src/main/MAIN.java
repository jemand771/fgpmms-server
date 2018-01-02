package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import java.util.UUID;

import deamons.TokenCleaner;
import things.SQLHandler;
import things.User;

public class MAIN {

	static String dbip = "";
	static String dbport = "";
	static String dbname = "";
	static String url = "";
	static String username = "";
	static String password = "";
	static boolean acceptingConnections = true;

	public static void main(String args[]) throws SQLException, IOException {

		
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));

		username = prop.getProperty("user");
		password = prop.getProperty("pass");
		dbip = prop.getProperty("dbip");
		dbport = prop.getProperty("dbport");
		dbname = prop.getProperty("dbname");
		url = "jdbc:mysql://" + dbip + ":" + dbport + "/" + dbname;
		
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
		
		// load settings from DB server
		SettingsManager.retrieveFromDatabase(handler);
		
		
		// auto-delete tokens that are not used
		System.out.print("Starting TokenCleaner...");
		Timer tokenDeleter = new Timer(true);
		tokenDeleter.schedule(new TokenCleaner(connection, 900000), 0, 60000); // 15
		System.out.println("OK!"); // minute
									// validity,
									// 1
									// minute
									// check
									// ->
									// 14-16m

		System.out.print("Starting HTTP Server...");

		Thread httpThread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					ServerSocket serverSocket = new ServerSocket(8080);

					System.out.println("OK!");
					while (acceptingConnections) {

						Socket client = serverSocket.accept();
						new HttpClientThing(handler, client).start();
						System.out.println("New client! " + client.getRemoteSocketAddress().toString());
					}

					serverSocket.close();
				} catch (IOException e) {
					System.out.println("FAILED! :(");
					e.printStackTrace();
				}

			}
		});
		httpThread.start();
		// wait for http thread
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// oops
		}
		System.out.println("All Threads started.");
		// connection.close();

		boolean thing = true;
		while(thing);
		
		// terminate
		System.out.print("Terminating HTTP Thread... ");
		httpThread.interrupt();
		System.out.println("OK!");
		
		UUID me_uuid = UUID.fromString("3e98161f-0ffa-4d90-8ad7-addc4f807bc2");
		UUID lukas_uuid = UUID.fromString("5f044e6d-9342-47e5-9abf-14d8aa74260e");
		
		@SuppressWarnings("unused")
		User me = new User(handler, me_uuid);
		
		
//		System.out.println("Exiting.");
//		System.exit(0);
	}
}