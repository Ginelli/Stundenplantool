package com.hdm.stundenplantool2.shared.bo;

import java.io.Serializable;

/*
 * @author: Herr Prof. Thies
 * @implement: Lucas Zanella
 */

public abstract class BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	private int id;	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


 
 public String toString() {
	    /*
	     * Wir geben den Klassennamen gefolgt von der ID des Objekts zurück.
	     */
	    return this.getClass().getName() + " #" + this.id;
	  }

 
}
