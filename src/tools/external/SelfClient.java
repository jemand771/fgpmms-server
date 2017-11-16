package tools.external;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SelfClient {

	private static Socket socket;
	private static Scanner sc;
	private static PrintWriter pw;
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		
		socket = new Socket("localhost", 8080);
	
		sc = new Scanner(socket.getInputStream());
        pw = new PrintWriter(socket.getOutputStream(), true);

        
        System.out.println(getUserToken("willy.hille", "123456"));
        

        pw.println("QUIT");
	}
	
	public static String getUserToken(String username, String password){
		
		JSONObject obj = new JSONObject();
        obj.put("action", "login");
        JSONObject userinfo = new JSONObject();
        userinfo.put("username", username);
        userinfo.put("password", password);
        obj.put("user", userinfo);
        
        pw.println(obj.toJSONString());
        String line = sc.nextLine();
        System.out.println(line);
        try {
			JSONObject mainresponse = (JSONObject) new JSONParser().parse(line);
			String status = (String) mainresponse.get("status");
			if(status.equalsIgnoreCase("ok")){
				JSONObject response = (JSONObject) mainresponse.get("response");
				String token = (String) response.get("token");
				if(token.length() == 36) return token;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
