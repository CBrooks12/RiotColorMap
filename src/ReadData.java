import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import dto.Match.MatchDetail;

public class ReadData{
	Gson gson = new Gson();
	public List<Coords> getCoordDataFile(String fileName)
	{
		Coords[] obj = null;
		List<Coords> fullList = new ArrayList<Coords>();
		int fileNumber = 0;
		boolean pass = true;
		while(pass)
		{
			List<Coords> tempList = new ArrayList<Coords>();
			try {
				System.out.println("retrieving file "+fileNumber);
				obj = gson.fromJson(new FileReader(fileName+Integer.toString(fileNumber)+".json"), Coords[].class);
				fullList.addAll(Arrays.asList(obj));
				fileNumber++;
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				pass = false;
				e.printStackTrace();
			}

		}
		return fullList;
	}
	public List<?> getDataFile(String fileName)
	{
		//BufferedReader br = new BufferedReader(new FileReader(fileName+".json"));
		//convert the json string back to object	
		List<?> aList = null;
		boolean pass = false;
		try {
			Long [] obj = gson.fromJson(new FileReader(fileName+".json"), Long[].class);
			aList = Arrays.asList(obj);
			pass = true;
			return aList;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!pass)
		{
			List<MatchDetail> fullList = new ArrayList<MatchDetail>();
			int fileNumber = 0;
			while(!pass)
			{
				List<MatchDetail> tempList = new ArrayList<MatchDetail>();
				try {
					System.out.println("retrieving file "+fileNumber);
					MatchDetail [] obj = gson.fromJson(new FileReader(fileName+Integer.toString(fileNumber)+".json"), MatchDetail[].class);
					//Type collectionType = new TypeToken<Collection<MatchDetail>>(){}.getType();
					//Collection<MatchDetail> aCollection = gson.fromJson(br, collectionType);
					tempList = Arrays.asList(obj);
					fullList.addAll(tempList);
					fileNumber++;
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					pass = true;
					e.printStackTrace();
				} catch (JsonIOException e) {
					// TODO Auto-generated catch block
					pass = true;
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					pass = true;
					e.printStackTrace();
				}
			}
			return fullList;
		}
		return null;
	}
}
