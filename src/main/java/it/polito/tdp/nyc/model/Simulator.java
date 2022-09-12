package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.nyc.model.Event.EventType;

public class Simulator {
	
	//Dati in ingresso
	private double p;
	private int durata;
	
	//Dati in uscita
	private Map<String, NTAcode> mappaNTA;
	
	//Modello del mondo
	private Graph<NTAcode, DefaultWeightedEdge> grafo;
	
	
	//Coda degli eventi
	private PriorityQueue<Event> queue;
	
	public Simulator(Graph<NTAcode, DefaultWeightedEdge> grafo) {
		this.grafo=grafo;
	}
	
	public void init(double probabilita, int d) {
		//inizializzo input e output
		this.p=probabilita;
		this.durata=d;	
		this.mappaNTA = new HashMap<>();
		for(NTAcode v : this.grafo.vertexSet()) {
			mappaNTA.put(v.getNTA_id(), v);
		}
		
		//creo la coda
		this.queue=new PriorityQueue<>();
		
		//inizializzo la coda
		for(int i=0; i<100; i++) {
			NTAcode selezionato = selezionaNTA(this.grafo.vertexSet());				
			this.queue.add(new Event(durata, EventType.CONDIVISIONE, selezionato));
		}
				
	}
		
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
		
	}
	
	public void processEvent(Event e) {
		
		switch(e.getType()) {
		
		case CONDIVISIONE:
			double caso = Math.random();
			
			if(caso<this.p) {//si condivide un file
				
				//incremento il numero dei file condivisi da quel nodo
				mappaNTA.get(e.getNTA().getNTA_id()).incrementaCondivisi();
				
				NTAcode confinante = this.selezionaConfinante(e.getNTA());				
				this.queue.add(new Event((int)Math.floor(e.getDurata()/2), EventType.RICONDIVISIONE, confinante));	
			}else {
				//non si condivide nulla
			}
			break;
			
			
		case RICONDIVISIONE:
			mappaNTA.get(e.getNTA().getNTA_id()).incrementaCondivisi();
			if(e.getDurata()>0) {
				NTAcode confinante = this.selezionaConfinante(e.getNTA());
				this.queue.add(new Event((int)Math.floor(e.getDurata()/2), EventType.RICONDIVISIONE, confinante));	
			}
			
		break;
		
		}
		
	}
	
	private NTAcode selezionaConfinante(NTAcode n) {
		//guardo se ci sono degli adiacenti di n senza file condivisi
		//se sì cerco quello col peso più alto
		//altrimenti cerco quello col peso più alto tra i confinanti
		List<NTAcode> adiacenti = Graphs.neighborListOf(this.grafo, n);
		List<NTAcode> validi = new ArrayList<>();
		int max=0;
		NTAcode confinante= null;
		
		for(NTAcode a : adiacenti) {
			NTAcode temp = this.mappaNTA.get(a.getNTA_id());
			if(temp.getCondivisi()==0) {
				validi.add(temp);
			}
		}
		
		if(validi.size()!=0) {
			for(NTAcode v: validi) {
				int peso = v.getPeso();
				if(peso>max) {
					max=peso;
					confinante=v;
				}
			}
		}else {
			for(NTAcode v: adiacenti) {
				int peso = v.getPeso();
				if(peso>max) {
					max=peso;
					confinante=v;
				}
			}
			
		}
		
		return confinante;
		
	}
	
	private NTAcode selezionaNTA(Collection<NTAcode> lista) {
		List<NTAcode> candidati = new ArrayList<>(lista);
		int scelto = (int)(Math.random()*lista.size());		
		return candidati.get(scelto);
	}


	public Map<String, NTAcode> getMappaNTA() {
		return mappaNTA;
	}

}
