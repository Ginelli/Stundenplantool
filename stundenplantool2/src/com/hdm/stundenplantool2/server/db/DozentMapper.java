package com.hdm.stundenplantool2.server.db;


import java.sql.*;
import java.util.Vector;

import com.hdm.stundenplantool2.shared.bo.*;

/*
 * Mapperklasse um Dozenten-Objekte aus und in die DB abzubilden
 * @author: Herr Prof. Thies
 * @implement: Lucas Zanella 
 */

public class DozentMapper {
	
	private static DozentMapper dozentMapper = null;
	
	protected DozentMapper(){
		
	}
	
	public static DozentMapper dozentMapper() {
	    if (dozentMapper == null) {
	      dozentMapper = new DozentMapper();
	    }

	    return dozentMapper;
	   }
	/*
	 * Methode um eine beliebige Anzahl an Dozenten anhand Ihrerer ID's aus der
	 * DB auszulesen
	 */
	public Vector<Dozent> findByKey(Vector<Integer> keys, Boolean loop) throws RuntimeException {
		StringBuffer ids = new StringBuffer();
		
		/*
		 * Erstellung des dynamischen Teils des SQL-Querys 
		 */	
		
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
		Vector<Dozent> dozenten = new Vector<Dozent>();
		try{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Dozent WHERE ID IN (" + ids.toString() + ")";
			rs = stmt.executeQuery(sql);
			
			// Erstellung des "Dozenten-Vectors"
			while(rs.next()){
	            Dozent dozent = new Dozent();
	            dozent.setId(rs.getInt("ID"));
	            dozent.setNachname(rs.getString("Nachname"));
	            dozent.setVorname(rs.getString("Vorname"));
	            dozent.setPersonalnummer(rs.getInt("Personalnummer"));
	            dozenten.add(dozent);  
	          }
			
			
			// Einfügen der zugehörigen Belegungen in die Dozenten des "Dozenten-Vectors"
		if (loop == true) {
			for (int i = 0; i < dozenten.size(); i++) {
				sql = "SELECT BelegungID FROM Dozentenbelegung_ZWT WHERE DozentID = "+ dozenten.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				BelegungMapper bMapper = BelegungMapper.belegungMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("BelegungID")); 
						
						}
					if (vi.size() > 0) {
						dozenten.elementAt(i).setBelegungen(bMapper.findByKey(vi, false));
					}
				}
		}
					
