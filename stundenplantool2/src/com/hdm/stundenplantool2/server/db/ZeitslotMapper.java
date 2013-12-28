package com.hdm.stundenplantool2.server.db;

import java.sql.*;
import java.util.Vector;

import com.hdm.stundenplantool2.shared.bo.*;

/*
 * Mapperklasse um Zeitslot-Objekte aus und in die DB abzubilden
 * @author: Herr Prof. Thies
 * @implement: Lucas Zanella 
 */

public class ZeitslotMapper {
	
	private static ZeitslotMapper zeitslotMapper = null;
	
	protected ZeitslotMapper(){
		
	}
	
	public static ZeitslotMapper zeitslotMapper() {
	    if (zeitslotMapper == null) {
	    	zeitslotMapper = new ZeitslotMapper();
	    }

	    return zeitslotMapper;
	   }
	
	/*
	 * Methode um eine beliebige Anzahl an Zeitslots anhand Ihrerer ID's aus der
	 * DB auszulesen
	 */
	public Vector<Zeitslot> findByKey(Vector<Integer> keys) throws RuntimeException {
		StringBuffer ids = new StringBuffer();
		
		//Erstellung des dynamischen Teils des SQL-Querys
		
		if (keys.size() > 1) {
		for (int i = 0; i < keys.size()-1; i++) {
			ids.append(keys.elementAt(i));	
			ids.append(",");
		}
		}
			
		ids.append(keys.elementAt(keys.size()-1));	
		
		//Einholen einer DB-Verbindung und
		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<Zeitslot> zeitslots = new Vector<Zeitslot>();
		
		try{			
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Zeitslot WHERE ID IN (" + ids.toString() + ")";
			rs = stmt.executeQuery(sql);
			
			// Befüllen des "Raum-Vectors"
			while(rs.next()){
				Zeitslot zeitslot = new Zeitslot();
				zeitslot.setId(rs.getInt("ID"));
				zeitslot.setAnfangszeit(rs.getInt("Anfangszeit"));
				zeitslot.setEndzeit(rs.getInt("Endzeit"));
				zeitslot.setWochentag(rs.getString("Wochentag"));
				zeitslots.add(zeitslot);  
	          }

		}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		return zeitslots;
	}
	
	// Auslesen aller Zeitslots aus der DB
	public Vector<Zeitslot> findAll() throws RuntimeException {
				
		//Einholen einer DB-Verbindung und
		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<Zeitslot> zeitslots = new Vector<Zeitslot>();
		
		try{			
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Zeitslot";
			rs = stmt.executeQuery(sql);
			
			// Befüllen des "Raum-Vectors"
			while(rs.next()){
				Zeitslot zeitslot = new Zeitslot();
				zeitslot.setId(rs.getInt("ID"));
				zeitslot.setAnfangszeit(rs.getInt("Anfangszeit"));
				zeitslot.setEndzeit(rs.getInt("Endzeit"));
				zeitslot.setWochentag(rs.getString("Wochentag"));
				zeitslots.add(zeitslot);  
	          }
			

		}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		return zeitslots;
	}
	
	// Aktualisieren eines Zeitslots
	
	public Zeitslot update(Zeitslot zeitslot) throws RuntimeException {
		Connection con = DBConnection.connection();
		
		// Aktualisierung der Raum-Entität in der DB
		
		try{
		Statement stmt = con.createStatement();
		
			String sql = "UPDATE Zeitslot SET Anfangszeit='"+zeitslot.getAnfangszeit()+"', Endzeit='"+zeitslot.getEndzeit()+"', Wochentag='"+zeitslot.getWochentag()+"' WHERE ID="+zeitslot.getId()+";";
			stmt.executeUpdate(sql);
					
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");
			}
		
		return zeitslot;
	}
	
	// Das Löschen eines Zeitslots ist bis dato nicht vorgesehen (Stand: 04.12.2013)
	public void delete(Zeitslot zeitslot) throws RuntimeException {
		
	}
	
	// Das Anlegen eines neuen Zeitslots ist bis dato nicht vorgesehen (Stand: 04.12.2013)
	public Zeitslot insertIntoDB(Zeitslot zeitslot) throws RuntimeException {
		return null;	
	}

}
