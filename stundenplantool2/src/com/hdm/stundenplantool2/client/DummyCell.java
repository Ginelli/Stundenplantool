package com.hdm.stundenplantool2.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;


public class DummyCell extends AbstractCell<String> {
	
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		
		if (value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value);
		sb.appendHtmlConstant("</div>");		
		
	}

}