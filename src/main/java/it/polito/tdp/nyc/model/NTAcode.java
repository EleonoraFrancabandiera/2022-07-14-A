package it.polito.tdp.nyc.model;

import java.util.HashSet;
import java.util.Set;

public class NTAcode {
	
	private String NTA_id;
	private Set<String> SSID;
	private int condivisi;
	
	
	public NTAcode(String nTA_id) {
		super();
		NTA_id = nTA_id;
		SSID = new HashSet<>();
		condivisi=0;
	}

	public String getNTA_id() {
		return NTA_id;
	}

	public void setNTA_id(String nTA_id) {
		NTA_id = nTA_id;
	}

	public Set<String> getSSID() {
		return SSID;
	}

	public void addSSID(String ssid) {
		this.SSID.add(ssid);
	}
	
	public int getCondivisi() {
		return condivisi;
	}

	public void incrementaCondivisi() {
		this.condivisi++;
	}

	@Override
	public String toString() {
		return this.NTA_id + "  " + this.condivisi;
	}
	
	public int getPeso() {
		return SSID.size();
	}
	
	
	

}
