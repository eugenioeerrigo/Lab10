package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private PortoDAO pdao;
	
	private AuthorIdMap amap;
	private PaperIdMap pmap;
	private List<Author> autori;
	private List<Paper> papers;
	
	private SimpleGraph<Author, DefaultEdge> grafo;
	
	public Model() {
		pdao = new PortoDAO();
		amap = new AuthorIdMap();
		pmap = new PaperIdMap();
		
		autori = pdao.getAllAutori(amap);
		papers = pdao.getAllPapers(pmap);
		this.creaGrafo();
	}

	public List<Author> getAutori(){
		Collections.sort(autori);
		return autori;
	}
	
	
	private void creaGrafo() {

		grafo = new SimpleGraph<>(DefaultEdge.class);
		
		Graphs.addAllVertices(this.grafo, autori);
		
		for(Author a1 : autori) {
			for(Author a2 : autori) {
				if(this.checkConn(a1, a2)) {
					grafo.addEdge(a1, a2);
					System.out.println("CI STO: "+a1+" - "+a2+"\n");
				}
			}
		}
		
	}
		
	public List<Author> getCoautori(Author box){
		
//		List<Author> coautori = new ArrayList<>();                    //Graph.neighboursOf(box)
//		
//		for(Author a : autori) {
//			if(!a.equals(box) && grafo.containsEdge(box, a))
//				coautori.add(a);
//		}
	
		return Graphs.neighborListOf(this.grafo, box);
	}
	
	public List<Author> getNonCoautori(Author box){
		
//		List<Author> noncoautori = new ArrayList<>();                
//		
//		for(Author a : autori) {                                      
//			if(!a.equals(box) && !grafo.containsEdge(box, a))
//				noncoautori.add(a);
//		}
//		return noncoautori;
		
		List<Author> noncoautori = new ArrayList<>(autori); 
		
		noncoautori.remove(box);
		noncoautori.removeAll(this.getCoautori(box));
		
		return noncoautori;
	}

	private boolean checkConn(Author a1, Author a2) {
		if(a1.equals(a2))
			return false;
		if(!pdao.checkConn(a1, a2, pmap).isEmpty())
			return true;
		return false;
	}

	public String trovaArticoliConn(Author a1, Author a2) {
		List<Paper> result = new ArrayList<>();
		String res = "";
		
		ShortestPathAlgorithm<Author, DefaultEdge> spa = new DijkstraShortestPath<Author, DefaultEdge>(this.grafo);
		GraphPath<Author, DefaultEdge> gp = spa.getPath(a1, a2);
		List<DefaultEdge> edges = gp.getEdgeList();
		
		for(DefaultEdge e : edges) {
			List<Paper> link = pdao.checkConn(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), pmap);
			for(Paper p : link) {
				result.add(p);
			}
		}
		
		for(Paper p : result) {
			res += p.toString()+"\n";
		}
		return res;
	}
}
