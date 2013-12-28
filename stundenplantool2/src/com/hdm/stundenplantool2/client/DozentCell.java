package com.hdm.stundenplantool2.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.hdm.stundenplantool2.shared.bo.Dozent;

public class DozentCell extends AbstractCell<Dozent> {
	
	public void render(Context context, Dozent value, SafeHtmlBuilder sb) {
		
		if (value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value.getNachname() + " ");
		sb.appendEscaped(value.getVorname());
		sb.appendHtmlConstant("</div>");		
		
	}

}
