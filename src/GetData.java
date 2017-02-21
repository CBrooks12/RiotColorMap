import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dto.Match.Event;
import dto.Match.Frame;
import dto.Match.MatchDetail;
import dto.MatchList.MatchList;
import dto.MatchList.MatchReference;
import main.java.riotapi.RiotApiException;

public class GetData {
	private String user;
	private String pathName;
	private RiotCalls aRiot = new RiotCalls();
	private ReadData aRead;
	public GetData(String pathName)
	{
		this.pathName = pathName;
		aRead = new ReadData(pathName);
	}
	
	public void setUser(String aUser) {
		user = aUser;
		
	}
	
	
	public static List<Long> convertToNums(MatchList finalData)
	{
		List<MatchReference> list = finalData.getMatches();
		List<Long> listOfMatches = new ArrayList<Long>();
		for(int i = 0; i < list.size();i++)
			listOfMatches.add(list.get(i).getMatchId());
		return listOfMatches;
		
	}
	
	public List<Coords> getParamCoords(List<MatchDetail> matches,String param) throws IOException, RiotApiException
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
							if(e.getEventType().equals(param))
								filteredData.filteredEvents.add(e);
								//aCoordsList.add(new Coords(e.getPosition().getX(),e.getPosition().getY()));
						}
				}
				aCoordsList.add(filteredData);
			}
			
		}
		return aCoordsList;
	}
	
	private List<MatchDetail> convertListToMatches(List<Long> aList){
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
	
	private List<?> getMatches(boolean b) throws IOException, RiotApiException
	{
		List<?> listOfMatchDetails = aRead.getDataFile(user+"/Completed/matchDetails");
		if(listOfMatchDetails==null||listOfMatchDetails.isEmpty())
		{
			System.out.println("No match details, retrieving match list");
			List<Long> matchListNums = getMatchList(true); //get just the list of matches
			if(matchListNums.isEmpty())
			{
				System.out.println("no match details to get");
				return null;
			}
			if(b){ // if true, save data
				int i = 0;
				int NumPerFile = 100;
				int count = 0;
				SaveToFile aSave;
				do
				{
					int tempI = i+NumPerFile;
					if(tempI>matchListNums.size())
						tempI = matchListNums.size();
					List<Long> tempMatchListNums = matchListNums.subList(i, tempI);
					listOfMatchDetails = convertListToMatches(tempMatchListNums);
					System.out.println("saving to file!");
					aSave = new SaveToFile(user, "matchDetails",count);
					MatchDetail[] array = listOfMatchDetails.toArray(new MatchDetail[listOfMatchDetails.size()]);
					aSave.saveFileData(array);
					count++;
					i = count*NumPerFile;
				}while(i<matchListNums.size());
			}
		}
		System.out.println("Retrieved "+listOfMatchDetails.size()+" game data");
		return listOfMatchDetails;
		
		
	}
	public List<Coords> getCoordData(boolean b) throws IOException, RiotApiException
	{
		List<Coords> coordsList = aRead.getCoordDataFile(user + "/Completed/"+"coordFile");
		if(coordsList==null||coordsList.isEmpty())
		{
			System.out.println("No coord list data, retrieving from matches");
			@SuppressWarnings("unchecked")
			List<MatchDetail> matches = (List<MatchDetail>) getMatches(true);
			if(matches==null)
			{
				System.out.println("cannot get coords, no match details");
				return null;
			}
			coordsList = getParamCoords(matches,"CHAMPION_KILL");
			
			//if true, save data
			if(b){
				int i = 0;
				int NumPerFile = 100;
				int count = 0;
				do
				{
					int tempI = i+NumPerFile;
					if(tempI>coordsList.size())
						tempI = coordsList.size();
					List<Coords> tempCoordsList = coordsList.subList(i, tempI);
					SaveToFile aSave = new SaveToFile(user,"coordFile",count);
					Coords[] coordsArray = tempCoordsList.toArray(new Coords[tempCoordsList.size()]);
					aSave.saveFileData(coordsArray);
					count++;
					i = count*NumPerFile;
				}while(i<coordsList.size());
			}
		}
		return coordsList;	
	}
	//helper function that gets matchList
	@SuppressWarnings("unchecked")
	private List<Long> getMatchList(boolean b) throws IOException {
		List<Long> matchListNums = (List<Long>) aRead.getDataFile(user + "/Completed/" + "Match List");
		if(matchListNums==null||matchListNums.isEmpty())
		{
			System.out.println("No match list, retrieving match list");
			SaveToFile aSave = null;
			if(b)  //if boolean is true, save to disk
				aSave = new SaveToFile(user, "Match List",0);
			
			//get matches
			MatchList aMatchList;
			try {
				aMatchList = aRiot.getMatchList("RANKED_SOLO_5x5", aRiot.getSummonerId(user));
			} catch (RiotApiException e) {
				System.out.println("Error retrieving match list");
				return null;
			}
			
			//convert the matches to a list of longs
			matchListNums = convertToNums(aMatchList);
			if(b)	//if boolean is true, save to disk
			{
				Long[] array = matchListNums.toArray(new Long[matchListNums.size()]);
				aSave.saveFileData(array);
			}
		}
		return matchListNums;
			
		
	}
	

}
