package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private PortoDAO pdao;
	
	private AuthorIdMap amap;
	private PaperIdMap pmap;
	private List<Author> autori;
	//private List<Paper> papers
	
	private SimpleGraph<Author, DefaultEdge> grafo;
	
	public Model() {
		pdao = new PortoDAO();
		amap = new AuthorIdMap();
		pmap = new PaperIdMap();
		
		autori = pdao.getAllAutori(amap);
		//papers = pdao.getAllPapers(pmap);
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
					System.out.println("CI STO");
				}
			}
		}
	}
		
	public List<Author> getCoautori(Author box){
		
		List<Author> coautori = new ArrayList<>();
		
		for(Author a : autori) {
			if(!a.equals(box) && grafo.containsEdge(box, a))
				coautori.add(a);
		}
		return coautori;
	}
	
	public List<Author> getNonCoautori(Author box){
		
		List<Author> noncoautori = new ArrayList<>();
		
		for(Author a : autori) {
			if(!a.equals(box) && !grafo.containsEdge(box, a))
				noncoautori.add(a);
		}
		return noncoautori;
	}

	private boolean checkConn(Author a1, Author a2) {
		if(a1.equals(a2))
			return false;
		if(pdao.checkConn(a1, a2))
			return true;
		return false;
	}
}
