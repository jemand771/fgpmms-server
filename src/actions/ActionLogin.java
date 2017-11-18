package actions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import org.json.simple.JSONObject;

import things.SQLHandler;
import things.Token;
import things.User;
import tools.UserTools;

public class ActionLogin {

	private DataOutputStream out;	
	private SQLHandler handler;
	
	public ActionLogin(DataOutputStream out, SQLHandler handler) {
		this.out = out;
		this.handler = handler;
	}
	
	public void run(JSONObject mainObject) throws IOException, SQLException {
		JSONObject userobj = (JSONObject) mainObject.get("user");
		String username = (String) userobj.get("username");
		String password = (String) userobj.get("password");
		
		UUID uid = new UserTools(handler).getUUIDfomName(username);
		
		if(uid == null){ //user not found or error
			JSONObject response = new JSONObject();
			response.put("status", "404");
			JSONObject error = new JSONObject();
			error.put("type", "notfound;");
			error.put("what", "user");
			response.put("error", error);
			out.writeBytes(response.toJSONString());
			out.writeBytes("\r\n");
			out.flush();
		}
		
		User user = new User(handler, uid);
		if(!user.validatePassword(password)){
			
			JSONObject response = new JSONObject();

			response.put("status", "404");
			JSONObject error = new JSONObject();
			error.put("type", "user");
			error.put("what", "password");
			response.put("error", error);
			out.writeBytes(response.toJSONString());
			out.writeBytes("\r\n");
			out.flush();
			
			return; // RETURN FUCKING RETURN!!!!! xD
		}
		
		JSONObject response = new JSONObject();
		Token token = user.getToken();
		if(token == null){
			response.put("status", "500");
			JSONObject error = new JSONObject();
			error.put("type", "server");
			error.put("what", "token");
			response.put("error", error);
			out.writeBytes(response.toJSONString());
			out.writeBytes("\r\n");
			out.flush();
		}
		response.put("status", "200");
		JSONObject r = new JSONObject();
		r.put("token", token.getString());
		
		response.put("response", r);
		
		out.writeBytes(response.toJSONString());
		out.writeBytes("\r\n");
		out.flush();
	}
}
