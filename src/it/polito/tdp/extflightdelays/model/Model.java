package it.polito.tdp.extflightdelays.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Map<Integer, Airport> idMap;
	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	
	public Model() {
		idMap = new HashMap<>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}

	public boolean isDigit(String numVoli) {
		if(numVoli.matches("\\d+")) {
			return true;
		}
		return false;
	}

	public String creaGrafo(String numVoli) {
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		String risultato="";
		dao.loadAllAirports(idMap);
		List<Rotta> rotte = dao.getRotte(idMap, numVoli);
		for(Rotta r: rotte) {
			if(!grafo.containsVertex(r.getA1())) {
				grafo.addVertex(r.getA1());
			}
			if(!grafo.containsVertex(r.getA2())) {
				grafo.addVertex(r.getA2());
			}
			DefaultWeightedEdge edge = grafo.getEdge(r.getA1(), r.getA2());
			if(edge==null) {
				Graphs.addEdgeWithVertices(grafo, r.getA1(), r.getA2(), r.getPeso());
			}
		}
		risultato+="Grafo creato! Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size()+"\n";
		return risultato;
	}

	public List<Airport> getVertici() {
		List<Airport> vertici = new LinkedList<>();
		for(Airport a: grafo.vertexSet()) {
			vertici.add(a);
		}
		return vertici;
	}

	public String getConnessioni(Airport a) {
		String risultato="";
		List<Airport> vicini = Graphs.neighborListOf(grafo, a);
		Collections.sort(vicini, new Comparator<Airport>() {

			@Override
			public int compare(Airport o1, Airport o2) {
				DefaultWeightedEdge edge1 = grafo.getEdge(a, o1);
				double peso1 = grafo.getEdgeWeight(edge1);
				DefaultWeightedEdge edge2 = grafo.getEdge(a, o2);
				double peso2 = grafo.getEdgeWeight(edge2);
				return (int) (peso1-peso2);
			}
		});
		for(Airport a1: vicini) {
			DefaultWeightedEdge edge = grafo.getEdge(a, a1);
			risultato+=a1.getAirportName()+" "+grafo.getEdgeWeight(edge)+"\n";
		}
		return risultato;
	}

}
