package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {
		// query per estrarre tutti i NERC presenti nel database
		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return nercList;
	}
	
	// recupera l'insieme di tutti i blackout per un certo NERC
	public List<PowerOutages> getPOList(Nerc nerc){
		String sql = " SELECT id, nerc_id, customers_affected, date_event_began, date_event_finished "
				+ "FROM poweroutages "
				+ "WHERE nerc_id = ?";
		List<PowerOutages> poList = new ArrayList<PowerOutages>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				PowerOutages po = new PowerOutages(rs.getInt("id"), nerc, rs.getInt("customers_affected"), rs.getTimestamp("date_event_began").toLocalDateTime(), rs.getTimestamp("date_event_finished").toLocalDateTime());
				poList.add(po);
			}
			conn.close();
			rs.close();
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
		 return poList;
	}

}
