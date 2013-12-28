package com.hdm.stundenplantool2.shared.bo;

import java.util.Vector;

public class Studiengang extends BusinessObject{

	/*public Dozent (int dozentID, String vorname, String nachname) {
	super.setId(dozentID);
	this.vorname = vorname;
	this.nachname = nachname;
}
*/

private static final long serialVersionUID = 1L;

private Vector<Semesterverband> semesterverbaende = null;
private Vector<Lehrveranstaltung> lehrveranstaltungen = null;
private String bezeichnung;
private String kuerzel;




public String getKuerzel() {
	return kuerzel;
}


public void setKuerzel(String kuerzel) {
	this.kuerzel = kuerzel;
}


public Vector<Semesterverband> getSemesterverbaende() {
	return semesterverbaende;
}


public void setSemesterverbaende(Vector<Semesterverband> semesterverbaende) {
	this.semesterverbaende = semesterverbaende;
}


public Vector<Lehrveranstaltung> getLehrveranstaltungen() {
	return lehrveranstaltungen;
}


public void setLehrveranstaltungen(Vector<Lehrveranstaltung> lehrveranstaltungen) {
	this.lehrveranstaltungen = lehrveranstaltungen;
}


public String getBezeichnung() {
	return bezeichnung;
}


public void setBezeichnung(String bezeichnung) {
	this.bezeichnung = bezeichnung;
}


public String toString() {
    return super.toString() + " " ;
  }


	
}
