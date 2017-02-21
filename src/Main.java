import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.javatuples.Pair;

import dto.Match.Event;
import dto.Match.Frame;
import dto.Match.MatchDetail;
import dto.Match.Timeline;
import dto.MatchList.MatchList;
import dto.MatchList.MatchReference;
import main.java.riotapi.RiotApiException;

public class Main {
	//14870,14980
	
	static Pair<Integer,Integer> MapMaxCoords = new Pair<Integer,Integer>(14870,14980);
	static Pair<Double,Double> PictureMaxCoords = new Pair<Double,Double>(1000.0,1000.0);
	
	public static void main(String[] args) throws IOException, RiotApiException {
		String pathName = "/home/chris/riotData/";
		GetData dataFetch = new GetData(pathName);
		String aUser = "Latvian Potato";
		dataFetch.setUser(aUser);
		drawMap(dataFetch, pathName, aUser);
	}

	
	private static void drawMap(GetData dataFetch, String pathName, String aUser) throws IOException, RiotApiException
	{
		List<Coords> coordsList = dataFetch.getCoordData(true);
		if(coordsList == null)
		{
			System.out.println("No coords collected");
			return;
		}
        JFrame frame = new JFrame("HeatMap");
		DrawImage aImage = new DrawImage(PictureMaxCoords.getValue0().intValue(),PictureMaxCoords.getValue1().intValue());
		aImage.drawPicture(-120, -120, MapMaxCoords,PictureMaxCoords, coordsList);
	    aImage.saveToFile(pathName+aUser+"/Completed/heatmap.png");
        frame.add(aImage);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}



}
