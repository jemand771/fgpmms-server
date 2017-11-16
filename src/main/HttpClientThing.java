package main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Actions.Actions;
import things.SQLHandler;
import things.Token;
import things.User;

public class HttpClientThing extends Thread {

	private Socket client;
	private SQLHandler handler;

	public HttpClientThing(SQLHandler handler, Socket client) {

		this.handler = handler;
		this.client = client;
	}

	public void run() {
		// Statement stmt;
		InputStream inp = null;
		BufferedReader brinp = null;
		DataOutputStream out = null;
		try {
			inp = client.getInputStream();
			brinp = new BufferedReader(new InputStreamReader(inp));
			out = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			return;
		}
		Actions actions = new Actions(out, handler);

		String line;
		while (true) {
			try {
				line = brinp.readLine();
				System.out.println("got " + line);
				if ((line == null) || line.equalsIgnoreCase("QUIT")) {
					client.close();
					break;
				} else {
					JSONParser parser = new JSONParser();
					JSONObject mainobj = (JSONObject) parser.parse(line);
					String action = (String) mainobj.get("action");

					if (action == "login") {
						actions.login(mainobj);
					} else {
						String tokenString = (String) mainobj.get("token");
						User tokenUser = Token.userBytoken(handler, tokenString);
						Token token = new Token(handler, tokenString, tokenUser);
						
						
						switch (action) {
						
						case "upload":
							actions.upload((JSONObject) mainobj.get("request"), token); 
							break;
						default:
							break;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Connection failiure, something went wrong.");
				return;
			} catch (ParseException e) {
				e.printStackTrace();
				System.out.println("Bad request");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.close();
			brinp.close();
			inp.close();
			System.out.println("Client disconnected peacefully");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
