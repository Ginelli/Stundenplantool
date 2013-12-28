package com.hdm.stundenplantool2.shared.bo;

import java.util.Vector;

/*Ein Objekt dieser Klasse ist eine Repräsentation eines realen Raumes
 * @author: Lucas Zanella
 * @implement: Mathias Zimmermann
 */

public class Raum extends BusinessObject {
	
	private static final long serialVersionUID = 1L;
	
	private int kapazitaet;
	private String bezeichnung;
	
	
	public int getKapazitaet() {
		return kapazitaet;
	}


	public void setKapazitaet(int kapazitaet) {
		this.kapazitaet = kapazitaet;
	}


	public String getBezeichnung() {
		return bezeichnung;
	}


	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}


	public String toString() {
	    return super.toString() + " " + this.kapazitaet + " " + this.bezeichnung + " "  ;
	  }
	
	
	

}
