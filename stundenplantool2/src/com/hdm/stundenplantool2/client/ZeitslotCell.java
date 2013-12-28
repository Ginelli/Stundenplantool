package com.hdm.stundenplantool2.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.hdm.stundenplantool2.shared.bo.Zeitslot;

public class ZeitslotCell extends AbstractCell<Zeitslot> {
	
	public void render(Context context, Zeitslot value, SafeHtmlBuilder sb) {
		
		if (value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value.getAnfangszeit() + " ");
		sb.appendEscaped(value.getEndzeit() + "");
		sb.appendHtmlConstant("</div>");		
		
	}

}
