import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dto.Match.MatchDetail;
import dto.MatchList.MatchList;
import dto.MatchList.MatchReference;

public class SaveToFile {
	FileWriter writer;
	BufferedWriter bf;
	String path = "/home/chris/riotData/";
	public SaveToFile(String aPath)
	{
		path = aPath;
	}
	public SaveToFile(String user, String fileName, int fileNumber)
	{
		fileName = path + user + "/Completed/" + fileName + Integer.toString(fileNumber) +".json";
		try {
			writer = new FileWriter(fileName);
			bf = new BufferedWriter(writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public boolean saveFileData(Long [] listOfMatches) throws IOException//String filename, int condition
	{
		Gson gson = new Gson();
		String json = gson.toJson(listOfMatches);
		bf.write(json);
		bf.close();
		
		return true;
	}
	public boolean saveFileData(MatchDetail [] matches) throws IOException
	{
		Gson gson = new Gson();
		String json = gson.toJson(matches);
		bf.write(json);
		bf.close();
		
		return true;
	}
	public boolean saveFileData(Coords [] listOfCoords) throws IOException//String filename, int condition
	{
		JsonParser parser = new JsonParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(listOfCoords);
		JsonElement el = parser.parse(json);
		String jsonPretty = gson.toJson(el);
		bf.write(jsonPretty);
		bf.close();
		
		return true;
	}
		
}
