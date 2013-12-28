package com.hdm.stundenplantool2.server.db;

import java.sql.*;
import java.util.Vector;

import com.hdm.stundenplantool2.shared.bo.*;

/*
 * Mapperklasse um Raum-Objekte aus und in die DB abzubilden
 * @author: Herr Prof. Thies
 * @implement: Lucas Zanella 
 */

public class RaumMapper {

	private static RaumMapper raumMapper = null;
	
	protected RaumMapper(){
		
	}
	
	public static RaumMapper raumMapper() {
	    if (raumMapper == null) {
	      raumMapper = new RaumMapper();
	    }

	    return raumMapper;
	   }
	
	// Aktualisieren eines Raumes
	
	public Raum update(Raum raum) throws RuntimeException {
		Connection con = DBConnection.connection();
		
		// Aktualisierung der Raum-Entität in der DB
		
		try{
		Statement stmt = con.createStatement();
		
		String sql = "UPDATE Raum SET Kapazitaet='"+raum.getKapazitaet()+"', Bezeichnung='"+raum.getBezeichnung()+"' WHERE ID="+raum.getId()+";";
		stmt.executeUpdate(sql);
		
		}			
		
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");	
		}
		
		return raum;
	}
	
	// Löschen eines Raums aus der DB
	public void delete(Raum raum) throws RuntimeException {
		
		
				Connection con = DBConnection.connection();
				try {
					Statement stmt = con.createStatement();
										
					String sql = "DELETE FROM Raum WHERE ID = '"+raum.getId()+"';";
					stmt.executeUpdate(sql);
					
					
					
				}
				catch (SQLException e1) {
					throw new RuntimeException("Datenbankbankproblem");
				}
		
	}
	
	// Ablegen eines neuen Raums in die DB
	public Raum insertIntoDB (Raum raum) throws RuntimeException {
		
		Connection con = DBConnection.connection();
		ResultSet rs;
		
		try{
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO Raum (`Kapazitaet`, `Bezeichnung`) VALUES ('"+raum.getKapazitaet()+"', '"+raum.getBezeichnung()+"');";
		stmt.executeUpdate(sql);
		
		/*
		 *  Auslesen der nach einfügen eines neuen Dozenten in DB entstandenen "größten" ID
		 *  @author: Herr Prof. Thies 
		 *  @implement: Lucas Zanella 
		 */
		sql = "SELECT MAX(ID) AS maxid FROM Raum;";
		rs = stmt.executeQuery(sql);
		
		/*
		 *  Setzen der ID dem hier aktuellen Semesterverband-Objekt
		 *  @author: Herr Prof. Thies
		 *  @implement: Lucas Zanella 
		 */
		while(rs.next()){
			raum.setId(rs.getInt("maxid"));
		}
		
		}
		
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");
		}
		
		return raum;
		
	}
	
	/*
	 * Methode um eine beliebige Anzahl an Räumen anhand Ihrerer ID's aus der
	 * DB auszulesen
	 */
	
	public Vector<Raum> findByKey(Vector<Integer> keys) throws RuntimeException {
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
		Vector<Raum> raeume = new Vector<Raum>();
		
		try{			
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Raum WHERE ID IN (" + ids.toString() + ")";
			rs = stmt.executeQuery(sql);
			
			// Befüllen des "Raum-Vectors"
			while(rs.next()){
	            Raum raum = new Raum();
	            raum.setId(rs.getInt("ID"));
	            raum.setKapazitaet(rs.getInt("Kapazitaet"));
	            raum.setBezeichnung(rs.getString("Bezeichnung"));
	            raeume.add(raum);  
	          }
			
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		return raeume;
	}
	
	public Vector<Raum> findAll() throws RuntimeException {
		
		//Einholen einer DB-Verbindung 	
				
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<Raum> raeume = new Vector<Raum>();
		
		try{			
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Raum";
			rs = stmt.executeQuery(sql);
			
			// Erstellung des "Raum-Vectors"
			while(rs.next()){
	            Raum raum = new Raum();
	            raum.setId(rs.getInt("ID"));
	            raum.setKapazitaet(rs.getInt("Kapazitaet"));
	            raum.setBezeichnung(rs.getString("Bezeichnung"));
	            raeume.add(raum);  
	          }
			
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		return raeume;
	}
	
}
