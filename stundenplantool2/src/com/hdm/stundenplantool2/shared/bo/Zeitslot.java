package com.hdm.stundenplantool2.shared.bo;

import java.util.Vector;

/*Ein Objekt dieser Klasse ist eine Repräsentation eines realen Zeitslots
 * @author: Lucas Zanella
 * @implement: Timm Roth
 */

public class Zeitslot extends BusinessObject {
	
	private static final long serialVersionUID = 1L;
	
	private int endzeit;
	private int anfangszeit;
	private String wochentag;

	
	public int getEndzeit() {
		return endzeit;
	}

	public void setEndzeit(int endzeit) {
		this.endzeit = endzeit;
	}


	public int getAnfangszeit() {
		return anfangszeit;
	}


	public void setAnfangszeit(int anfangszeit) {
		this.anfangszeit = anfangszeit;
	}


	public String getWochentag() {
		return wochentag;
	}


	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}


	public String toString() {
	    return super.toString() + " " + this.endzeit + " " + this.anfangszeit + " " + this.wochentag + " " ;
	  }
	

}
