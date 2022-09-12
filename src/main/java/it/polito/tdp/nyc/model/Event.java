package it.polito.tdp.nyc.model;

public class Event implements Comparable<Event> {

	public enum EventType{
		CONDIVISIONE,
		RICONDIVISIONE
	}
	
	private int durata;
	private EventType type;
	private NTAcode NTA;
	
	
	public Event(int durata, EventType type, NTAcode nTA) {
		super();
		this.durata = durata;
		this.type = type;
		NTA = nTA;
	}


	public int getDurata() {
		return durata;
	}


	public EventType getType() {
		return type;
	}


	public NTAcode getNTA() {
		return NTA;
	}


	@Override
	public int compareTo(Event o) {
		return this.durata-o.getDurata();
	}
	
	
}
