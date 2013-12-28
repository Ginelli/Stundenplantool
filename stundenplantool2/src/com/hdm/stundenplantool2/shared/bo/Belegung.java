package com.hdm.stundenplantool2.shared.bo;

import java.util.Vector;

/*Ein Objekt dieser Klasse ist eine Repräsentation einer realen Belegung
 * @author: Lucas Zanella
 * @implement: Timm Roth
 */

public class Belegung extends BusinessObject {
	
	private static final long serialVersionUID = 1L;
	
	private Lehrveranstaltung lehrveranstaltung;
	private Raum raum;
	private Zeitslot zeitslot;
	private Vector<Dozent> dozenten;
	private Vector<Semesterverband> semesterverbaende;


	public Lehrveranstaltung getLehrveranstaltung() {
		return lehrveranstaltung;
	}


	public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		this.lehrveranstaltung = lehrveranstaltung;
	}

	public Raum getRaum() {
		return raum;
	}


	public void setRaum(Raum raum) {
		this.raum = raum;
	}


	public Zeitslot getZeitslot() {
		return zeitslot;
	}


	public void setZeitslot(Zeitslot zeitslot) {
		this.zeitslot = zeitslot;
	}


	public Vector<Dozent> getDozenten() {
		return dozenten;
	}


	public void setDozenten(Vector<Dozent> dozenten) {
		this.dozenten = dozenten;
	}


	public Vector<Semesterverband> getSemesterverbaende() {
		return semesterverbaende;
	}


	public void setSemesterverbaende(Vector<Semesterverband> semesterverbaende) {
		this.semesterverbaende = semesterverbaende;
	}


	public String toString() {
	    return super.toString() ;
	  }
	

}
