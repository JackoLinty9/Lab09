package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public void getVertici(Map<Integer,Country>mappaCountries, int anno) {
		
		String sql = "SELECT StateAbb as abb, CCode as code, StateNme as name "+
				"FROM country c, contiguity co "+
				"WHERE (c.CCode=co.state1no OR c.CCode=co.state2no) AND co.year<=? AND co.conttype=1 "+
				"GROUP BY c.CCode";
	
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country ctemp=new Country(rs.getString("abb"),rs.getInt("code"),rs.getString("name"));
				mappaCountries.put(ctemp.getCode(), ctemp);
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public List<Border> getArchi(int anno, Map<Integer, Country> mappa) { //passo come parametro la mappa con vertici del grafo
		
		String sql = "SELECT state1no, state2no "+
				"FROM contiguity AS con "+
				"WHERE con.year<=? AND con.conttype=1 AND state1no<state2no";
		
		List<Border>result=new ArrayList<Border>();	
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Country c1=mappa.get(rs.getInt("state1no"));
				Country c2=mappa.get(rs.getInt("state2no"));
				Border btemp=new Border(c1,c2);
				result.add(btemp);
				
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
}
