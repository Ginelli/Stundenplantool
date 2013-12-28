package com.hdm.stundenplantool2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.hdm.stundenplantool2.shared.bo.*;

/*
 * Mapperklasse um Lehrveranstaltung-Objekte aus und in die DB abzubilden
 * @author: Herr Prof. Thies
 * @implement: Lucas Zanella 
 */

public class LehrveranstaltungMapper {
	
private static LehrveranstaltungMapper lehrveranstaltungMapper = null;
	
	protected LehrveranstaltungMapper(){
		
	}
	
	public static LehrveranstaltungMapper lehrveranstaltungMapper() {
	    if (lehrveranstaltungMapper == null) {
	    	lehrveranstaltungMapper = new LehrveranstaltungMapper();
	    }

	    return lehrveranstaltungMapper;
	   }
	
	/*
	 * Methode um eine bestehende Lehrveranstaltung in der DB zu aktualisieren
	 */
	public Lehrveranstaltung update(Lehrveranstaltung lehrveranstaltung) throws RuntimeException {
		Connection con = DBConnection.connection();
		
		// Aktualisierung der Lehrveranstaltung-Entität in der DB
		
		try{
		Statement stmt = con.createStatement();
		String sql = "UPDATE Lehrveranstaltung SET Umfang='"+lehrveranstaltung.getUmfang()+"', Bezeichnung='"+lehrveranstaltung.getBezeichnung()+"', Studiensemester='"+lehrveranstaltung.getStudiensemester()+"' WHERE ID="+lehrveranstaltung.getId()+";";
		stmt.executeUpdate(sql);
		
		// Löschen der "Studiengangzuordnung" (die m zu n Beziehung zwischen Studiengang und Lehrveranstaltung)
		
		sql = "DELETE FROM Studiengangzuordnung_ZWT WHERE LehrveranstaltungID = '"+lehrveranstaltung.getId()+"';";
		stmt.executeUpdate(sql);
		
		// Aktualisierung der "Studiengangzuordnung" (die m zu n Beziehung zwischen Dozent und Belegungen)
		
		if (lehrveranstaltung.getStudiengaenge() != null) {
			for (int i = 0; i < lehrveranstaltung.getStudiengaenge().size(); i++){
				sql = "INSERT INTO Studiengangzuordnung_ZWT (`StudiengangID`, `LehrveranstaltungID`) VALUES ('"+lehrveranstaltung.getStudiengaenge().elementAt(i).getId()+"', '"+lehrveranstaltung.getId()+"');";
				stmt.executeUpdate(sql);
				}
		}
		
		// Löschen der "Dozenten-Zugehörigkeit" (die m zu n Beziehung zwischen Dozent und Lehrveranstaltung)
		
		sql = "DELETE FROM Dozentenzugehörigkeit_ZWT WHERE LehrveranstaltungID = '"+lehrveranstaltung.getId()+"';";
		stmt.executeUpdate(sql);
				
		// Aktualisierung der "Dozenten-Zugehörigkeit" (die m zu n Beziehung zwischen Dozent und Lehrveranstaltung)
		
		if (lehrveranstaltung.getDozenten() != null) {
			for (int i = 0; i < lehrveranstaltung.getDozenten().size(); i++){
				sql = "INSERT INTO Dozentenzugehörigkeit_ZWT (`LehrveranstaltungID`, `DozentID`) VALUES ('"+lehrveranstaltung.getId()+"', '"+lehrveranstaltung.getDozenten().elementAt(i).getId()+"');";
				stmt.executeUpdate(sql);
				}
		}
		
		
		//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankproblem - LehrveranstaltungMapper.update-1");		
		}
		
		return lehrveranstaltung;
	}
	
