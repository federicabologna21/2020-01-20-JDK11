package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRole(){
		String sql = "SELECT a.role AS role "
				+ "FROM authorship a "
				+ "GROUP BY a.role";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void listArtists(Map<Integer, Artist> idMap) {
		String sql = "SELECT * "
				+ "FROM artists ";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("artist_id"))) {
					Artist artist = new Artist(res.getInt("artist_id"), res.getString("name"));
					idMap.put(artist.getArtist_id(), artist);
					
				}
				
			}
			conn.close();
		//	return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		//	return null;
		}
		
	}
	
	public List<Artist> getVertici(String role, Map<Integer, Artist> idMap){
		String sql = "SELECT a.artist_id AS id "
				+ "FROM authorship a "
				+ "WHERE a.role=? "
				+ "GROUP BY a.artist_id";
		List<Artist> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(idMap.get(res.getInt("id")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze (String role, Map<Integer, Artist> idMap){
		String sql ="SELECT a1.artist_id AS id1, a2.artist_id AS id2, COUNT(eo1.exhibition_id) AS peso "
				+ "FROM authorship a1, authorship a2, objects o1, objects o2, exhibition_objects eo1, exhibition_objects eo2 "
				+ "WHERE a1.artist_id > a2.artist_id "
				+ "AND a1.object_id = o1.object_id AND a2.object_id = o2.object_id "
				+ "AND eo1.exhibition_id = eo2.exhibition_id "
				+ "AND eo1.object_id = o1.object_id AND eo2.object_id = o2.object_id "
				+ "AND a1.role = a2.role AND a1.role=? "
				+ "GROUP BY a1.artist_id, a2.artist_id";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Artist a1 = idMap.get(res.getInt("id1"));
				Artist a2 = idMap.get(res.getInt("id2"));
				
				if( a1!=null && a2!=null) {
					Adiacenza ad = new Adiacenza (a1, a2, res.getDouble("peso"));
					result.add(ad);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
