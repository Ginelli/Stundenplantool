package com.hdm.stundenplantool2.server.db;

import java.sql.*;

import com.google.appengine.api.rdbms.AppEngineDriver;

/* Klasse zur Erzeugung einer Datenbankanbindung
 * @author: Herr Prof. Thies
 * @implement: Lucas Zanella
 */

public class DBConnection {

	private static Connection con = null;
	
	private static String url = "jdbc:google:rdbms://titanium-spider-370:stundenplantool/stundenplantooltest" + "," +  "stundenplantool"  + "," + "stundenplan.tool";
	
	public static Connection connection() {
		// Wenn es bisher keine Conncetion zur DB gab, ... 
		if ( con == null ) {
			try {
				// Ersteinmal muss der passende DB-Treiber geladen werden
				DriverManager.registerDriver(new AppEngineDriver());

				/*
				 * Dann erst kann uns der DriverManager eine Verbindung mit den oben
				 * in der Variable url angegebenen Verbindungsinformationen aufbauen.
				 * 
				 * Diese Verbindung wird dann in der statischen Variable con 
				 * abgespeichert und fortan verwendet.
				 */
				con = DriverManager.getConnection(url);
			} 
			catch (SQLException e1) {
				con = null;
				e1.printStackTrace();
			}
		}
		
		// Zurückgegeben der Verbindung
		return con;
	}
}
