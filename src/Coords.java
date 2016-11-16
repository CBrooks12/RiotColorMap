import java.util.ArrayList;
import java.util.List;

import dto.Match.Event;
import dto.Match.Participant;
import dto.Match.ParticipantIdentity;

public class Coords {
	List<Participant> participantData;
	List<ParticipantIdentity> participantIdentityData;
	List<Event> filteredEvents;
	
	public Coords() {
		participantIdentityData = new ArrayList<ParticipantIdentity>();
		filteredEvents = new ArrayList<Event>();
	}
	public Coords(List<Participant> pData,List<ParticipantIdentity> piData, List<Event> eData) {
		participantData = pData;
		participantIdentityData = piData;
		filteredEvents = eData;
		
	}


}
