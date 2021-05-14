package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Map<Integer, Country>mappaCountries;
	private BordersDAO dao;
	private SimpleGraph<Country, DefaultEdge>grafo;
	private Map<Country, Country> predecessore;
	
	public Model() {
		mappaCountries=new HashMap<Integer,Country>();
		dao=new BordersDAO();	
	}
	
    public void creaGrafo(int anno) {
    	
    	dao.getVertici(mappaCountries, anno);
    	grafo=new SimpleGraph<>(DefaultEdge.class);
		Graphs.addAllVertices(grafo, mappaCountries.values()); 
		System.out.println(grafo.vertexSet().size());
		
		//aggiungo archi
		for(Border b:dao.getArchi(anno, mappaCountries)) {
			
			if(this.grafo.containsVertex(b.getS1()) && this.grafo.containsVertex(b.getS2())) { //non c'è ancora arco tra A1 e A2
				
				DefaultEdge e=this.grafo.getEdge(b.getS1(), b.getS2());
				if(e==null) //se non c'e ancora l'arco tra i due vertici
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
	
    public String getRaggiungibili(Country partenza) {
		
    	if(!this.grafo.containsVertex(partenza)) {
			return "Il vertice selezionato non è presente nel grafo!";
		}
    	
		BreadthFirstIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<>(this.grafo, partenza) ;
        
		this.predecessore = new HashMap<Country,Country>() ;
		this.predecessore.put(partenza, null) ; //visto che è il primo vertice non ha predecessore
		
		//metodo che fa si che l'iteratore mi avverta quando succedono certe cose mentre attraversa il grafo
		bfv.addTraversalListener(new TraversalListener<Country, DefaultEdge>(){ //nuova classe inline

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				
				DefaultEdge arco = e.getEdge() ;
				Country a = grafo.getEdgeSource(arco);
				Country b = grafo.getEdgeTarget(arco);
			
				//se conoscevo b ma non a
				if(predecessore.containsKey(b) && !predecessore.containsKey(a))
					predecessore.put(a, b) ;
					
			    //se conoscevo a ma non b
				else if(predecessore.containsKey(a) && !predecessore.containsKey(b))
					predecessore.put(b, a) ;	
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
			}
		});
		
		List<Country> statiRaggiungibili = new ArrayList<>() ;
		
		while(bfv.hasNext()) {
			Country f = bfv.next() ;
			statiRaggiungibili.add(f) ;
		}
		
		String risultato="";
		if(statiRaggiungibili.isEmpty()) {
			return risultato;
		}

		risultato = risultato +"Numero stati raggiungibili = " +statiRaggiungibili.size() +"\n";
		for(Country c : statiRaggiungibili)
			risultato = risultato +c +"\n";

		return risultato;
    }
    
}
