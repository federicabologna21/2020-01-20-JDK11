package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private SimpleWeightedGraph <Artist, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao; 
	private Map <Integer, Artist> idMap;
	
	private List<Artist> percorsoMigliore;
	private double pesoScelto ;
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap <Integer, Artist>();
		dao.listArtists(idMap);
		
	}
	
	public List<String> getRole(){
		return dao.getRole();
	}
	
	public void creaGrafo(String role) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici 
		Graphs.addAllVertices(grafo, dao.getVertici(role, idMap));
		
		// aggiungo gli archi
		for (Adiacenza a : dao.getAdiacenze(role, idMap)) {
			if (this.grafo.containsVertex(a.getA1()) && this.grafo.containsVertex(a.getA2())) {
				DefaultWeightedEdge e = this.grafo.getEdge(a.getA1(), a.getA2());
				
				if (e == null) {
					Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
				}
			}
		}
	}

	public int getNumVertici() {
		if (this.grafo!=null) {
			return this.grafo.vertexSet().size();
		}
		return 0;
	}
	public int getNumArchi() {
		if (this.grafo!=null) {
			return this.grafo.edgeSet().size();
		}
		return 0;
	}
	
	public List<Adiacenza> getArtistiConnessi(String role){
		List<Adiacenza> result= new LinkedList<Adiacenza>(this.dao.getAdiacenze(role, idMap));
		Collections.sort(result, new Comparator<Adiacenza>() {

			@Override
			public int compare(Adiacenza o1, Adiacenza o2) {
				Double d1 = o1.getPeso();
				Double d2 = o2.getPeso();
				return d2.compareTo(d1);
			}
			
		});
		
		return result;
	}
	
	public List<Artist> trovaPercorso (Integer inserito){
		
		this.percorsoMigliore = new ArrayList<>();
		pesoScelto = 0;
		int n= 0;
		List<Artist> parziale = new ArrayList<>();
		Artist partenza = null;
		
		for (Artist a : this.grafo.vertexSet()) {
			if(a.getArtist_id() == inserito) {
				partenza = a;
			}
		}
		
		parziale.add(partenza);
		cerca(parziale, n, partenza);
		return this.percorsoMigliore;
	}

	private void cerca(List<Artist> parziale, int n, Artist partenza) {
		// caso terminale
		if (parziale.size() > this.percorsoMigliore.size()) {
			this.percorsoMigliore = new ArrayList<>(parziale);
			return ;
		}
	/*	if (this.percorsoMigliore == null) {
			this.percorsoMigliore = new ArrayList<>(parziale);
			return ;
		} */
		
		// altrimenti ... 
		
		if (n == 0) {
			for (Artist vicino : Graphs.neighborListOf(grafo, partenza)) {
				DefaultWeightedEdge arco = this.grafo.getEdge(partenza, vicino);
				double peso = this.grafo.getEdgeWeight(arco);
				pesoScelto = peso; 
				
				parziale.add(vicino);
				n++;
				cerca(parziale, n, partenza);
				parziale.remove(parziale.size()-1);
			}
		}
		else {
			Artist ultimo = parziale.get(parziale.size()-1);
			for (Artist vicino2 : Graphs.neighborListOf(grafo, ultimo)) {
				if(!parziale.contains(vicino2)) {
					DefaultWeightedEdge arco = this.grafo.getEdge(ultimo, vicino2);
					double peso = this.grafo.getEdgeWeight(arco);
					
					if (peso == pesoScelto) {
						parziale.add(vicino2);
						n++;
						cerca(parziale, n, partenza);
						parziale.remove(parziale.size()-1);
					}
				}
			
			}
		
		}
	}
}