			// Einfügen der zugehörigen Lehrveranstaltungen in die Dozenten des "Dozenten-Vectors"
		if (loop == true) {
			for (int i = 0; i < dozenten.size(); i++) {
				sql = "SELECT LehrveranstaltungID FROM Dozentenzugehörigkeit_ZWT WHERE DozentID = "+ dozenten.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				LehrveranstaltungMapper lMapper = LehrveranstaltungMapper.lehrveranstaltungMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("LehrveranstaltungID")); 
						
						}
					if (vi.size() > 0) {
						dozenten.elementAt(i).setLehrveranstaltungen(lMapper.findByKey(vi, false));
					}					 
					
				}
				
		}
		
					
			//con.close();
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		
		return dozenten;
	}
	
	/*
	 * Methode um einen bestehenden Dozenten in der DB zu aktualisieren
	 */
	
	public Dozent update(Dozent dozent) throws RuntimeException {
		Connection con = DBConnection.connection();
		
		// Aktualisierung der Dozent-Entität in der DB
		
		try{
		Statement stmt = con.createStatement();
		String sql = "UPDATE Dozent SET Nachname='"+dozent.getNachname()+"', Vorname='"+dozent.getVorname()+"', Personalnummer='"+dozent.getPersonalnummer()+"' WHERE ID="+dozent.getId()+";";
		stmt.executeUpdate(sql);
		
		/* --> Aktuell wird es nicht möglich sein, über den Dozenten Belegungen zu ändern
		 *  Löschen der "Dozenten-Belegung" (die m zu n Beziehung zwischen Dozent und Belegungen)
		 *  >>> Funktion am 08.12.2013 deaktiviert - Erforderniss fraglich
		 
		sql = "DELETE FROM Dozentenbelegung_ZWT WHERE DozentID = '"+dozent.getId()+"';";
		stmt.executeUpdate(sql);
		
		// Aktualisierung der "Dozenten-Belegung" (die m zu n Beziehung zwischen Dozent und Belegungen)
		
		if (dozent.getBelegungen() != null) {
			for (int i = 0; i < dozent.getBelegungen().size(); i++){
				sql = "INSERT INTO Dozentenbelegung_ZWT (`DozentID`, `BelegungID`) VALUES ('"+dozent.getId()+"', '"+dozent.getBelegungen().elementAt(i).getId()+"');";
				stmt.executeUpdate(sql);
				}
		}
		*/
		
		// Löschen der "Dozenten-Zugehörigkeit" (die m zu n Beziehung zwischen Dozent und Lehrveranstaltung)
		
		sql = "DELETE FROM Dozentenzugehörigkeit_ZWT WHERE DozentID = '"+dozent.getId()+"';";
		stmt.executeUpdate(sql);
				
		// Aktualisierung der "Dozenten-Zugehörigkeit" (die m zu n Beziehung zwischen Dozent und Lehrveranstaltung)
		
		if (dozent.getLehrveranstaltungen() != null) {
			for (int i = 0; i < dozent.getLehrveranstaltungen().size(); i++){
				sql = "INSERT INTO Dozentenzugehörigkeit_ZWT (`LehrveranstaltungID`, `DozentID`) VALUES ('"+dozent.getLehrveranstaltungen().elementAt(i).getId()+"', '"+dozent.getId()+"');";
				stmt.executeUpdate(sql);
				}
		}
		
		
		//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - DozentMapper.update");
			}
		
		return dozent;
	}
	
	// Ablegen eines neuen Dozenten in die DB
	public Dozent insertIntoDB(Dozent dozent) throws RuntimeException {
		Connection con = DBConnection.connection();
		ResultSet rs;
						
		try{
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO Dozent (`Nachname`, `Vorname`, `Personalnummer`) VALUES ('"+dozent.getNachname()+"', '"+dozent.getVorname()+"', '"+dozent.getPersonalnummer()+"');";
		stmt.executeUpdate(sql);
		
		/*
		 *  Auslesen der nach einfügen eines neuen Dozenten in DB entstandenen "größten" ID
		 *  @author: Herr Prof. Thies 
		 *  @implement: Lucas Zanella 
		 */
		sql = "SELECT MAX(ID) AS maxid FROM Dozent;";
		rs = stmt.executeQuery(sql);
		
		/*
		 *  Setzen der ID dem hier aktuellen Semesterverband-Objekt
		 *  @author: Herr Prof. Thies
		 *  @implement: Lucas Zanella 
		 */
		while(rs.next()){
			dozent.setId(rs.getInt("maxid"));
		}
		
		if(dozent.getBelegungen() != null) {
			for ( int i = 0; i < dozent.getBelegungen().size(); i++) {
				sql = "INSERT INTO Dozentenbelegung_ZWT (`DozentID`, `BelegungID`) VALUES ('"+dozent.getId()+"', '"+dozent.getBelegungen().elementAt(i).getId()+"');";
				stmt.executeUpdate(sql);
			}
		}
		
		if(dozent.getLehrveranstaltungen() != null) {
			for ( int i = 0; i < dozent.getBelegungen().size(); i++) {
				sql = "INSERT INTO Dozentenzugehörigkeit_ZWT (`LehrveranstaltungID`, `DozentID`) VALUES ('"+dozent.getLehrveranstaltungen().elementAt(i).getId()+"', '"+dozent.getId()+"');";
				stmt.executeUpdate(sql);
			}
		}
		
		
		//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");
		}
		
		return dozent;
	}
	
	// Alle Dozenten aus der DB auslesen
	
	public Vector<Dozent> findAll(Boolean loop) throws RuntimeException {
				
		Connection con = DBConnection.connection();
		
		Vector<Dozent> dozenten = new Vector<Dozent>();
		
		try{
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM Dozent;";
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			Dozent dozent = new Dozent();
            dozent.setId(rs.getInt("ID"));
            dozent.setNachname(rs.getString("Nachname"));
            dozent.setVorname(rs.getString("Vorname"));
            dozent.setPersonalnummer(rs.getInt("Personalnummer"));
            dozenten.add(dozent); 
		}
		
				
		// Einfügen der zugehörigen Belegungen in die Dozenten des "Dozenten-Vectors"
		
		/*
		if (loop == true) {
			for (int i = 0; i < dozenten.size(); i++) {
				sql = "SELECT BelegungID FROM Dozentenbelegung_ZWT WHERE DozentID = "+ dozenten.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				BelegungMapper bMapper = BelegungMapper.belegungMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("BelegungID")); 
						
						}
					if (vi.size() > 0) {
						dozenten.elementAt(i).setBelegungen(bMapper.findByKey(vi, false));
					}					 
				}
		}
		*/

			
			// Einfügen der zugehörigen Lehrveranstaltungen in die Dozenten des "Dozenten-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < dozenten.size(); i++) {
				sql = "SELECT LehrveranstaltungID FROM Dozentenzugehörigkeit_ZWT WHERE DozentID = "+ dozenten.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				LehrveranstaltungMapper lMapper = LehrveranstaltungMapper.lehrveranstaltungMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("LehrveranstaltungID")); 
						
						}
					if (vi.size() > 0) {
						dozenten.elementAt(i).setLehrveranstaltungen(lMapper.findByKey(vi, false));
					}					 
					
				}
		}
				
		//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");		
		}
		
				
		return dozenten;
			
	}
	
	// Löschen eines Dozenten aus der DB
	public void delete(Dozent dozent) throws RuntimeException {
		
		// Ein Dozent kann nur gelöscht werden, wenn er durch keine Belegungen mehr referenziert ist
		if (dozent.getBelegungen() == null) {
			Connection con = DBConnection.connection();
			try {
				Statement stmt = con.createStatement();
				
				/*
				 *  Löschen der "Dozenten-Zugehörigkeit" durch Ersetzen (die m zu n Beziehung zwischen Dozent und Lehrveranstaltung)
				 *  Die betroffenen Lehrveranstaltungen erhalten einen "default-Dozenten" mit der ID = 13, welcher nicht gültig ist 
				 *  und nur als Platzhalter dient. (Belegungen mit "default-Dozent" sind nicht zulässig und werden verhindert)
				 */
				
				String sql = "UPDATE Dozentenzugehörigkeit_ZWT SET DozentID='13' WHERE DozentID="+dozent.getId()+";";
				stmt.executeUpdate(sql);
				
				sql = "DELETE FROM Dozent WHERE ID = '"+dozent.getId()+"';";
				stmt.executeUpdate(sql);
				
				
				
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");
			}
		}
	}
	

}
