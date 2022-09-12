package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private NYCDao dao;
	private List<String> borghi;
	private List<NTAcode> vertici;
	private Graph<NTAcode, DefaultWeightedEdge> grafo;
	
	//risultati simulatore
	private Map<String, NTAcode> mappaNTA;
	
	public Model() {
		this.dao = new NYCDao();
	}
	
	public void creaGrafo(String borgo) {
		this.vertici =this.dao.getVertici(borgo);
		this.dao.setSSID(vertici);
		
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		for(NTAcode v1: vertici) {
			for(NTAcode v2 : vertici) {
				if(!v1.equals(v2)) {
					Set<String> ssid = new HashSet<>(v1.getSSID());
					ssid.addAll(v2.getSSID());
					if(ssid.size()>0) {
						Graphs.addEdge(this.grafo, v1, v2, ssid.size());
					}
				}
			}
		}
		
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getBorghi(){
		if(this.borghi==null)
			this.borghi=	this.dao.getAllBorough();
		return this.borghi;
	}
	
	public double pesoMedioArchi() {
		
		double pesoTot=0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoTot+=this.grafo.getEdgeWeight(e);
		}
		
		double pesoMedio = pesoTot/this.getNumArchi();
		return pesoMedio;
	}
	
	public List<Arco> getArchiPesoMaggiore(){
		double pesoMedio= this.pesoMedioArchi();
		
		List<Arco> result= new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			double pesoArco = this.grafo.getEdgeWeight(e);
			if(pesoArco>pesoMedio) {
				result.add(new Arco(this.grafo.getEdgeSource(e).getNTA_id(), this.grafo.getEdgeTarget(e).getNTA_id(), pesoArco));
			}
		}
		
		Collections.sort(result);
		return result;
	}
	
	public boolean isGrafoCreato() {
		if(this.grafo!=null)
			return true;
		else 
			return false;
	}
	
	public void simula(double probabilita, int durata) {
		Simulator sim = new Simulator(this.grafo);
		sim.init(probabilita, durata);
		sim.run();
		this.mappaNTA=sim.getMappaNTA();
		
	}

	public Map<String, NTAcode> getMappaNTA() {
		return mappaNTA;
	}

	
}
