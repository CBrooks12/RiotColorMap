import java.awt.Color;
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
	static RiotCalls aRiot = new RiotCalls();
	static ReadData aRead = new ReadData();
	//14870,14980
	
	static Pair<Integer,Integer> MapMaxCoords = new Pair<Integer,Integer>(14870,14980);
	static Pair<Double,Double> PictureMaxCoords = new Pair<Double,Double>(1000.0,1000.0);
	public static List<Long> convertToNums(MatchList finalData)
	{
		List<MatchReference> list = finalData.getMatches();
		List<Long> listOfMatches = new ArrayList<Long>();
		for(int i = 0; i < list.size();i++)
			listOfMatches.add(list.get(i).getMatchId());
		return listOfMatches;
		
	}
	public static List<Coords> getCoords(List<MatchDetail> matches)
	{
		List<Coords> aCoordsList = new ArrayList<Coords>();
		List<String> aStringList = new ArrayList<String>();
		/*aStringList.add("PRESEASON3");
		aStringList.add("SEASON3");
		aStringList.add("PRESEASON2014");
		aStringList.add("SEASON2014");
		aStringList.add("PRESEASON2015");*/
		for(MatchDetail match : matches)
		{
			if(aStringList.contains(match.getSeason()))
			{
				//ignore specific seasons
			}
			else if(match.getTimeline()!=null)
			{
				Coords filteredData = new Coords();
				filteredData.participantData = match.getParticipants();
				filteredData.participantIdentityData = match.getParticipantIdentities();
				List<Frame> aFrameList = match.getTimeline().getFrames();
				for(Frame f: aFrameList)
				{
					List<Event> aEventList = f.getEvents();
					if(aEventList != null)
						for(Event e: aEventList)
						{
							//System.out.println(e.getEventType());
							if(e.getEventType().equals("CHAMPION_KILL"))
								filteredData.filteredEvents.add(e);
								//aCoordsList.add(new Coords(e.getPosition().getX(),e.getPosition().getY()));
						}
				}
				aCoordsList.add(filteredData);
			}
			
		}
		return aCoordsList;
	}
	private static List<MatchDetail> convertListToMatches(List<Long> aList){
		List<MatchDetail> matchDetailList = new ArrayList<MatchDetail>();
		boolean errorCheck;
		for(Long l: aList)
		{
			errorCheck = false;
			do
			{
				try {
					Thread.sleep(1050);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					matchDetailList.add(aRiot.getMatchDetails(l));
					errorCheck = true;
				} catch (RiotApiException e) {
					e.printStackTrace();
				}
			}while(!errorCheck);
		}
		return matchDetailList;
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, RiotApiException {
		String coordFile = "FilteredData";
		List<Coords> coordsList = aRead.getCoordDataFile("e:/Completed/"+coordFile+Integer.toString(0));
		if(coordsList==null||coordsList.isEmpty())
		{
			List<Long> matchListNums = (List<Long>) aRead.getDataFile("e:/Completed/Latvian Match List0");
			if(matchListNums==null||matchListNums.isEmpty())
			{
				SaveToFile aSave = new SaveToFile("Latvian Match List",0);
				MatchList aMatchList = aRiot.getMatchList("RANKED_SOLO_5x5", aRiot.getSummonerId("Latvian Potato"));
				matchListNums = convertToNums(aMatchList);
				Long[] array = matchListNums.toArray(new Long[matchListNums.size()]);
				aSave.saveFileData(array);
			}
			List<?> listOfMatchDetails = aRead.getDataFile("e:/Completed/matchDetails");
			int i = 0;
			int NumPerFile = 100;
			int count = 0;
			if(listOfMatchDetails==null||listOfMatchDetails.isEmpty())
			{
				SaveToFile aSave;
				do
				{
					int tempI = i+NumPerFile;
					if(tempI>matchListNums.size())
						tempI = matchListNums.size();
					List<Long> tempMatchListNums = matchListNums.subList(i, tempI);
					listOfMatchDetails = convertListToMatches(tempMatchListNums);
					System.out.println("saving to file!");
					aSave = new SaveToFile("matchDetails",count);
					MatchDetail[] array = listOfMatchDetails.toArray(new MatchDetail[listOfMatchDetails.size()]);
					aSave.saveFileData(array);
					count++;
					i = count*NumPerFile;
				}while(i<matchListNums.size());
			}
			System.out.println("Retrieved "+listOfMatchDetails.size()+" game data");
			coordsList = getCoords((List<MatchDetail>) listOfMatchDetails);
			i = 0;
			count = 0;
			do
			{
				int tempI = i+NumPerFile;
				if(tempI>coordsList.size())
					tempI = coordsList.size();
				List<Coords> tempCoordsList = coordsList.subList(i, tempI);
				SaveToFile aSave = new SaveToFile(coordFile,count);
				Coords[] coordsArray = tempCoordsList.toArray(new Coords[tempCoordsList.size()]);
				aSave.saveFileData(coordsArray);
				count++;
				i = count*NumPerFile;
			}while(i<coordsList.size());
		}
        JFrame frame = new JFrame("HeatMap");
		DrawImage aImage = new DrawImage(PictureMaxCoords.getValue0().intValue(),PictureMaxCoords.getValue1().intValue());
		//List<Coords> resizedCoords = resizeCoords(coordsList,true);
		aImage.drawPicture(-120, -120, MapMaxCoords,PictureMaxCoords, coordsList);
	    aImage.saveToFile("e:/Completed/heatmap.png");
        frame.add(aImage);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*private static List<Coords> resizeCoords(List<Coords> coordsList, boolean invert) {
		List<Coords> newCoords = new ArrayList<Coords>();
		for(Coords c: coordsList){
			int x = (int) (c.x*(1000.0/MapMaxCoords.x));
			int y =(int) (c.y*(1000.0/MapMaxCoords.y));
			if(invert)
				y = PictureMaxCoords.y - y;
			newCoords.add(new Coords(x,y));
		}
		return newCoords;
	}*/



}
