package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.PaperIdMap;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	/**
	 * 
	 * @return List di tutti gli autori su db
	 */
	public List<Author> getAllAutori(AuthorIdMap amap) {

		final String sql = "SELECT DISTINCT * FROM author";
		List<Author> list = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				list.add(amap.get(autore));
			}

			return list;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<Author> getAutoriFromPaper(Paper p, PaperIdMap map) {
		
		final String sql = "SELECT id, lastname, firstname FROM creator AS c, author AS a WHERE c.authorid=a.id AND c.eprintid = ? ";
		List<Author> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, map.get(p).getEprintid());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				list.add(autore);
			}

			return list;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/**
	 * 
	 * @return List di tutti i papers su db
	 */
	public List<Paper> getAllPapers(PaperIdMap pmap) {
		final String sql = "SELECT DISTINCT * FROM paper ";
		List<Paper> list = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Paper p = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"), rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				list.add(pmap.get(p));
			}

			return list;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public boolean checkConn(Author a1, Author a2) {
		
		final String sql = "SELECT eprintid FROM creator AS c WHERE eprintid IN(SELECT eprintid FROM creator WHERE creator.authorid= ?) AND c.authorid = ?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a1.getId());
			st.setInt(2, a2.getId());

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				st.close();
				rs.close();
				conn.close();
				return true;
			}
			
			st.close();
			rs.close();
			conn.close();

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		

		return false;
	}
}