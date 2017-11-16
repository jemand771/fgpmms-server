package Actions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import org.json.simple.JSONObject;

import things.Image;
import things.SQLHandler;
import things.Token;
import things.User;
import tools.UserTools;

public class Actions {

	
	private DataOutputStream out;	
	private SQLHandler handler;
	
	public Actions(DataOutputStream out, SQLHandler handler) {
		this.out = out;
		this.handler = handler;
	}
	
	public void login(JSONObject mainobj) throws IOException, SQLException{
		
		JSONObject userobj = (JSONObject) mainobj.get("user");
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
		
		//TODO password check
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

	public void upload(JSONObject request, Token token) throws IOException, SQLException{
		
		if(!checkTokenValidity(token)) return;
		
		String ext = (String) request.get("fileext");
		String str = (String) request.get("filebase64");
		String name = (String) request.get("filename");
		
		Image img = new Image(handler, Image.upload(handler, str, name, ext, token));
		
		JSONObject response = new JSONObject();
		response.put("status", "200");
		JSONObject details = new JSONObject();
		details.put("id", img.getID());
	}
	
	private boolean checkTokenValidity(Token token) throws IOException{
		
		if(token.exists()){
			return true;
		}
		
		JSONObject mainresponse = new JSONObject();
		mainresponse.put("status", "403");

		JSONObject error = new JSONObject();
		error.put("type", "notfound;");
		error.put("what", "token");
		mainresponse.put("error", error);
		
		out.writeBytes(mainresponse.toJSONString());
		out.writeBytes("\n\r");
		out.flush();
		
		return false;
	}
}
