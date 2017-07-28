import java.util.Arrays;

import constant.Region;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import dto.Match.MatchDetail;
import dto.MatchList.MatchList;
import dto.Summoner.Summoner;

public class RiotCalls {
	RiotApi api = new RiotApi("your-key-here");
	public MatchList getMatchList(String queueType, Long summonerId) throws RiotApiException{
		return api.getMatchList(Region.NA, summonerId);
	};
	
	public Long getSummonerId(String summonerId) throws RiotApiException{
		return api.getSummonerByName(summonerId).getId();
	};
	
	public MatchDetail getMatchDetails(Long l) throws RiotApiException
	{
		System.out.println("getting match "+ l);
		return api.getMatch(Region.NA, (long) l, true);
	}
	public static void main(String[] args) throws RiotApiException{
		RiotCalls aRiot = new RiotCalls();
		System.out.println(aRiot.getSummonerId("Latvian Potato"));
		
	
	}
}

