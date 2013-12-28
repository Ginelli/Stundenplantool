package com.hdm.stundenplantool2.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.hdm.stundenplantool2.shared.bo.Belegung;

public class BelegungCell extends AbstractCell<Belegung> {
	
	public void render(Context context, Belegung value, SafeHtmlBuilder sb) {
		
		if (value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value.getLehrveranstaltung().getBezeichnung() + " ");
		sb.appendEscaped(value.getZeitslot().getWochentag());
		sb.appendHtmlConstant("</div>");		
		
	}

}
