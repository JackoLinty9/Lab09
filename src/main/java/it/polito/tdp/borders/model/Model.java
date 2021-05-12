package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Map<Integer, Country>mappaCountries;
	private BordersDAO dao;
	private SimpleGraph<Country, DefaultEdge>grafo;
	
	public Model() {
		mappaCountries=new HashMap<Integer,Country>();
		dao=new BordersDAO();	
	}
	
    public void creaGrafo(int anno) {
    	
    	dao.loadCountries(mappaCountries, anno);
    	grafo=new SimpleGraph<>(DefaultEdge.class);
		Graphs.addAllVertices(grafo, mappaCountries.values()); // non ci sono filtri sui vertici
		System.out.println(grafo.vertexSet().size());
		
		//aggiungo archi
		for(Border b:dao.getCountryPairs(anno, mappaCountries)) {
			if(this.grafo.containsVertex(b.getS1()) && this.grafo.containsVertex(b.getS2())) { //non c'è ancora arco tra A1 e A2
				DefaultEdge e=this.grafo.getEdge(b.getS1(), b.getS2());
				if(e==null)
					Graphs.addEdgeWithVertices(grafo, b.getS1(), b.getS2());
		    }
		}	
		System.out.println(grafo.edgeSet().size());
    }
    
    public Set<Country> getCountries() {
		return grafo.vertexSet();
	}
    
    public String getGradoVertici() {
    	
		String s = "";
		for(Country c : this.grafo.vertexSet()) {
			s += c.getName() + ": " + this.grafo.degreeOf(c) + " stato/i confinante/i\n";
		}
		return s;
	}
    
    public int getNumberOfConnectedComponents() {
    	
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<>(grafo); //constructor
		return ci.connectedSets().size(); //method
	}
		

}
