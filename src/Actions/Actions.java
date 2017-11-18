package actions;

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
		
		new ActionLogin(out, handler).run(mainobj);
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
