package things;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

import tools.StatementBuilder;

public class Image {

	private String id;
	private SQLHandler handler;

	public Image(SQLHandler handler, String id) {
		this.id = id;
		this.handler = handler;
	}

	public static String upload(SQLHandler handler, String base64String, String name, String ext, Token token) throws SQLException, IOException {

		String uuid = UUID.randomUUID().toString();
		if (new Image(handler, uuid).exists())
			return upload(handler, base64String, name, ext, token);

		ext = ext.toLowerCase();
		byte[] imageByteArray = Base64.getDecoder().decode(base64String);
		File file = new File("images/" + uuid + "." + ext);
		file.createNewFile();
		FileOutputStream imageOutFile = new FileOutputStream(file);
		imageOutFile.write(imageByteArray);
		imageOutFile.close();

		HashMap<String, String> values = new HashMap<>();
		values.put("id", uuid);
		values.put("name", name);
		values.put("extension", ext);
		values.put("uploaded_at", String.valueOf(System.currentTimeMillis()));
		values.put("uploaded_by", token.getBoundUser().UUID());
		String sql = new StatementBuilder("images").buildInsert(values);
		handler.execute(sql);
		return uuid;
	}

	public boolean exists() throws SQLException {

		String sql = "SELECT * FROM images WHERE id = '" + id + "';";
		ResultSet set = handler.executeQuerry(sql);
		if (set.next())
			return true;
		return false;
	}

	public String getID() {
		return id;
	}

	public String getExtension() throws SQLException {

		String sql = "SELECT * FROM images WHERE id = '" + id + "';";
		ResultSet set = handler.executeQuerry(sql);
		set.next();
		return set.getString("extension");
	}

	public String getBase64Data() throws SQLException, IOException {
		File file = new File("images/" + id + "." + getExtension());

		FileInputStream imageInFile = new FileInputStream(file);
		byte imageData[] = new byte[(int) file.length()];
		imageInFile.read(imageData);
		imageInFile.close();

		String imageDataString = Base64.getEncoder().encodeToString(imageData);
		return imageDataString;
	}
}
