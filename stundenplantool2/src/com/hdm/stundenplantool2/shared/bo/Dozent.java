package com.hdm.stundenplantool2.shared.bo;

import java.util.Vector;

/*Ein Objekt dieser Klasse ist eine Repräsentation eines realen Dozenten
 * @author: Lucas Zanella
 * @implement: Lucas Zanella
 */

public class Dozent extends BusinessObject {
	
	/*public Dozent (int dozentID, String vorname, String nachname) {
		super.setId(dozentID);
		this.vorname = vorname;
		this.nachname = nachname;
	}
	*/
	
	private static final long serialVersionUID = 1L;
	
	private String vorname;
	private int personalnummer;
	private String nachname;
	private Vector<Belegung> belegungen;
	private Vector<Lehrveranstaltung> lehrveranstaltungen;
	private String titel = "Prof.";

	
	
	public String getTitel() {
		return titel;
	}

	public int getPersonalnummer() {
		return personalnummer;
	}

	public void setPersonalnummer(int personalnummer) {
		this.personalnummer = personalnummer;
	}

	public Vector<Belegung> getBelegungen() {
		return belegungen;
	}

	public void setBelegungen(Vector<Belegung> belegungen) {
		this.belegungen = belegungen;
	}

	public Vector<Lehrveranstaltung> getLehrveranstaltungen() {
		return lehrveranstaltungen;
	}

	public void setLehrveranstaltungen(Vector<Lehrveranstaltung> lehrveranstaltungen) {
		this.lehrveranstaltungen = lehrveranstaltungen;
	}
	
	
	
	
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	
	public String toString() {
	    return super.toString() + " " + this.vorname + " " + this.nachname + " " + this.personalnummer;
	  }
	

}