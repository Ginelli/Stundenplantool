package com.hdm.stundenplantool2.server;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.hdm.stundenplantool2.server.db.*;
import com.hdm.stundenplantool2.shared.Verwaltung;
import com.hdm.stundenplantool2.shared.bo.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class VerwaltungImpl extends RemoteServiceServlet implements Verwaltung {
	
	BelegungMapper belegungMapper = BelegungMapper.belegungMapper();
	DozentMapper dozentMapper = DozentMapper.dozentMapper();
	LehrveranstaltungMapper lehrveranstaltungMapper = LehrveranstaltungMapper.lehrveranstaltungMapper();
	RaumMapper raumMapper = RaumMapper.raumMapper();
	SemesterverbandMapper semesterverbandMapper = SemesterverbandMapper.semesterverbandMapper();
	StudiengangMapper studiengangMapper= StudiengangMapper.studiengangMapper();
	ZeitslotMapper zeitslotMapper = ZeitslotMapper.zeitslotMapper();

	// ------------------------------------------------------------------------------------------------------------------------------------
	
	
	// Test-Methode!
	public String getAntwort(Vector<Integer> keys) throws IllegalArgumentException {
		Vector<Dozent> dozenten = dozentMapper.findByKey(keys, true);
		return vectorToString(dozenten);
		
		
	}
	
	private String vectorToString(Vector<Dozent> dozenten) {
		int index = dozenten.size();
		StringBuffer result = new StringBuffer();
		
		for (int i = 0; i < index; i++){
			result.append(dozenten.elementAt(i).getNachname());
			result.append(dozenten.elementAt(i).getLehrveranstaltungen().elementAt(0).getBezeichnung());
			result.append(dozenten.elementAt(i).getBelegungen().elementAt(0).getRaum().getBezeichnung());
		}
		
		return result.toString();
		
	}
	
	// ------------------------------------------------------------------------------------------------------------------------------------
	
	/*
	 * Auslesen der Business-Objects ---------------------------------------------------------------------------------------------------------------------------
	 */
	
	public Vector<Semesterverband> auslesenAlleSemesterverbaende() throws RuntimeException {
		return semesterverbandMapper.findAll(true);
	}
	
	public Vector<Dozent> auslesenAlleDozenten() throws RuntimeException {
		return dozentMapper.findAll(false);
	}
	
	public Vector<Dozent> auslesenDozent(Dozent dozent) throws RuntimeException {
		Vector<Integer> vi = new Vector<Integer>();
		vi.add(dozent.getId());
		return dozentMapper.findByKey(vi, true);
	}
	
	public Vector<Zeitslot> auslesenAlleZeitslots() throws RuntimeException {
		return zeitslotMapper.findAll();
	}
	
	public Vector<Lehrveranstaltung> auslesenAlleLehrveranstaltungen() throws RuntimeException {
		return lehrveranstaltungMapper.findAll(false);
	}
	
	public Vector<Belegung> auslesenAlleBelegungen() throws RuntimeException {
		return belegungMapper.findAll(false);
	}
	
	public Vector<Studiengang> auslesenAlleStudiengaenge() throws RuntimeException {
		return studiengangMapper.findAll(false);
	}
	
	public Vector<Raum> auslesenAlleRaeume() throws RuntimeException {
		return raumMapper.findAll();
	}
	
	/*
	 * Löschen der Business-Objects ---------------------------------------------------------------------------------------------------------------------------
	 */
	
	public void loeschenSemesterverband(Semesterverband semesterverband) throws RuntimeException {
		
		// Ein Semesterverband kann nur gelöscht werden, wenn er durch keine Belegungen mehr referenziert ist
		
		if (semesterverband.getBelegungen() == null) {
			semesterverbandMapper.delete(semesterverband);
		}
		else {
			StringBuffer eText = new StringBuffer();
			eText.append("Bitte loeschen Sie zuerst alle Belegungen von " + semesterverband.getStudiengang().getKuerzel() + " " + semesterverband.getJahrgang()+ "\n");
			eText.append("Folgende Belegungen wurden identifiziert:\n");
			eText.append("\n");
			for(int i = 0; i < semesterverband.getBelegungen().size(); i++) {
				eText.append(semesterverband.getBelegungen().elementAt(i).getZeitslot().getWochentag() + "\t\t");
				eText.append(semesterverband.getBelegungen().elementAt(i).getLehrveranstaltung().getBezeichnung());
				eText.append("\n");
			}			
			throw new RuntimeException(eText.toString());
		}
	}
	
	public void loeschenDozent(Dozent dozent) throws RuntimeException {
		
		// Ein Dozent kann nur gelöscht werden, wenn er durch keine Belegungen mehr referenziert ist
		
		if (dozent.getBelegungen() == null) {
			dozentMapper.delete(dozent);
		}
		else {			
			throw new RuntimeException("Bitte loeschen Sie zuerst alle Belegungen von " + dozent.getTitel() + " " + dozent.getVorname() + " " + dozent.getNachname());
		}		
	}
	
	public void loeschenZeitslot(Zeitslot zeitslot) throws RuntimeException {
		// Das Löschen eines Zeitslots ist bis dato nicht vorgesehen Stand: 06.12.2013
	}
	
	public void loeschenLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) throws RuntimeException {
		
		// Ein Dozent kann nur gelöscht werden, wenn er durch keine Belegungen mehr referenziert ist
		
		if (lehrveranstaltung.getBelegungen() == null) {
			lehrveranstaltungMapper.delete(lehrveranstaltung);
		}
		else {			
			throw new RuntimeException("Bitte loeschen Sie zuerst alle Belegungen von " + lehrveranstaltung.getBezeichnung());
		}	
	}
	
	public void loeschenBelegungen(Belegung belegung) throws RuntimeException {
		belegungMapper.delete(belegung);
	}
	
	public void loeschenStudiengang(Studiengang studiengang) throws RuntimeException {
		
		// Ein Studiengang kann nur gelöscht werden, wenn er durch keine Lehrveranstaltungen und Semesterverbaende mehr referenziert wird
		
		if(studiengang.getLehrveranstaltungen() == null && studiengang.getSemesterverbaende() == null) {
			studiengangMapper.delete(studiengang);
		}
		else {
			StringBuffer eText = new StringBuffer();
			eText.append("Bitte loeschen Sie zuerst alle Referenzen auf " + studiengang.getBezeichnung() + "\n\n");
			if (studiengang.getLehrveranstaltungen() != null) {
				eText.append("Folgende Lehrveranstaltungen sind verknüpft:\n");
				for (int i = 0; i < studiengang.getLehrveranstaltungen().size(); i++) {
					eText.append(studiengang.getLehrveranstaltungen().elementAt(i).getBezeichnung() + "\n");					
				}
				eText.append("\n\n");
			}
			if (studiengang.getSemesterverbaende() != null) {
				eText.append("Folgende Jahrgaenge sind verknüpft:\n");
				for (int i = 0; i < studiengang.getSemesterverbaende().size(); i++) {
					eText.append(studiengang.getSemesterverbaende().elementAt(i).getJahrgang() + "\n");					
				}
			}
			throw new RuntimeException(eText.toString());
		}		
	}
	
	public void loeschenRaum(Raum raum) throws RuntimeException {
		
		// Ein Raum kann nur gelöscht werden, wenn er durch keine Belegungen mehr referenziert ist
		
		if (this.belegungMapper.findByRaum(raum) == null) {
			raumMapper.delete(raum);
		}
		else {			
			throw new RuntimeException("Bitte loeschen Sie zuerst alle Belegungen von Raum" + raum.getBezeichnung());
		}
	}
	
	/*
	 * Ändern der Business-Objects ---------------------------------------------------------------------------------------------------------------------------
	 */
	
	public Semesterverband aendernSemesterverband(Semesterverband semesterverband) throws RuntimeException {
		
		StringBuffer jahrgang = new StringBuffer();
		jahrgang.append(semesterverband.getJahrgang());
		
		// Es wird geprüft, ob der Jahrgang semantisch und syntaktisch korrekt eingegeben wurde
		
		if (jahrgang.capacity() == 0) {
			throw new IllegalArgumentException("Das Feld \"Jahrgang\" darf nicht leer sein"); 
		}
		
		if (!jahrgang.substring(0,2).equals("SS") && !jahrgang.substring(0,2).equals("WS") || !jahrgang.substring(2,4).equals("20")) {
			throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben\nBeachten Sie auch die Gross-/Kleinschreibweise");			
		}
		
		if ((jahrgang.substring(0,2).equals("SS") && jahrgang.capacity() != 6) || (jahrgang.substring(0,2).equals("WS") && jahrgang.capacity() != 9) ||
				(jahrgang.substring(0,2).equals("WS") && jahrgang.charAt(6) != new Character('/'))) {
				throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben");
		}
		
		if (jahrgang.substring(0,2).equals("SS")) {
			try {
				Integer.parseInt(jahrgang.substring(4,6));
			}
			catch (NumberFormatException e1) {
				throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben");
			}
		}
		
		if (jahrgang.substring(0,2).equals("WS")) {
			int vJahr;
			int nJahr;
			try {
				vJahr = Integer.parseInt(jahrgang.substring(4,6));
				nJahr = Integer.parseInt(jahrgang.substring(7,9));
			}
			catch (NumberFormatException e1) {
				throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben");
			}
			if (nJahr - vJahr != 1) {
				throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben\nNur ein Jahreswechsel erlaubt");
			}
		}
		
		// Ein Semsterverband kann erst gelöscht werden, wenn er keinen Belegungen mehr zugeordnet ist
		
		if (this.belegungMapper.findBySemesterverband(semesterverband) == null) {
			this.semesterverbandMapper.update(semesterverband);
		}
		else {
			throw new RuntimeException("Bitte loeschen Sie zuerst alle Belegungen von " + semesterverband.getStudiengang().getKuerzel() + " " + semesterverband.getJahrgang());
		}
			
			return semesterverband;
	}
	
	public Dozent aendernDozent(Dozent dozent) throws RuntimeException {
		
		// Prüfung ob Vor- und Nachname angegeben wurden
		
		StringBuffer tempVorname = new StringBuffer();
		tempVorname.append(dozent.getVorname());
		StringBuffer tempNachname = new StringBuffer();
		tempNachname.append(dozent.getNachname());
		
						
		if ((tempVorname.length() == 0) || (tempNachname.length() == 0)) {
			throw new IllegalArgumentException("Bitte geben Sie Vor- und Nachname an");
		}
		
		
		// Prüfung des Vor- und Nachnamens auf Zahlen und bestimmte Sonderzeichen, diese sind nicht erlaubt
		
		if (!dozent.getVorname().matches("[^0-9\\,\\_\\+\\*\\/\\=\\}\\{\\[\\]\\%\\$\\§\\\"\\!\\^\\°\\<\\>\\|\\;\\:\\#\\~\\@\\€\\?\\(\\)\\²\\³]*") || 
				!dozent.getNachname().matches("[^0-9\\,\\_\\+\\*\\/\\=\\}\\{\\[\\]\\%\\$\\§\\\"\\!\\^\\°\\<\\>\\|\\;\\:\\#\\~\\@\\€\\?\\(\\)\\²\\³]*")) {
			throw new IllegalArgumentException("Es befinden sich nicht erlaubte Zeichen im Vor- bzw. Nachnamen");
		}
		
		// Prüfung der Personalnummer auf fünfstellige Ziffernfolge
		
		if (!new Integer(dozent.getPersonalnummer()).toString().matches("[0-9]{5}")) {
			throw new IllegalArgumentException("Die Personalnummer ist nicht fuenfstellig\noder es befinden sich darin nicht erlaubte Zeichen");
		}
		
		// Laden der "alten" Version des Dozenten um einen Vergleich der Lehrveranstaltungen zu ermöglichen
		Vector<Integer> dozentID = new Vector<Integer>();
		dozentID.add(dozent.getId());
		Vector<Dozent> oldDozent = this.dozentMapper.findByKey(dozentID, true);
		
		// Vector mit ID's der Lehrveranstaltungen, welche nicht mehr vom Dozenten gehalten werden
		Vector<Integer> tempLehrveranstalungIDs = new Vector<Integer>();
		
		// Auslesen der ID's derer Lehrveranstaltungen, welche nicht mehr vom Dozenten gehalten werden
		
		Integer a = null;
		
		for (int i = 0; i < oldDozent.elementAt(0).getLehrveranstaltungen().size(); i++) {
			for (int j = 0; j < dozent.getLehrveranstaltungen().size(); j++) {
				if (oldDozent.elementAt(0).getLehrveranstaltungen().elementAt(i).getId() != dozent.getLehrveranstaltungen().elementAt(j).getId()) {
					a = oldDozent.elementAt(0).getLehrveranstaltungen().elementAt(i).getId();
				}
				else {
					a = null;
					break;
				}
			}
			if (a != null) {
				tempLehrveranstalungIDs.add(a);
				a = null;
			}
		}
		
		/*
		 *  Prüfwert, falls Referenzen vom Dozent auf bestimmte Lehrveranstaltungen nicht gelöscht werden können,
		 *  da sie Belegungen mit einzig "diesem" Dozenten referenzieren -> false = kann nicht gelöscht werden
		 */
		
		Boolean check = true;
		
		StringBuffer eText = new StringBuffer("Die nicht mehr gehaltenen Lehrveranstaltungen können nicht gelöscht werden.\nFolgende Belegungen muessen zuerst "
				+ "entfernt oder von einem anderen Dozenten übernommen werden: \n\nID\t\t\tWochentag\t\t\tZeit\t\t\tLV\n");
		
		// Zuerst wird geprüft, ob der Dozent bestimmte Lehrveranstaltungen nun nicht mehr hält		
		if (tempLehrveranstalungIDs.size() > 0) {
			// Die Lehrveranstaltungen, die der Dozent nicht halten soll bzw. wird, werden nun geladen
			Vector<Lehrveranstaltung> tempLehrveranstalungen = this.lehrveranstaltungMapper.findByKey(tempLehrveranstalungIDs, true);
			// Jede dieser Lehrveranstaltungen wird nun geprüft, ob sie Belegungen enthält
			for (int i = 0; i < tempLehrveranstalungen.size(); i++) {
				if (tempLehrveranstalungen.elementAt(i).getBelegungen() != null) {
					// Sollte eine Lehrveranstaltung Belegungen referenzieren, werden diese nun geladen
					Vector<Belegung> tempBelegungen = this.belegungMapper.findByLehrveranstaltung(tempLehrveranstalungen.elementAt(i));
					/*
					 *  Bei jeder Belegung wird schließlich geprüft, ob sie nur von einem Dozenten durchgeführt wird 
					 *  und ob es sich dabei um den hier (Methoden-Parameter) zu ändernden Dozenten hält.
					 *  Sollte dies der Fall sein, kann eine Änderung nicht durchgeführt werden, da zuerst die
					 *  Belegungen der zu löschenden Lehrveranstaltung gelöscht werden müssen oder aber sie
					 *  werden von einem anderen Dozenten gehalten
					 */
					for (int j = 0; j < tempBelegungen.size(); j++) {
						if(tempBelegungen.elementAt(j).getDozenten().size() <= 1 && (tempBelegungen.elementAt(j).getDozenten().elementAt(0).getId() == dozent.getId())) {
							check = false;
							eText.append(tempBelegungen.elementAt(j).getZeitslot().getWochentag() + " " + tempBelegungen.elementAt(j).getZeitslot().getAnfangszeit() + "\t\t");
							eText.append(tempLehrveranstalungen.elementAt(i).getBezeichnung() + "\n");
						}
					}
				}
			}
		}
		
		if(!check) {
			throw new RuntimeException(eText.toString());
		}
		else {
			this.dozentMapper.update(dozent);
			return dozent;
		}
	}
	
	public Zeitslot aendernZeitslot(Zeitslot zeitslot) throws RuntimeException {
		
		// Prüfung der Zeitangaben auf syntaktische Korrektheit
		if (!new Integer(zeitslot.getAnfangszeit()).toString().matches("[0-9]{3,4}") || !new Integer(zeitslot.getEndzeit()).toString().matches("[0-9]{3,4}")) {
			throw new IllegalArgumentException("Zeiten duerfen nur Zahlen von 0 bis 9 enthalten und müssen sich zwischen 08:15 und 21:00 Uhr befinden");
		}
		
		// Prüfung des Wochentags auf semantische Korrektheit
		if (!zeitslot.getWochentag().equals("Montag") || !zeitslot.getWochentag().equals("Dienstag") || !zeitslot.getWochentag().equals("Mittwoch") || 
				!zeitslot.getWochentag().equals("Donnerstag") || !zeitslot.getWochentag().equals("Freitag") || !zeitslot.getWochentag().equals("Samstag") || 
				!zeitslot.getWochentag().equals("Sonntag")) {
			throw new IllegalArgumentException("Wochentage muessen ausgeschrieben sein");
			 }
		
		if ((zeitslot.getEndzeit() - zeitslot.getAnfangszeit()) != 90) {
			throw new IllegalArgumentException("Ein Zeitslot muss einer Dauer von 90 Minuten entsprechen");
		}
		
		if ((zeitslot.getAnfangszeit() % 15) != 0 || (zeitslot.getAnfangszeit() % 15) != 0) {
			throw new IllegalArgumentException("Ein Zeitslot muss sich im Viertelstundentakt bewegen - XX:00/:15/:30/:45");
		}
		
		// Laden des vorhergehenden und nachfolgenden Zeitslots
		
		Vector<Zeitslot> davorZeitslot = null;
		Vector<Zeitslot> danachZeitslot = this.zeitslotMapper.findByKey(new Vector<Integer>(zeitslot.getId()+1));
		if(zeitslot.getId() > 1) {
			davorZeitslot = this.zeitslotMapper.findByKey(new Vector<Integer>(zeitslot.getId()-1));
		}

		// Prüfung des geänderten Zeitslots, ob dieser mit den vorgeschriebenen Pausen von 15 Minuten im Konflikt steht
		
		if (davorZeitslot != null) {
			if((zeitslot.getAnfangszeit() - davorZeitslot.elementAt(0).getEndzeit()) < 15) {
				throw new RuntimeException("Zum vorherigen Zeitslot muessen mindestens 15 Minuten Abstand sein");
			}
		}
		
		if((danachZeitslot.elementAt(0).getAnfangszeit() - zeitslot.getEndzeit()) < 15) {
			throw new RuntimeException("Zum nachfolgenden Zeitslot muessen mindestens 15 Minuten Abstand sein");
		}
		
		this.zeitslotMapper.update(zeitslot);
		return zeitslot;
	}
	
	public Lehrveranstaltung aendernLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) throws RuntimeException {
		
		//Prüfung ob die Bezeichnung der Lehrveranstaltung syntaktisch korrekt ist
		if (!lehrveranstaltung.getBezeichnung().matches("[^0-9\\,\\_\\+\\*\\/\\=\\}\\{\\[\\]\\%\\$\\§\\\"\\!\\^\\°\\<\\>\\|\\;\\:\\#\\~\\@\\€\\?\\(\\)\\²\\³]*")) {
			throw new IllegalArgumentException("Es befinden sich nicht erlaubte Zeichen in der Bezeichnung");
		}
		
		// Laden der "alten" Version der Lehrveranstaltung
		Vector<Integer> vI = new Vector<Integer>();
		vI.add(lehrveranstaltung.getId());
		Vector<Lehrveranstaltung> oldLehrveranstaltung = this.lehrveranstaltungMapper.findByKey(vI, true);
		
		// Vector mit ID's der Studiengänge, welche nicht mehr der hier zu ändernden Lehrveranstaltung zugeordnet sind
		Vector<Integer> tempStudiengangIDs = new Vector<Integer>();
				
		// Auslesen der ID's derer Studiengänge, welche nicht mehr der hier zu ändernden Lehrveranstaltung zugeordnet sind
				
		Integer a1 = null;
				
		for (int i = 0; i < oldLehrveranstaltung.elementAt(0).getStudiengaenge().size(); i++) {
			for (int j = 0; j < lehrveranstaltung.getStudiengaenge().size(); j++) {
				if (oldLehrveranstaltung.elementAt(0).getStudiengaenge().elementAt(i).getId() != lehrveranstaltung.getStudiengaenge().elementAt(j).getId()) {
					a1 = oldLehrveranstaltung.elementAt(0).getStudiengaenge().elementAt(i).getId();
				}
				else {
					a1 = null;
					break;
				}
			}
			if (a1 != null) {
				tempStudiengangIDs.add(a1);
				a1 = null;
			}
		}
		
		/*
		 *  Prüfwert, falls Referenzen von der Lehrveranstaltung auf bestimmte Studiengänge nicht gelöscht werden können,
		 *  da Semesterverbände dieser Studiengänge eine Belegung mit "dieser" Lehrveranstaltung referenzieren
		 *  -> false = kann nicht gelöscht werden
		 */
		
		Boolean check1 = true;
		
		StringBuffer eText1 = new StringBuffer("Verbindungen zu Studiengängen können nicht entfernt werden.\nFolgende Belegungen muessen zuerst "
				+ "entfernt oder einem anderen Semesterverband zugeordnet werden: \n\n");
		
		// Zuerst wird geprüft ob fehlende Studiengänge identifiziert wurden
		if (tempStudiengangIDs.size() > 0) {
			// Nun werden die Belegungen dieser Lehrveranstaltung geladen
			Vector<Belegung> tempBelegungen = this.belegungMapper.findByLehrveranstaltung(lehrveranstaltung);
			// Für jeden Studiengang, welcher "dieser" Lehrveranstaltung nicht mehr zugeordnet sein soll... 
			for (int i = 0; i < tempStudiengangIDs.size(); i++) {
				// ...werden alle Belegungen "dieser" Lehrveranstaltung durchsucht und in jeder dieser Belegung...
				for (int j = 0; j < tempBelegungen.size(); j++) {
					// ...werden alle zugeordneten Semesterverbände durchsucht
					for (int k = 0; k < tempBelegungen.elementAt(j).getSemesterverbaende().size(); k++) {
						/*
						 *  Sollte ein Semesterverband den "entfernten" Studiengängen entsprechen,
						 *  wird check auf "false" gesetzt und die Lehrveranstaltung kann nicht
						 *  geändert werden
						 */
						if(tempBelegungen.elementAt(j).getSemesterverbaende().elementAt(k).getStudiengang().getId() == tempStudiengangIDs.elementAt(i)) {
							check1 = false;
							eText1.append(tempBelegungen.elementAt(j).getZeitslot().getWochentag() + "\t\t" + tempBelegungen.elementAt(j).getZeitslot().getAnfangszeit() + "\t\t" + tempBelegungen.elementAt(j).getSemesterverbaende().elementAt(k).getStudiengang().getKuerzel()+ " " + tempBelegungen.elementAt(j).getSemesterverbaende().elementAt(k).getJahrgang() + "\n");
						}
					}
				}
			}			
		}
		
		if(!check1) {
			throw new RuntimeException(eText1.toString());
		}
		
		// Vector mit ID's der Dozenten, welche nicht mehr der Lehrveranstaltung zugeordnet sein sollen
		Vector<Integer> tempDozentIDs = new Vector<Integer>();
				
		// Auslesen der ID's derer Dozenten, welche nicht mehr der Lehrveranstaltung zugeordnet sein sollen
				
		Integer a2 = null;
				
		for (int i = 0; i < oldLehrveranstaltung.elementAt(0).getDozenten().size(); i++) {
			for (int j = 0; j < lehrveranstaltung.getDozenten().size(); j++) {
				if (oldLehrveranstaltung.elementAt(0).getDozenten().elementAt(i).getId() != lehrveranstaltung.getDozenten().elementAt(j).getId()) {
					a2 = oldLehrveranstaltung.elementAt(0).getDozenten().elementAt(i).getId();
				}
				else {
					a2 = null;
					break;
				}
			}
			if (a2 != null) {
				tempDozentIDs.add(a2);
				a2 = null;
			}
		}
				
		/*
		 *  Prüfwert, falls Referenzen vom Dozent auf bestimmte Lehrveranstaltungen nicht gelöscht werden können,
		 *  da sie Belegungen mit einzig "diesem" Dozenten referenzieren -> false = kann nicht gelöscht werden
		 */
				
		Boolean check2 = true;
				
		StringBuffer eText2 = new StringBuffer("Die Verbindung zu bestimmten Dozenten kann nicht geloescht werden.\nFolgende Belegungen muessen zuerst "
				+ "entfernt oder von einem anderen Dozenten übernommen werden: ");
				
				// Zuerst wird geprüft, ob Dozenten "dieser" Lehrveranstaltung nicht mehr zugeordnet sein sollen 		
				if (tempDozentIDs.size() > 0) {
					// Die Dozenten die "dieser" Lehrveranstaltung nicht mehr zugeordnet sein sollen werden nun geladen
					Vector<Dozent> tempDozenten = this.dozentMapper.findByKey(tempDozentIDs, true);
					// Für jede dieser Dozenten wird nun auf vorhandene Belegungen geprüft
					for (int i = 0; i < tempDozenten.size(); i++) {
						if (tempDozenten.elementAt(i).getBelegungen() != null) {
							// Sollte ein Dozent Belegungen referenzieren, werden diese nun geladen
							Vector<Belegung> tempBelegungen = this.belegungMapper.findByDozent(tempDozenten.elementAt(i));
							/*
							 *  Bei jeder Belegung wird schließlich geprüft, ob diese der "hier" zu ändernden
							 *  Lehrveranstaltung (Methoden-Parameter) entspricht. Sollte dies der Fall sein, kann 
							 *  eine Änderung nicht durchgeführt werden, da zuerst die Belegungen der zu löschenden 
							 *  Dozenten gelöscht werden müssen oder aber sie müssen einem anderen Dozenten zugeordnet
							 *  werden
							 */
							for (int j = 0; j < tempBelegungen.size(); j++) {
								if(tempBelegungen.elementAt(j).getLehrveranstaltung().getId() == lehrveranstaltung.getId()) {
									check2 = false;
									eText2.append(tempBelegungen.elementAt(j).getZeitslot().getWochentag() + " " + tempBelegungen.elementAt(j).getZeitslot().getAnfangszeit() + "\t\t");
									eText2.append(tempDozenten.elementAt(i).getTitel() + " ");
									eText2.append(tempDozenten.elementAt(i).getVorname() + " ");
									eText2.append(tempDozenten.elementAt(i).getNachname() + "\n");
								}
							}
						}
					}
				}		
		
		if(!check2) {
			throw new RuntimeException(eText2.toString());
		}
		
		if((lehrveranstaltung.getUmfang() != oldLehrveranstaltung.elementAt(0).getUmfang()) && (oldLehrveranstaltung.elementAt(0).getBelegungen() != null)) {
			throw new RuntimeException("Es müssen erst alle Belegungen dieser Lehrveranstaltung geloescht werden, bevor deren Umfang geaendert werden kann");
		}
				
		this.lehrveranstaltungMapper.update(lehrveranstaltung);
		return lehrveranstaltung;						
		
	}
	
	public Belegung aendernBelegung(Belegung belegung) throws RuntimeException {
		
		// Prüfung ob das Studiensemester der gewünschten Lehrveranstaltung mit dem des Semesterverbandsvereinbar ist
		
		Integer semesterAlt = null;
		Integer semesterNeu = null;
		
		for (int i = 0; i < belegung.getSemesterverbaende().size(); i++) {
			
			Semesterverband tempSemVerband = belegung.getSemesterverbaende().elementAt(i);
			
			Date datum = new Date();
			SimpleDateFormat datumFormat = new SimpleDateFormat("dd.MM.yyyy");
				
			String monatJahr = datumFormat.format(datum).toString();
			
			int semMonat = 0;
			int semJahr = 0;
			int aktMonat = Integer.parseInt(monatJahr.substring(3, 5));
			int aktJahr = Integer.parseInt(monatJahr.substring(6, 10));
			
			if (tempSemVerband.getJahrgang().substring(0, 2) == "WS") {
				semMonat = 9;
			}
			
			if (tempSemVerband.getJahrgang().substring(0, 2) == "SS") {
				semMonat = 3;
			}
			
			semJahr = Integer.parseInt(tempSemVerband.getJahrgang().substring(2, 6));
			
			int jahresDiff = aktJahr - semJahr;
			
			int[] calendar = {1,2,3,4,5,6,7,8,9,10,11,12};

			
			
			if (!(aktMonat < semMonat && aktJahr <= semJahr)) {
				
			
			int zaehler = 0;
			
			for (int j = semMonat-1; j < calendar.length; j++ ) {
				
				zaehler++;
				
				if((calendar[j] == aktMonat) && (jahresDiff == 0)) {
					break;
				}
				if(calendar[j] == 12) {
					j = -1;
					jahresDiff--;
				}
			}
			
			int studienSem = zaehler / 6;
			int studienSemMonat = zaehler;
			
			if (zaehler != 0) {
				studienSemMonat = zaehler % 6;
			}
			
			

			
			if (studienSem == 0) {
				semesterAlt = 1;
				semesterNeu = 0;
			}
			
			else if (studienSemMonat == 0) {
				semesterAlt = studienSem;
				semesterNeu = studienSem + 1;
			}
			
					
			else {
				semesterAlt = studienSem + 1;
			}
			}
			else {
				semesterAlt = 1;
			}
		}
		
		if (semesterNeu == null || semesterNeu == 0) {
			if (belegung.getLehrveranstaltung().getStudiensemester() != semesterAlt) {
				throw new RuntimeException("Die Lehrveranstaltung ist für ein anderes Studiensemester vorgesehen");
			}
		}
		
		else if (semesterAlt == null || semesterAlt == 0) {
			if (belegung.getLehrveranstaltung().getStudiensemester() != semesterNeu) {
				throw new RuntimeException("Die Lehrveranstaltung ist für ein anderes Studiensemester vorgesehen");
			}
		}		
		
		else {
			if ((belegung.getLehrveranstaltung().getStudiensemester() != semesterAlt) && (belegung.getLehrveranstaltung().getStudiensemester() != semesterNeu)) {
				throw new RuntimeException("Die Lehrveranstaltung ist für ein anderes Studiensemester vorgesehen");
		}
		}
				
		
		
		// Prüfen ob der Raum zum gewünschten Zeitslot noch verfügbar ist
		
		Vector<Belegung> raumBelegungen = this.belegungMapper.findByRaum(belegung.getRaum());
		
		for (int i = 0; i < raumBelegungen.size(); i++) {
			if ((belegung.getZeitslot().getId() == raumBelegungen.elementAt(i).getZeitslot().getId()) && (belegung.getId() != raumBelegungen.elementAt(i).getId())) {
				throw new RuntimeException("Der gewünschte Raum ist zum gewünschten Zeitslot schon belegt");
			}
		}
		
		// Prüfen ob der Semesterverband zum gewünschten Zeitslot verfügbar ist
		
		for (int i = 0; i < belegung.getSemesterverbaende().size(); i++) {
			Vector<Belegung> semVerBelegungen = this.belegungMapper.findBySemesterverband(belegung.getSemesterverbaende().elementAt(i));
			for (int j = 0; j < semVerBelegungen.size(); j++) {
				if ((belegung.getZeitslot().getId() == semVerBelegungen.elementAt(j).getZeitslot().getId()) && (belegung.getId() != semVerBelegungen.elementAt(j).getId())) {
					throw new RuntimeException("Der Semesterverband ist zu diesem Zeitpunkt bereits eingeteilt");
				}
			}
		}
		
		// Prüfen ob der Dozent zum gewünschten Zeitslot verfügbar ist
		
		for (int i = 0; i < belegung.getDozenten().size(); i++) {
			Vector<Belegung> dozentenBelegungen = this.belegungMapper.findByDozent(belegung.getDozenten().elementAt(i));
			for (int j = 0; j < dozentenBelegungen.size(); j++) {
				if ((belegung.getZeitslot().getId() == dozentenBelegungen.elementAt(j).getZeitslot().getId()) && (belegung.getId() != dozentenBelegungen.elementAt(j).getId())) {
					throw new RuntimeException("Der Dozent ist zu diesem Zeitpunkt bereits eingeteilt");
				}
			}
		}
		
		Vector<Integer> vi = new Vector<Integer>();
		vi.add(belegung.getLehrveranstaltung().getId());
		
		// Prüfung ob sich die Lehrveranstaltung und der Semesterverband im gleichen Studiengang sind
		
		Lehrveranstaltung tempLehrveranstaltung = this.lehrveranstaltungMapper.findByKey(vi, true).elementAt(0);
		
		boolean check = false;
		
		for (int i = 0; i < belegung.getSemesterverbaende().size(); i++) {
			check = false;
			for (int j = 0; j < tempLehrveranstaltung.getStudiengaenge().size(); j++) {
				if (belegung.getSemesterverbaende().elementAt(i).getStudiengang().getId() == tempLehrveranstaltung.getStudiengaenge().elementAt(j).getId()) {
					check = true;
					break;
				}
			}
		}
		
		if (!check) {
			throw new RuntimeException("Lehrveranstaltung und Semesterverband befinden sich nicht im gleichen Studiengang");
		}
			
		// Prüfung ob der Umfang (SWS) einer Lehrveranstaltung für einen Semesterverband bereits erreicht wurde
		
		for (int i = 0; i < belegung.getSemesterverbaende().size(); i++) {
			Vector<Belegung> tempSemVerBelegungen = this.belegungMapper.findBySemesterverband(belegung.getSemesterverbaende().elementAt(i));
			int countSWS = 0;
			for (int j = 0; j < tempSemVerBelegungen.size(); j++) {
				if((belegung.getLehrveranstaltung().getId() == tempSemVerBelegungen.elementAt(j).getLehrveranstaltung().getId()) && (belegung.getId() != tempSemVerBelegungen.elementAt(j).getId())) {
					countSWS = countSWS + 2;
				}
			}
			if (belegung.getLehrveranstaltung().getUmfang() < (countSWS + 2)) {
				throw new RuntimeException("Der vorgesehene Umfang (SWS) der Lehrveranstaltung wurde für Semesterverband " + belegung.getSemesterverbaende().elementAt(i).getStudiengang().getKuerzel() + " " + belegung.getSemesterverbaende().elementAt(i).getJahrgang());
			}
		}
		
		return this.belegungMapper.update(belegung);
				
	}
	
	public Studiengang aendernStudiengang(Studiengang studiengang) throws RuntimeException {
		
		// Prüfung ob Bezeichung und Kuerzel angegeben wurden
		
		StringBuffer bezeichnung = new StringBuffer();
		bezeichnung.append(studiengang.getBezeichnung());
		StringBuffer kuerzel = new StringBuffer();
		kuerzel.append(studiengang.getKuerzel());
		
		if ((bezeichnung.capacity() == 0) || (kuerzel.capacity() == 0)) {
			throw new IllegalArgumentException("Bitte geben Sie die Bezeichnung und das Kuerzel an");
		}
		
		/*
		 *  Die Maske zum ändern eines Studiengangs wird nur das Ändern des Kürzels und der Bezeichnung ermöglichen,
		 *  um dennoch Manipulationen der referenzierten Lehrveranstaltungen und Semesterverbände zu verhindern,
		 *  wird die "alte" Version dieses Studiengangs geladen, dann dessen Kürzel und Bezichnung gemäß dem "übergebenen"
		 *  Studiengangs überschrieben und schließlich wieder in die DB abgelegt
		 */
		
		Vector<Integer> vi = new Vector<Integer>();
		vi.add(studiengang.getId());
		
		Vector<Studiengang> tempStudiengang = this.studiengangMapper.findByKey(vi, true);
		
		tempStudiengang.elementAt(0).setBezeichnung(studiengang.getBezeichnung());
		tempStudiengang.elementAt(0).setKuerzel(studiengang.getKuerzel());
		
		return this.studiengangMapper.update(tempStudiengang.elementAt(0));
		
	}
	
	public Raum aendernRaum(Raum raum) throws RuntimeException {
		
		// Prüfziffer ("0" steht für Akzeptiert)
		int accepted = 0;

		
		// Auslesen aller Belegungen die den Raum referenzieren, der aktualisiert werden soll
		
		Vector<Belegung> tempBelegungen = this.belegungMapper.findByRaum(raum);
		
		// Lokale Variable zum festhalten der SemesterverbandID's, welche von "vB" referenziert werden 
		Vector<Integer> vI = new Vector<Integer>();

		// Hinzufügen der SemesterverbandID's zu "vI", falls sie nicht bereits enthalten sind
		for(int i = 0; i < tempBelegungen.size(); i++) {
			for (int j = 0; j < tempBelegungen.elementAt(i).getSemesterverbaende().size(); j++){
			
				if (!vI.contains((Integer)tempBelegungen.elementAt(i).getSemesterverbaende().elementAt(j).getId())) {
					vI.add(tempBelegungen.elementAt(i).getSemesterverbaende().elementAt(j).getId());
				}
			}			
		}
		
		//Laden der betroffenen Semesterverbände mittels "vI"		
		Vector<Semesterverband> vS = SemesterverbandMapper.semesterverbandMapper().findByKey(vI, false);

		//Prüfen ob eine Kapazitätsänderung mit den referenzierten Semesterverbandsgrößen vereinbar ist
		for(int i = 0; i < vS.size(); i++) {
			if (raum.getKapazitaet() < vS.elementAt(i).getAnzahlStudenten()) {
				accepted++;
			}
		}

		if ( accepted == 0) {
			return this.raumMapper.update(raum);
		}
		else {
			throw new RuntimeException("Die Kapazität ist für Semesterverbände, welche bereits Lehrveranstaltungen in diesem Raum zugeordnet sind, zu klein");
		}

	}
	
	/*
	 * Ändern der Business-Objects ---------------------------------------------------------------------------------------------------------------------------
	 */
	
	public Semesterverband anlegenSemesterverband (int anzahlStudenten, String jahrgang, Studiengang studiengang) throws RuntimeException {
		StringBuffer tempJahrgang = new StringBuffer();
		tempJahrgang.append(jahrgang);
		
		// Es wird geprüft, ob der Jahrgang semantisch und syntaktisch korrekt eingegeben wurde
		
		if (!tempJahrgang.substring(0,2).equals("SS") && !tempJahrgang.substring(0,2).equals("WS") || !tempJahrgang.substring(2,4).equals("20")) {
			throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben\nBeachten Sie auch die Gross-/Kleinschreibweise");			
		}
		
		if ((tempJahrgang.substring(0,2).equals("SS") && tempJahrgang.capacity() != 6) || (tempJahrgang.substring(0,2).equals("WS") && tempJahrgang.capacity() != 9) ||
				(tempJahrgang.substring(0,2).equals("WS") && tempJahrgang.charAt(6) != new Character('/'))) {
				throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben");
		}
		
		if (tempJahrgang.substring(0,2).equals("SS")) {
			try {
				Integer.parseInt(tempJahrgang.substring(4,6));
			}
			catch (NumberFormatException e1) {
				throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben");
			}
		}
		
		if (tempJahrgang.substring(0,2).equals("WS")) {
			int vJahr;
			int nJahr;
			try {
				vJahr = Integer.parseInt(jahrgang.substring(4,6));
				nJahr = Integer.parseInt(jahrgang.substring(7,9));
			}
			catch (NumberFormatException e1) {
				throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben");
			}
			if (nJahr - vJahr != 1) {
				throw new IllegalArgumentException("Ihre Jahrgangsangabe entspricht nicht den Vorgaben\nNur ein Jahreswechsel erlaubt");
			}
		}
		
		// Prüfung ob das Feld "anzahlStudenten" nur Zahlen enthält
		
		if (!new Integer(anzahlStudenten).toString().matches("[0-9]{1}|[0-9]*")) {
			throw new IllegalArgumentException("Bitten geben Sie die Anzahl der Studenten an");
		}
		
		if (studiengang == null) {
			throw new IllegalArgumentException("Bitte geben Sie einen Studiengang an");
		}
		
		// Neues Semesterverband-Objekt erzeugen
		
		Semesterverband neuSemVerband = new Semesterverband();
		neuSemVerband.setJahrgang(jahrgang);
		neuSemVerband.setAnzahlStudenten(anzahlStudenten);
		neuSemVerband.setStudiengang(studiengang);
		
		return this.semesterverbandMapper.insertIntoDB(neuSemVerband);
	}
	
	public Dozent anlegenDozent(String vorname, String nachname, String personalnummer, Vector<Lehrveranstaltung> lehrveranstaltungen) throws RuntimeException {
		
		// Prüfung ob Vor- und Nachname angegeben wurden
		
		StringBuffer tempVorname = new StringBuffer();
		tempVorname.append(vorname);
		StringBuffer tempNachname = new StringBuffer();
		tempNachname.append(nachname);
				
		if ((tempVorname.capacity() == 0) || (tempNachname.capacity() == 0)) {
			throw new IllegalArgumentException("Bitte geben Sie Vor- und Nachname an");
		}
		
		
		// Prüfung des Vor- und Nachnamens auf Zahlen und bestimmte Sonderzeichen, diese sind nicht erlaubt
		
		if (!vorname.matches("[^0-9\\,\\_\\+\\*\\/\\=\\}\\{\\[\\]\\%\\$\\§\\\"\\!\\^\\°\\<\\>\\|\\;\\:\\#\\~\\@\\€\\?\\(\\)\\²\\³]*") || 
				!nachname.matches("[^0-9\\,\\_\\+\\*\\/\\=\\}\\{\\[\\]\\%\\$\\§\\\"\\!\\^\\°\\<\\>\\|\\;\\:\\#\\~\\@\\€\\?\\(\\)\\²\\³]*")) {
			throw new IllegalArgumentException("Es befinden sich nicht erlaubte Zeichen im Vor- bzw. Nachnamen");
		}
				
		// Prüfung der Personalnummer auf fünfstellige Ziffernfolge
				
		if (!personalnummer.matches("[0-9]{5}")) {
			throw new IllegalArgumentException("Die Personalnummer ist nicht fuenfstellig\noder es befinden sich darin nicht erlaubte Zeichen");
		}
		
		// Neues Dozent-Objekt erzeugen
		
		Dozent neuDozent = new Dozent();
		neuDozent.setVorname(vorname);
		neuDozent.setNachname(nachname);
		neuDozent.setPersonalnummer(new Integer(personalnummer));
		if (lehrveranstaltungen != null && lehrveranstaltungen.size() > 0) {
			neuDozent.setLehrveranstaltungen(lehrveranstaltungen);
		}
		
		return this.dozentMapper.insertIntoDB(neuDozent);
	}
	
	/*
	public Dozent anlegenDozent(String vorname, String nachname, int personalnummer) throws RuntimeException {
		
		Vector<Lehrveranstaltung> keineLV = null;
		
		return this.anlegenDozent(vorname, nachname, personalnummer, keineLV);
	}
	*/
		
	public Zeitslot anlegenZeitslot(int anfangszeit, int endzeit, String wochentag) throws RuntimeException {
		
		// Das Anlegen eines neuen Zeitslots ist bis dato nicht vorgesehen - Stand: 12.12.2013
		
		return null;
	}
	
	public Lehrveranstaltung anlegenLehrveranstaltung(int umfang, String bezeichnung, int studiensemester, Vector<Studiengang> studiengaenge, Vector<Dozent> dozenten) throws RuntimeException {
		
		// Prüfung ob das Feld "umfang" nur Zahlen enthält und nicht leer ist
		
		if (!new Integer(umfang).toString().matches("[0-9]{1}|[0-9]*")) {
			throw new IllegalArgumentException("Bitten geben Sie die Anzahl der Studenten an");
		}
		
		// Prüfung ob der Inahlt Feld "umfang" durch 2 teilbar ist
		
		if ((umfang % 2) != 0) {
			throw new IllegalArgumentException("Der Umfang muss durch 2 teilbar sein");
		}
		
		// Prüfung ob das Feld "Studiensemester" nur Zahlen enthält und nicht leer ist
		
		if (!new Integer(umfang).toString().matches("[0-9]{1}|[0-9]*")) {
			throw new IllegalArgumentException("Bitten geben Sie das Studiensemester an");
		}
		
		// Neues Lehrveranstaltung-Objekt erzeugen
		
		Lehrveranstaltung neuLehrveranstaltung = new Lehrveranstaltung();
		neuLehrveranstaltung.setUmfang(umfang);
		neuLehrveranstaltung.setBezeichnung(bezeichnung);
		neuLehrveranstaltung.setStudiensemester(studiensemester);
		neuLehrveranstaltung.setStudiengaenge(studiengaenge);
		if (dozenten != null) {
			neuLehrveranstaltung.setDozenten(dozenten);
		}
		
		return this.lehrveranstaltungMapper.insertIntoDB(neuLehrveranstaltung);
		
	}
	
	public Lehrveranstaltung anlegenLehrveranstaltung(int umfang, String bezeichnung, int studiensemester, Vector<Studiengang> studiengaenge) throws RuntimeException {
		
		Vector<Dozent> keineDozenten = null;
		
		return this.anlegenLehrveranstaltung(umfang, bezeichnung, studiensemester, studiengaenge, keineDozenten);
	}
	
	public Belegung anlegenBelegung(Lehrveranstaltung lehrveranstaltung, Raum raum, Zeitslot zeitslot, Vector<Dozent> dozenten, Vector<Semesterverband> semesterverbaende) throws RuntimeException {
		
		// Neues Belegung-Objekt erzeugen
		
		Belegung belegung = new Belegung();
		belegung.setLehrveranstaltung(lehrveranstaltung);
		belegung.setRaum(raum);
		belegung.setZeitslot(zeitslot);
		belegung.setDozenten(dozenten);
		belegung.setSemesterverbaende(semesterverbaende);
		
		// Prüfung ob das Studiensemester der gewünschten Lehrveranstaltung mit dem des Semesterverbandsvereinbar ist
		
		Integer semesterAlt = null;
		Integer semesterNeu = null;
		
		for (int i = 0; i < belegung.getSemesterverbaende().size(); i++) {
			
			Semesterverband tempSemVerband = belegung.getSemesterverbaende().elementAt(i);
			
			Date datum = new Date();
			SimpleDateFormat datumFormat = new SimpleDateFormat("dd.MM.yyyy");
				
			String monatJahr = datumFormat.format(datum).toString();
			
			int semMonat = 0;
			int semJahr = 0;
			int aktMonat = Integer.parseInt(monatJahr.substring(3, 5));
			int aktJahr = Integer.parseInt(monatJahr.substring(6, 10));
			
			if (tempSemVerband.getJahrgang().substring(0, 2) == "WS") {
				semMonat = 9;
			}
			
			if (tempSemVerband.getJahrgang().substring(0, 2) == "SS") {
				semMonat = 3;
			}
			
			semJahr = Integer.parseInt(tempSemVerband.getJahrgang().substring(2, 6));
			
			int jahresDiff = aktJahr - semJahr;
			
			int[] calendar = {1,2,3,4,5,6,7,8,9,10,11,12};

			
			
			if (!(aktMonat < semMonat && aktJahr <= semJahr)) {
				
			
			int zaehler = 0;
			
			for (int j = semMonat-1; j < calendar.length; j++ ) {
				
				zaehler++;
				
				if((calendar[j] == aktMonat) && (jahresDiff == 0)) {
					break;
				}
				if(calendar[j] == 12) {
					j = -1;
					jahresDiff--;
				}
			}
			
			int studienSem = zaehler / 6;
			int studienSemMonat = zaehler;
			
			if (zaehler != 0) {
				studienSemMonat = zaehler % 6;
			}
			
			

			
			if (studienSem == 0) {
				semesterAlt = 1;
				semesterNeu = 0;
			}
			
			else if (studienSemMonat == 0) {
				semesterAlt = studienSem;
				semesterNeu = studienSem + 1;
			}
			
					
			else {
				semesterAlt = studienSem + 1;
			}
			}
			else {
				semesterAlt = 1;
			}
		}
		
		if (semesterNeu == null || semesterNeu == 0) {
			if (belegung.getLehrveranstaltung().getStudiensemester() != semesterAlt) {
				throw new RuntimeException("Die Lehrveranstaltung ist für ein anderes Studiensemester vorgesehen");
			}
		}
		
		else if (semesterAlt == null || semesterAlt == 0) {
			if (belegung.getLehrveranstaltung().getStudiensemester() != semesterNeu) {
				throw new RuntimeException("Die Lehrveranstaltung ist für ein anderes Studiensemester vorgesehen");
			}
		}		
		
		else {
			if ((belegung.getLehrveranstaltung().getStudiensemester() != semesterAlt) && (belegung.getLehrveranstaltung().getStudiensemester() != semesterNeu)) {
				throw new RuntimeException("Die Lehrveranstaltung ist für ein anderes Studiensemester vorgesehen");
		}
		}
				
		
		
		// Prüfen ob der Raum zum gewünschten Zeitslot schon noch verfügbar ist
		
		Vector<Belegung> raumBelegungen = this.belegungMapper.findByRaum(belegung.getRaum());
		
		for (int i = 0; i < raumBelegungen.size(); i++) {
			if (belegung.getZeitslot().getId() == raumBelegungen.elementAt(i).getZeitslot().getId()) {
				throw new RuntimeException("Der gewünschte Raum ist zum gewünschten Zeitslot schon belegt");
			}
		}
		
		// Prüfen ob der Semesterverband zum gewünschten Zeitslot verfügbar ist
		
		for (int i = 0; i < belegung.getSemesterverbaende().size(); i++) {
			Vector<Belegung> semVerBelegungen = this.belegungMapper.findBySemesterverband(belegung.getSemesterverbaende().elementAt(i));
			for (int j = 0; j < semVerBelegungen.size(); j++) {
				if (belegung.getZeitslot().getId() == semVerBelegungen.elementAt(j).getZeitslot().getId()) {
					throw new RuntimeException("Der Semesterverband ist zu diesem Zeitpunkt bereits eingeteilt");
				}
			}
		}
		
		// Prüfen ob der Dozent zum gewünschten Zeitslot verfügbar ist
		
		for (int i = 0; i < belegung.getDozenten().size(); i++) {
			Vector<Belegung> dozentenBelegungen = this.belegungMapper.findByDozent(belegung.getDozenten().elementAt(i));
			for (int j = 0; j < dozentenBelegungen.size(); j++) {
				if (belegung.getZeitslot().getId() == dozentenBelegungen.elementAt(j).getZeitslot().getId()) {
					throw new RuntimeException("Der Dozent ist zu diesem Zeitpunkt bereits eingeteilt");
				}
			}
		}
		
		Vector<Integer> vi = new Vector<Integer>();
		vi.add(belegung.getLehrveranstaltung().getId());
		
		// Prüfung ob sich die Lehrveranstaltung und der Semesterverband im gleichen Studiengang sind
		
		Lehrveranstaltung tempLehrveranstaltung = this.lehrveranstaltungMapper.findByKey(vi, true).elementAt(0);
		
		boolean check = false;
		
		for (int i = 0; i < belegung.getSemesterverbaende().size(); i++) {
			check = false;
			for (int j = 0; j < tempLehrveranstaltung.getStudiengaenge().size(); j++) {
				if (belegung.getSemesterverbaende().elementAt(i).getStudiengang().getId() == tempLehrveranstaltung.getStudiengaenge().elementAt(j).getId()) {
					check = true;
					break;
				}
			}
		}
		
		if (!check) {
			throw new RuntimeException("Lehrveranstaltung und Semesterverband befinden sich nicht im gleichen Studiengang");
		}
			
		// Prüfung ob der Umfang (SWS) einer Lehrveranstaltung für einen Semesterverband bereits erreicht wurde
		
		for (int i = 0; i < belegung.getSemesterverbaende().size(); i++) {
			Vector<Belegung> tempSemVerBelegungen = this.belegungMapper.findBySemesterverband(belegung.getSemesterverbaende().elementAt(i));
			int countSWS = 0;
			for (int j = 0; j < tempSemVerBelegungen.size(); j++) {
				if(belegung.getLehrveranstaltung().getId() == tempSemVerBelegungen.elementAt(j).getLehrveranstaltung().getId()) {
					countSWS = countSWS + 2;
				}
			}
			if (belegung.getLehrveranstaltung().getUmfang() < (countSWS + 2)) {
				throw new RuntimeException("Der vorgesehene Umfang (SWS) der Lehrveranstaltung wurde für Semesterverband " + belegung.getSemesterverbaende().elementAt(i).getStudiengang().getKuerzel() + " " + belegung.getSemesterverbaende().elementAt(i).getJahrgang());
			}
		}
		
		return this.belegungMapper.insertIntoDB(belegung);
				
	}
		
	public Studiengang anlegenStudiengang(String bezeichnung, String kuerzel, Vector<Lehrveranstaltung> lehrveranstaltungen) throws RuntimeException {
		
		// Prüfung ob Bezeichung und Kuerzel angegeben wurden
		
		StringBuffer tempBezeichnung = new StringBuffer();
		tempBezeichnung.append(bezeichnung);
		StringBuffer tempKuerzel = new StringBuffer();
		tempKuerzel.append(kuerzel);
		
		if ((tempBezeichnung.capacity() == 0) || (tempKuerzel.capacity() == 0)) {
			throw new IllegalArgumentException("Bitte geben Sie die Bezeichnung und das Kuerzel an");
		}
		
		// Neues Studiengang-Objekt erzeugen
		
		Studiengang neuStudiengang = new Studiengang();
		neuStudiengang.setBezeichnung(bezeichnung);
		neuStudiengang.setKuerzel(kuerzel);
		if (lehrveranstaltungen != null) {
			neuStudiengang.setLehrveranstaltungen(lehrveranstaltungen);
		}
		
		return this.studiengangMapper.insertIntoDB(neuStudiengang);
		
	}
	
	public Studiengang anlegenStudiengang(String bezeichnung, String kuerzel) throws RuntimeException {
		
		Vector<Lehrveranstaltung> keineLV = null;
		
		return anlegenStudiengang(bezeichnung, kuerzel, keineLV);
		
	}
	
	public Raum anlegenRaum(String bezeichnung, int kapazitaet) throws RuntimeException {
		
		// Prüfung ob Bezeichung und Kapazität angegeben wurden
		
		StringBuffer tempBezeichnung = new StringBuffer();
		tempBezeichnung.append(bezeichnung);
		StringBuffer tempKuerzel = new StringBuffer();
		tempKuerzel.append(kapazitaet);
				
		if ((tempBezeichnung.capacity() == 0) || (tempKuerzel.capacity() == 0)) {
			throw new IllegalArgumentException("Bitte geben Sie die Bezeichnung und das Kuerzel an");
		}
		
		Raum neuRaum = new Raum();
		neuRaum.setBezeichnung(bezeichnung);
		neuRaum.setKapazitaet(kapazitaet);
		
		return this.raumMapper.insertIntoDB(neuRaum);
		
	}
	
}
