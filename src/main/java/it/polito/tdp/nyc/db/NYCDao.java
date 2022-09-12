package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.nyc.model.Hotspot;
import it.polito.tdp.nyc.model.NTAcode;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public List<String> getAllBorough(){
		String sql="SELECT DISTINCT Borough AS b "
				+ "FROM nyc_wifi_hotspot_locations";
				
		List<String> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("b"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;		
	}
	
	public List<NTAcode> getVertici(String borgo){
		String sql="SELECT distinct n.NTACode "
				+ "	FROM nyc_wifi_hotspot_locations n "
				+ "	WHERE n.Borough=?";
				
		List<NTAcode> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, borgo);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new NTAcode(res.getString("n.NTACode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;		
	}
	
	public void setSSID(List<NTAcode> vertici) {
		String sql="SELECT distinct n.SSID "
				+ "FROM nyc_wifi_hotspot_locations n "
				+ "WHERE n.NTACode=?";
		
		for(NTAcode v : vertici) {
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, v.getNTA_id());
				ResultSet res = st.executeQuery();
	
				while (res.next()) {
					v.addSSID(res.getString("n.SSID"));
				}
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("SQL Error");
			}
		}
	}
	
	

	
}