	// Löschen einer Lehrveranstaltung in der DB
	public void delete(Lehrveranstaltung lehrveranstaltung) throws RuntimeException {
		
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
						
			String sql = "DELETE FROM Dozentenzugehörigkeit_ZWT WHERE LehrveranstaltungID = '"+lehrveranstaltung.getId()+"';";
			stmt.executeUpdate(sql);
						
			sql = "DELETE FROM Studiengangzuordnung_ZWT WHERE LehrveranstaltungID = '"+lehrveranstaltung.getId()+"';";
			stmt.executeUpdate(sql);
						
			sql = "DELETE FROM Lehrveranstaltung WHERE ID = '"+lehrveranstaltung.getId()+"';";
			stmt.executeUpdate(sql);
						
						
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");
		}
								
	}
	
	// Anlegen einer neuen Lehrveranstaltung in die DB
	public Lehrveranstaltung insertIntoDB(Lehrveranstaltung lehrveranstaltung) throws RuntimeException {
		Connection con = DBConnection.connection();
		ResultSet rs;
						
		try{
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO Lehrveranstaltung (`Umfang`, `Bezeichnung`, `Studiensemester`) VALUES ('"+lehrveranstaltung.getUmfang()+"', '"+lehrveranstaltung.getBezeichnung()+"', '"+lehrveranstaltung.getStudiensemester()+"');";
		stmt.executeUpdate(sql);
		
		/*
		 *  Auslesen der nach einfügen einer neuen Lehrveranstaltung in DB entstandenen "größten" ID
		 *  @author: Herr Prof. Thies 
		 *  @implement: Lucas Zanella 
		 */
		sql = "SELECT MAX(ID) AS maxid FROM Lehrveranstaltung;";
		rs = stmt.executeQuery(sql);
		
		/*
		 *  Setzen der ID dem hier aktuellen Semesterverband-Objekt
		 *  @author: Herr Prof. Thies
		 *  @implement: Lucas Zanella 
		 */
		while(rs.next()){
			lehrveranstaltung.setId(rs.getInt("maxid"));
		}
		
		
		if(lehrveranstaltung.getStudiengaenge() != null) {
			for ( int i = 0; i < lehrveranstaltung.getStudiengaenge().size(); i++) {
				sql = "INSERT INTO Studiengangzuordnung_ZWT (`StudiengangID`, `LehrveranstaltungID`) VALUES ('"+lehrveranstaltung.getStudiengaenge().elementAt(i).getId()+"', '"+lehrveranstaltung.getId()+"');";
				stmt.executeUpdate(sql);
			}
		}
		
		if(lehrveranstaltung.getDozenten() != null) {
			for ( int i = 0; i < lehrveranstaltung.getDozenten().size(); i++) {
				sql = "INSERT INTO Dozentenzugehörigkeit_ZWT (`LehrveranstaltungID`, `DozentID`) VALUES ('"+lehrveranstaltung.getId()+"', '"+lehrveranstaltung.getDozenten().elementAt(i).getId()+"');";
				stmt.executeUpdate(sql);
			}
		}
		
		
		//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");
		}
		
		return lehrveranstaltung;
	}
	
		
	/*
	 * Methode um eine beliebige Anzahl an Lehrveranstaltungen anhand Ihrerer ID's aus der
	 * DB auszulesen
	 */
	
	public Vector<Lehrveranstaltung> findByKey(Vector<Integer> keys, Boolean loop) throws RuntimeException {
		StringBuffer ids = new StringBuffer();
		String sql;
		Statement stmt;
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
		Vector<Lehrveranstaltung> lehrveranstaltungen = new Vector<Lehrveranstaltung>();
		try{
			stmt = con.createStatement();
			sql = "SELECT * FROM Lehrveranstaltung WHERE ID IN (" + ids.toString() + ")";
			rs = stmt.executeQuery(sql);
			
			// Befüllen des "Lehrveranstaltung-Vectors"
			while(rs.next()){
				Lehrveranstaltung lehrveranstaltung = new Lehrveranstaltung();
				lehrveranstaltung.setId(rs.getInt("ID"));
				lehrveranstaltung.setUmfang(rs.getInt("Umfang"));
				lehrveranstaltung.setBezeichnung(rs.getString("Bezeichnung"));
				lehrveranstaltung.setStudiensemester(rs.getInt("Studiensemester"));
	            lehrveranstaltungen.add(lehrveranstaltung);  
	          }
		}
			catch(SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem - LehrveranstaltungMapper.findByKey-1");
			}
		
		
		try{			
			
		// Einfügen der zugehörigen Dozenten in die einzelnen Lehrveranstaltungen des "Lehrveranstaltung-Vectors"
		if (loop == true) {
			for (int i = 0; i < lehrveranstaltungen.size(); i++) {
				sql = "SELECT DozentID FROM Dozentenzugehörigkeit_ZWT WHERE LehrveranstaltungID = "+ lehrveranstaltungen.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				DozentMapper dMapper = DozentMapper.dozentMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("DozentID")); 
						
						}
					if (vi.size() > 0) {
						lehrveranstaltungen.elementAt(i).setDozenten(dMapper.findByKey(vi, false));
					}
				}
		}
		}
		catch(SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - LehrveranstaltungMapper.findByKey-2");
		}	
		
		try {
			
		// Einfügen der zugehörigen Studiengänge in die einzelnen Lehrveranstaltungen des "Lehrveranstaltung-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < lehrveranstaltungen.size(); i++) {
				sql = "SELECT StudiengangID FROM Studiengangzuordnung_ZWT WHERE LehrveranstaltungID = "+ lehrveranstaltungen.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				StudiengangMapper sMapper = StudiengangMapper.studiengangMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("StudiengangID")); 
						
						}
					if (vi.size() > 0) {
						lehrveranstaltungen.elementAt(i).setStudiengaenge(sMapper.findByKey(vi, false));
					}					
					
				}
		}
		}
		catch(SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - LehrveranstaltungMapper.findByKey-3");
		}		
		
		try {
		// Einfügen der zugehörigen Belegungen in die einzelnen Lehrveranstaltungen des "Lehrveranstaltung-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < lehrveranstaltungen.size(); i++) {
				sql = "SELECT ID FROM Belegung WHERE LehrveranstaltungID = "+ lehrveranstaltungen.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
							
				BelegungMapper bMapper = BelegungMapper.belegungMapper();
				Vector<Integer> vi = new Vector<Integer>();
							
					while(rs.next()){
								
						vi.add(rs.getInt("ID")); 
								
						}
					if (vi.size() > 0) {
						lehrveranstaltungen.elementAt(i).setBelegungen(bMapper.findByKey(vi, false));
					}					
							
				}
		}		
					
			//con.close();
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem - LehrveranstaltungMapper.findByKey-4");				
			}
		
		return lehrveranstaltungen;
	}
	
	// Auslesen aller Lehrveranstaltungen
	
	public Vector<Lehrveranstaltung> findAll(Boolean loop) throws RuntimeException {
					
		//Einholen einer DB-Verbindung und
		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<Lehrveranstaltung> lehrveranstaltungen = new Vector<Lehrveranstaltung>();
		try{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Lehrveranstaltung";
			rs = stmt.executeQuery(sql);
			
			// Befüllen des "Lehrveranstaltung-Vectors"
			while(rs.next()){
				Lehrveranstaltung lehrveranstaltung = new Lehrveranstaltung();
				lehrveranstaltung.setId(rs.getInt("ID"));
				lehrveranstaltung.setUmfang(rs.getInt("Umfang"));
				lehrveranstaltung.setBezeichnung(rs.getString("Bezeichnung"));
				lehrveranstaltung.setStudiensemester(rs.getInt("Studiensemester"));
	            lehrveranstaltungen.add(lehrveranstaltung);  
	          }
			
			
		// Einfügen der zugehörigen Dozenten in die einzelnen Lehrveranstaltungen des "Lehrveranstaltung-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < lehrveranstaltungen.size(); i++) {
				sql = "SELECT DozentID FROM Dozentenzugehörigkeit_ZWT WHERE LehrveranstaltungID = "+ lehrveranstaltungen.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				DozentMapper dMapper = DozentMapper.dozentMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("DozentID")); 
						
						}
					if (vi.size() > 0) {
						lehrveranstaltungen.elementAt(i).setDozenten(dMapper.findByKey(vi, false));
					}					
				}
		}
			
		// Einfügen der zugehörigen Studiengänge in die einzelnen Lehrveranstaltungen des "Lehrveranstaltung-Vectors"
		
		if(loop == true) {
			for (int i = 0; i < lehrveranstaltungen.size(); i++) {
				sql = "SELECT StudiengangID FROM Studiengangzuordnung_ZWT WHERE LehrveranstaltungID = "+ lehrveranstaltungen.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				StudiengangMapper sMapper = StudiengangMapper.studiengangMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("StudiengangID")); 
						
						}
					if (vi.size() > 0) {
						lehrveranstaltungen.elementAt(i).setStudiengaenge(sMapper.findByKey(vi, false));
					}					
					
				}
		}
		
		// Einfügen der zugehörigen Belegungen in die einzelnen Lehrveranstaltungen des "Lehrveranstaltung-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < lehrveranstaltungen.size(); i++) {
				sql = "SELECT ID FROM Belegung WHERE LehrveranstaltungID = "+ lehrveranstaltungen.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
							
				BelegungMapper bMapper = BelegungMapper.belegungMapper();
				Vector<Integer> vi = new Vector<Integer>();
							
					while(rs.next()){
								
						vi.add(rs.getInt("ID")); 
								
						}
					if (vi.size() > 0) {
						lehrveranstaltungen.elementAt(i).setBelegungen(bMapper.findByKey(vi, false));
					}					
							
				}
		}
				
							
			//con.close();
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		
		return lehrveranstaltungen;
		
	}

}
