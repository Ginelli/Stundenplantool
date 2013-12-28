package com.hdm.stundenplantool2.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.hdm.stundenplantool2.shared.bo.Raum;

public class RaumCell extends AbstractCell<Raum> {
	
public void render(Context context, Raum value, SafeHtmlBuilder sb) {
		
		if (value == null) {
			return;
		}
				
		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value.getBezeichnung());
		sb.appendHtmlConstant("</div>");		
		
	}

}
