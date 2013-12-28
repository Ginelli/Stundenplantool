package com.hdm.stundenplantool2.server.db;

import java.sql.*;
import java.util.Vector;

import com.hdm.stundenplantool2.shared.bo.*;

public class StudiengangMapper {
	
private static StudiengangMapper studiengangMapper = null;
	
	protected StudiengangMapper(){
		
	}
	
	public static StudiengangMapper studiengangMapper() {
	    if (studiengangMapper == null) {
	    	studiengangMapper = new StudiengangMapper();
	    }

	    return studiengangMapper;
	   }
	
	/*
	 * Methode um eine beliebige Anzahl an Studieng�ngen anhand Ihrerer ID's aus der
	 * DB auszulesen
	 */
	public Vector<Studiengang> findByKey(Vector<Integer> keys, Boolean loop) throws RuntimeException {
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
		Vector<Studiengang> studiengaenge = new Vector<Studiengang>();
		try{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Studiengang WHERE ID IN (" + ids.toString() + ")";
			rs = stmt.executeQuery(sql);
			
			// Bef�llen des "Studiengang-Vectors"
			while(rs.next()){
				Studiengang studiengang = new Studiengang();
				studiengang.setId(rs.getInt("ID"));
				studiengang.setBezeichnung(rs.getString("Bezeichnung"));
				studiengang.setKuerzel(rs.getString("Kuerzel"));
				studiengaenge.add(studiengang);  
	          }
			
			
			// Einf�gen der zugeh�rigen Semesterverb�nde in die einzelenen Studieng�nge des "Studiengang-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < studiengaenge.size(); i++) {
				sql = "SELECT ID FROM Semesterverband WHERE StudiengangID = "+ studiengaenge.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				SemesterverbandMapper svMapper = SemesterverbandMapper.semesterverbandMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("ID")); 
						
						}
					if (vi.size() > 0) {
						studiengaenge.elementAt(i).setSemesterverbaende(svMapper.findByKey(vi, false));
					}					
				}
		}
			
			// Einf�gen der zugeh�rigen Lehrveranstaltungen in die einzelenen Studieng�nge des "Studiengang-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < studiengaenge.size(); i++) {
				sql = "SELECT LehrveranstaltungID FROM Studiengangzuordnung_ZWT WHERE StudiengangID = "+ studiengaenge.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				LehrveranstaltungMapper lMapper = LehrveranstaltungMapper.lehrveranstaltungMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("LehrveranstaltungID")); 
						
						}
					if (vi.size() > 0) {
						studiengaenge.elementAt(i).setLehrveranstaltungen(lMapper.findByKey(vi, false));
					}					
					
				}
		}
					
			//con.close();
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		
		return studiengaenge;
	}
	
	// Alle Studieng�nge aus der DB auslesen
	
	public Vector<Studiengang> findAll(Boolean loop) throws RuntimeException {
        			
		//Einholen einer DB-Verbindung und
		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<Studiengang> studiengaenge = new Vector<Studiengang>();
		try{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Studiengang";
			rs = stmt.executeQuery(sql);
			
			// Bef�llen des "Studiengang-Vectors"
			while(rs.next()){
				Studiengang studiengang = new Studiengang();
				studiengang.setId(rs.getInt("ID"));
				studiengang.setBezeichnung(rs.getString("Bezeichnung"));
				studiengang.setKuerzel(rs.getString("Kuerzel"));
				studiengaenge.add(studiengang);  
	          }
			
			
			// Einf�gen der zugeh�rigen Semesterverb�nde in die einzelenen Studieng�nge des "Studiengang-Vectors"
		if (loop == true) {
			for (int i = 0; i < studiengaenge.size(); i++) {
				sql = "SELECT ID FROM Semesterverband WHERE StudiengangID = "+ studiengaenge.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				SemesterverbandMapper svMapper = SemesterverbandMapper.semesterverbandMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("ID")); 
						
						}
					if (vi.size() > 0) {
						studiengaenge.elementAt(i).setSemesterverbaende(svMapper.findByKey(vi, false));
					}					
				}
		}
			
		// Einf�gen der zugeh�rigen Lehrveranstaltungen in die einzelenen Studieng�nge des "Studiengang-Vectors"
		
		if (loop == true) {
			for (int i = 0; i < studiengaenge.size(); i++) {
				sql = "SELECT LehrveranstaltungID FROM Studiengangzuordnung_ZWT WHERE StudiengangID = "+ studiengaenge.elementAt(i).getId();
				rs = stmt.executeQuery(sql);
					
				LehrveranstaltungMapper lMapper = LehrveranstaltungMapper.lehrveranstaltungMapper();
				Vector<Integer> vi = new Vector<Integer>();
					
					while(rs.next()){
						
						vi.add(rs.getInt("LehrveranstaltungID")); 
						
						}
					if (vi.size() > 0) {
						studiengaenge.elementAt(i).setLehrveranstaltungen(lMapper.findByKey(vi, false));
					}					
					
				}
		}
					
			//con.close();
			}
			catch (SQLException e1) {
				throw new RuntimeException("Datenbankbankproblem");				
			}
		
		return studiengaenge;
	}
	
	public Studiengang update(Studiengang studiengang) throws RuntimeException {
		Connection con = DBConnection.connection();
		
		// Aktualisierung der Dozent-Entit�t in der DB
		
		try{
		Statement stmt = con.createStatement();
		String sql = "UPDATE Studiengang SET Bezeichnung='"+studiengang.getBezeichnung()+"', Kuerzel='"+studiengang.getKuerzel()+"' WHERE ID="+studiengang.getId()+";";
		stmt.executeUpdate(sql);
		
		// L�schen der "Studiengangzuordnung" (die m zu n Beziehung zwischen Studiengang und Lehrveranstaltung)		
				
		sql = "DELETE FROM Studiengangzuordnung_ZWT WHERE StudiengangID = '"+studiengang.getId()+"';";
		stmt.executeUpdate(sql);
				
		// Aktualisierung der "Studiengangzuordnung" (die m zu n Beziehung zwischen Studiengang und Lehrveranstaltung)
		
		if (studiengang.getLehrveranstaltungen() != null) {
			for (int i = 0; i < studiengang.getLehrveranstaltungen().size(); i++){
				sql = "INSERT INTO Studiengangzuordnung_ZWT (`StudiengangID`, `LehrveranstaltungID`) VALUES ('"+studiengang.getId()+"', '"+studiengang.getLehrveranstaltungen().elementAt(i).getId()+"');";
				stmt.executeUpdate(sql);
				}
		}
		
		
		//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");		
		}
		
		return studiengang;
	}
	
	// L�schen eines Studiengangs
	
	public void delete(Studiengang studiengang) throws RuntimeException {
		/*
		 *  Ein Studiengang kann nur gel�scht werden, wenn er durch keine Lehrveranstaltungen und 
		 *  Semesterverb�nde mehr referenziert wird
		 */
				if (studiengang.getLehrveranstaltungen() == null && studiengang.getSemesterverbaende() == null ) {
					Connection con = DBConnection.connection();
					try {
						Statement stmt = con.createStatement();
												
						String sql = "DELETE FROM Studiengang WHERE ID = '"+studiengang.getId()+"';";
						stmt.executeUpdate(sql);
						
						
					}
					catch (SQLException e1) {
						throw new RuntimeException("Datenbankbankproblem");
					}
				}
	}
	
	// Ablegen eines neuen Studiegangs in die DB
	public Studiengang insertIntoDB(Studiengang studiengang) throws RuntimeException {
		Connection con = DBConnection.connection();
		ResultSet rs;
						
		try{
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO Studiengang (`Bezeichnung`, `Kuerzel`) VALUES ('"+studiengang.getBezeichnung()+"', '"+studiengang.getKuerzel()+"');";
		stmt.executeUpdate(sql);
		
		/*
		 *  Auslesen der nach einf�gen eines neuen Dozenten in DB entstandenen "gr��ten" ID
		 *  @author: Herr Prof. Thies 
		 *  @implement: Lucas Zanella 
		 */
		sql = "SELECT MAX(ID) AS maxid FROM Studiengang;";
		rs = stmt.executeQuery(sql);
		
		/*
		 *  Setzen der ID dem hier aktuellen Semesterverband-Objekt
		 *  @author: Herr Prof. Thies
		 *  @implement: Lucas Zanella 
		 */
		while(rs.next()){
			studiengang.setId(rs.getInt("maxid"));
		}
		
		if(studiengang.getLehrveranstaltungen() != null) {
			for ( int i = 0; i < studiengang.getLehrveranstaltungen().size(); i++) {
				sql = "INSERT INTO Studiengangzuordnung_ZWT (`StudiengangID`, `LehrveranstaltungID`) VALUES ('"+studiengang.getId()+"', '"+studiengang.getLehrveranstaltungen().elementAt(i).getId()+"');";
				stmt.executeUpdate(sql);
			}
		}
		
		//con.close();
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem");
		}
		
		return studiengang;
	}

}
