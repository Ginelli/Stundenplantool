package com.hdm.stundenplantool2.shared.bo;

import java.util.Vector;

/*Ein Objekt dieser Klasse ist eine Repräsentation eines realen Semesterverbands
 * @author: Lucas Zanella
 * @implement: Mathias Zimmermann
 * * @implement: Timm Roth
 */
public class Semesterverband extends BusinessObject {
	private static final long serialVersionUID = 1L;
	
	private Studiengang studiengang;
	private String jahrgang;
	private int anzahlStudenten;
	private Vector<Belegung> belegungen = null;

	
	public Studiengang getStudiengang() {
		return studiengang;
	}

	public void setStudiengang(Studiengang studiengang) {
		this.studiengang = studiengang;
	}


	public String getJahrgang() {
		return jahrgang;
	}


	public void setJahrgang(String jahrgang) {
		this.jahrgang = jahrgang;
	}



	public int getAnzahlStudenten() {
		return anzahlStudenten;
	}



	public void setAnzahlStudenten(int anzahlStudenten) {
		this.anzahlStudenten = anzahlStudenten;
	}


	public Vector<Belegung> getBelegungen() {
		return belegungen;
	}



	public void setBelegungen(Vector<Belegung> belegungen) {
		this.belegungen = belegungen;
	}



	public String toString() {
	    return super.toString() + " " + this.studiengang + " " + this.jahrgang + " " + this.anzahlStudenten;
	  }
	
}
