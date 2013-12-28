package com.hdm.stundenplantool2.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.hdm.stundenplantool2.shared.bo.Semesterverband;

public class SemesterverbandCell extends AbstractCell<Semesterverband> {
	
	public void render(Context context, Semesterverband value, SafeHtmlBuilder sb) {
		
		if (value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value.getStudiengang().getKuerzel() + " ");
		sb.appendEscaped(value.getJahrgang());
		sb.appendHtmlConstant("</div>");		
		
	}

}
