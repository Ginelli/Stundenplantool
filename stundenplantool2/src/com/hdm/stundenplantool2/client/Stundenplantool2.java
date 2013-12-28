package com.hdm.stundenplantool2.client;

import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hdm.stundenplantool2.shared.*;
import com.hdm.stundenplantool2.shared.bo.*;

public class Stundenplantool2 implements EntryPoint {
	
	private final VerwaltungAsync server = GWT.create(Verwaltung.class);
	private CellTree cellTree;
	private DozentTreeViewModel dtvm;
	private DockLayoutPanel p;
	private ScrollPanel navi;
	private DozentForm dF;
	private VerticalPanel mainPanel;

	@Override
	public void onModuleLoad() {
			
		Button button = new Button("neuertree");
		
		navi = new ScrollPanel();
		
		
		dF = new DozentForm();
		
		dtvm = new DozentTreeViewModel(dF);
		cellTree = new CellTree(dtvm, "Root");
		navi.add(cellTree);
		
		
		mainPanel = new VerticalPanel();
		
		
		
		
		p = new DockLayoutPanel(Unit.EM);
		p.addNorth(button, 5);
		p.addSouth(new HTML("footer"), 2);
		p.addWest(navi, 30);
		p.add(mainPanel);
		
		
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				navi.clear();
				cellTree = new CellTree(dtvm, "Root");
				navi.add(cellTree);
			}
		});
		
		
		
		RootLayoutPanel rlp = RootLayoutPanel.get();
		rlp.add(p);
		RootPanel.get().add(rlp);
		
		dtvm.setStundenplanttol2(this);
		
		
	}
	
	public void setDozentFormToMain() {
		mainPanel.clear();
		mainPanel.add(dF);
	}
	
	public CellTree getCellTree() {
		return cellTree;
	}
	
	
}
