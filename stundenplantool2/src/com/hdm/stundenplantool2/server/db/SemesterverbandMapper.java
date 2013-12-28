package com.hdm.stundenplantool2.server.db;

import java.sql.*;
import java.util.Vector;

import com.hdm.stundenplantool2.shared.bo.*;

/*
 * Mapperklasse um Semesterverband-Objekte aus und in die DB abzubilden
 * @author: Herr Prof. Thies
 * @implement: Lucas Zanella 
 */

public class SemesterverbandMapper {
	
	private static SemesterverbandMapper semesterverbandMapper = null;
	
	protected SemesterverbandMapper(){
		
	}
	
	public static SemesterverbandMapper semesterverbandMapper() {
	    if (semesterverbandMapper == null) {
	    	semesterverbandMapper = new SemesterverbandMapper();
	    }

	    return semesterverbandMapper;
	   }
	
	/*
	 * Methode um eine beliebige Anzahl an Semesterverbände anhand Ihrerer ID's aus der
	 * DB auszulesen
	 */
	public Vector<Semesterverband> findByKey(Vector<Integer> keys, Boolean loop) throws RuntimeException {
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
		Vector<Semesterverband> semesterverbaende = new Vector<Semesterverband>();
		try{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Semesterverband WHERE ID IN (" + ids.toString() + ")";
			rs = stmt.executeQuery(sql);
			
			// Befüllen des "Semesterverband-Vectors"
			while(rs.next()){
				Semesterverband semesterverband = new Semesterverband();
				
				StudiengangMapper sMapper = StudiengangMapper.studiengangMapper();
				Vector<Integer> vI = new Vector<Integer>();
				
				semesterverband.setId(rs.getInt("Anzahl Studenten"));
				semesterverband.setJahrgang(rs.getString("Jahrgang"));
				vI.add(rs.getInt("StudiengangID"));
				semesterverband.setStudiengang(sMapper.findByKey(vI, false).elementAt(0));
				semesterverbaende.add(semesterverband);  
	          }
			
			
		// Einfügen der zugehörigen Belegungen in die Semesterverbände des "Semesterverband-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < semesterverbaende.size(); i++) {
				sql = "SELECT BelegungID FROM Semesterverbandszugehörigkeit_ZWT WHERE SemesterverbandID = "+ semesterverbaende.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				BelegungMapper bMapper = BelegungMapper.belegungMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("BelegungID")); 
						
						}
					if (vi.size() > 0) {
						semesterverbaende.elementAt(i).setBelegungen(bMapper.findByKey(vi, false));
					}
					}
				}
		
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		
		return semesterverbaende;
	}
	
	public Vector<Semesterverband> findAll(Boolean loop)  throws RuntimeException {
		
		//Einholen einer DB-Verbindung und
		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<Semesterverband> semesterverbaende = new Vector<Semesterverband>();
		try{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Semesterverband";
			rs = stmt.executeQuery(sql);
			
			// Befüllen des "Semesterverband-Vectors"
			while(rs.next()){
				Semesterverband semesterverband = new Semesterverband();
				
				StudiengangMapper sMapper = StudiengangMapper.studiengangMapper();
				Vector<Integer> vI = new Vector<Integer>();
				
				semesterverband.setId(rs.getInt("ID"));
				semesterverband.setAnzahlStudenten(rs.getInt("Anzahl Studenten"));
				semesterverband.setJahrgang(rs.getString("Jahrgang"));
				vI.add(rs.getInt("StudiengangID"));
				semesterverband.setStudiengang(sMapper.findByKey(vI, false).elementAt(0));
				semesterverbaende.add(semesterverband);  
	          }
			
			
		// Einfügen der zugehörigen Belegungen in die Semesterverbände des "Semesterverband-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < semesterverbaende.size(); i++) {
				sql = "SELECT BelegungID FROM Semesterverbandszugehörigkeit_ZWT WHERE SemesterverbandID = "+ semesterverbaende.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				BelegungMapper bMapper = BelegungMapper.belegungMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("BelegungID")); 
						
						}
					if (vi.size() > 0) {
						semesterverbaende.elementAt(i).setBelegungen(bMapper.findByKey(vi, false));
					}					
					}
				
				}
					
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		
		return semesterverbaende;
		
	}
	
	public Semesterverband update(Semesterverband semesterverband) throws RuntimeException {
		Connection con = DBConnection.connection();
		
		// Aktualisierung der Semesterverband-Entität in der DB
		
		try{
		Statement stmt = con.createStatement();
		String sql = "UPDATE Semesterverband SET Anzahl Studenten='"+semesterverband.getAnzahlStudenten()+"', Jahrgang='"+semesterverband.getJahrgang()+"', StudiengangID='"+semesterverband.getStudiengang().getId()+"' WHERE ID="+semesterverband.getId()+";";
		stmt.executeUpdate(sql);
		
		// Löschen der "Semesterverbandszugehörigkeit" (die m zu n Beziehung zwischen Semesterverband und Belegungen)
		
		sql = "DELETE FROM Semesterverbandszugehörigkeit_ZWT WHERE SemesterverbandID = '"+semesterverband.getId()+"';";
		stmt.executeUpdate(sql);
		
		// Aktualisierung der "Semesterverbandszugehörigkeit" (die m zu n Beziehung zwischen Dozent und Belegungen)
		
		if (semesterverband.getBelegungen() != null) {
			for (int i = 0; i < semesterverband.getBelegungen().size(); i++){
				sql = "INSERT INTO Semesterverbandszugehörigkeit_ZWT (`SemesterverbandID`, `BelegungID`) VALUES ('"+semesterverband.getId()+"', '"+semesterverband.getBelegungen().elementAt(i).getId()+"');";
				stmt.executeUpdate(sql);
				}
		}
		
			//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");			
		}
		
		return semesterverband;
	}
	
	public void delete(Semesterverband semesterverband) throws RuntimeException {

					Connection con = DBConnection.connection();
					try {
						Statement stmt = con.createStatement();
						
						String sql = "DELETE FROM Semesterverband WHERE ID = '"+semesterverband.getId()+"';";
						stmt.executeUpdate(sql);
												
						
					}
					catch (SQLException e1) {
						throw new RuntimeException("Datenbankbankproblem");
					}
				
		
	}
	
	// Ablegen eines neuen Semesterverbands in die DB
		
	public Semesterverband insertIntoDB(Semesterverband semesterverband) throws RuntimeException {
		Connection con = DBConnection.connection();
		ResultSet rs;
		
		try{
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO Semesterverband (`Anzahl Studenten`, `Jahrgang`, `StudiengangID`) VALUES ('"+semesterverband.getAnzahlStudenten()+"', '"+semesterverband.getJahrgang()+"', '"+semesterverband.getStudiengang().getId()+"');";
		stmt.executeUpdate(sql);
		
		// Auslesen der nach einfügen eines neuen Semesterverbandes in DB entstandenen "größten" ID
		sql = "SELECT MAX(ID) AS maxid FROM Semesterverband;";
		rs = stmt.executeQuery(sql);
		
		// Setzen der ID dem hier aktuellen Semesterverband-Objekt
		while(rs.next()){
			semesterverband.setId(rs.getInt("maxid"));
		}
		
		// Setzen der Semesterverbandszugehörigkeit (die m zu n Beziehung zwischen Semesterverband und Belegung)
		if(semesterverband.getBelegungen() != null) {
			for ( int i = 0; i < semesterverband.getBelegungen().size(); i++) {
				sql = "INSERT INTO Semesterverbandszugehörigkeit_ZWT (`SemesterverbandID`, `BelegungID`) VALUES ('"+semesterverband.getId()+"', '"+semesterverband.getBelegungen().elementAt(i).getId()+"');";
				stmt.executeUpdate(sql);
			}
		}
		
		
		//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");
		}
		
		return semesterverband;
	}
}
