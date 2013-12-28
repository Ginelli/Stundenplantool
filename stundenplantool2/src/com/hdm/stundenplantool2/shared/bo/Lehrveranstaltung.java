package com.hdm.stundenplantool2.shared.bo;

import java.util.Vector;

/*Ein Objekt dieser Klasse ist eine Repräsentation einer realen Lehrveranstaltung
 * @author: Lucas Zanella
 * @implement: Lucas Zanella
 */

public class Lehrveranstaltung extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
	private int umfang;
	private String bezeichnung;
	private int studiensemester;
	private Vector<Studiengang> studiengaenge = null;
	private Vector<Dozent> dozenten = null;
	private Vector<Belegung> belegungen = null;
	
	

	public Vector<Belegung> getBelegungen() {
		return belegungen;
	}



	public void setBelegungen(Vector<Belegung> belegungen) {
		this.belegungen = belegungen;
	}



	public int getUmfang() {
		return umfang;
	}



	public void setUmfang(int umfang) {
		this.umfang = umfang;
	}




	public String getBezeichnung() {
		return bezeichnung;
	}



	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}



	public int getStudiensemester() {
		return studiensemester;
	}



	public void setStudiensemester(int studiensemester) {
		this.studiensemester = studiensemester;
	}



	public Vector<Studiengang> getStudiengaenge() {
		return studiengaenge;
	}




	public void setStudiengaenge(Vector<Studiengang> studiengaenge) {
		this.studiengaenge = studiengaenge;
	}


	public Vector<Dozent> getDozenten() {
		return dozenten;
	}



	public void setDozenten(Vector<Dozent> dozenten) {
		this.dozenten = dozenten;
	}



	public String toString() {
	    return super.toString() + " " + this.umfang + " " + this.bezeichnung + " " + this.dozenten + " " + this.studiensemester;
	  }
	
	}
