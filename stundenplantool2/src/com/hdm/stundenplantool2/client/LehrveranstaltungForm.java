package com.hdm.stundenplantool2.client;



import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hdm.stundenplantool2.shared.*;
import com.hdm.stundenplantool2.shared.bo.*;

public class LehrveranstaltungForm extends VerticalPanel {
	
	VerwaltungAsync verwaltung = GWT.create(Verwaltung.class);
	
	TextBox lvbezeichnungTb = new TextBox();
	ListBox listBoxstudiengang = new ListBox();
	ListBox listBoxumfang = new ListBox();
	ListBox listBoxsemester = new ListBox();
	ListBox listBox3 = new ListBox();
	
	
	
	
	
	FlexTable lvTable;
	//ListBox listBox;
	
	
	Vector<Dozent> alleD = null;
//	Vector<Lehrveranstaltung> LVvonNeuerDozent = null;
	Vector<Studiengang> LVStudiengang = null;
	Vector<Integer> LVumfang = new Vector<Integer>();
	
	
	
	
	Vector <Dozent> dozenten = null;
	Vector <Integer> LVsemester ;
	
	
	Vector<Dozent> DozentenvonLV = null;
	
	
	Lehrveranstaltung shownLehrveranstaltung = null;
	DozentTreeViewModel dtvm = null;
	
	Button aendernButton;
	Button lehrveranstaltungLoeschenButton;
	Button lehrveranstaltungAnlegenButton;
	
	HorizontalPanel lehrveranstaltungButtonsPanel;
	

	
	public LehrveranstaltungForm() {
		Grid lehrveranstaltungGrid = new Grid(3,2);
		this.add(lehrveranstaltungGrid);
		
		Label vornameLabel = new Label("Lehrveranstaltungsbezeichnung :");
		lehrveranstaltungGrid.setWidget(0, 0, vornameLabel);
		lehrveranstaltungGrid.setWidget(0, 1, lvbezeichnungTb);
		
		Label nachnameLabel = new Label("Studiengang :");
		lehrveranstaltungGrid.setWidget(1, 0, nachnameLabel);
		lehrveranstaltungGrid.setWidget(1, 1, listBoxstudiengang);
		
		Label personalNummerLabel = new Label("Umfang :");
		lehrveranstaltungGrid.setWidget(2, 0, personalNummerLabel);
		lehrveranstaltungGrid.setWidget(2, 1, listBoxumfang);
		
		lehrveranstaltungButtonsPanel = new HorizontalPanel();
		this.add(lehrveranstaltungButtonsPanel);
		
		aendernButton = new Button("Aendern");
		aendernButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				aendernGewaehlteLehrveranstaltung();
			}
		});
		
		lehrveranstaltungLoeschenButton = new Button("Lehrveranstaltung loeschen");
		lehrveranstaltungLoeschenButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
				verwaltung.loeschenLehrveranstaltung(shownLehrveranstaltung, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
						Window.alert(caught.getMessage());						
					}
					
					public void onSuccess(Void result) {
						DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
						dtvm.loeschenLehrveranstaltung(shownLehrveranstaltung);
						Window.alert("Lehrveranstaltung wurde geloescht");
					}
				});
				
				
			}
		});
		
		lehrveranstaltungAnlegenButton = new Button("Anlegen");
		lehrveranstaltungAnlegenButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				verwaltung.anlegenLehrveranstaltung(LVumfang.elementAt(listBoxumfang.getSelectedIndex()), lvbezeichnungTb.getText(), LVsemester.elementAt(listBoxsemester.getSelectedIndex()), LVStudiengang, dozenten, new AsyncCallback <Lehrveranstaltung>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
					public void onSuccess(Lehrveranstaltung result) {
//						dtvm.addDozent(result);
						clearForm();
						Window.alert("Lehrveranstaltung wurde angelegt");
					}
				});
			}
		});
		
		lehrveranstaltungButtonsPanel.add(aendernButton);
		lehrveranstaltungButtonsPanel.add(lehrveranstaltungLoeschenButton);
		lehrveranstaltungButtonsPanel.add(lehrveranstaltungAnlegenButton);
		
		lehrveranstaltungAnlegenButton.setVisible(false);
		
//		lvTable = new FlexTable();
//		lvTable.setText(0, 0, "Lehrveranstaltung");
//		lvTable.setText(0, 1, "entfernen");
		
		HorizontalPanel tablePanel = new HorizontalPanel();
//		tablePanel.add(lvTable);
//		this.add(tablePanel);	
	
		LVumfang.add(2);
		LVumfang.add(4);
		
		LVsemester.add(1);
		LVsemester.add(2);
		LVsemester.add(3);
		LVsemester.add(4);
		LVsemester.add(5);
		LVsemester.add(6);
		LVsemester.add(7);
		LVsemester.add(8);
		LVsemester.add(9);
		LVsemester.add(10);
		
		
		
		listBox3 = new ListBox();
		
		tablePanel.add(listBox3);
		Button lvHinzufuegen = new Button("Hinzufuegen");
		tablePanel.add(lvHinzufuegen);
		lvHinzufuegen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				if (shownLehrveranstaltung != null) {
					boolean check = true;					
					for (Dozent lv : shownLehrveranstaltung.getDozenten()) {
						if (lv.getId() == alleD.elementAt(listBox3.getSelectedIndex()).getId()) {
							Window.alert("Die Lehrveranstaltung ist bereits hinzugefuegt");
							check = false;
							break;
						}
					}
					if (check) {
						shownLehrveranstaltung.getDozenten().addElement(alleD.elementAt(listBox3.getSelectedIndex()));
						dozentenAnzeigen();
					}
				}
				else {
					if (DozentenvonLV == null) {
						DozentenvonLV = new Vector<Dozent>();
					}
					boolean check = true;
					for (Dozent lv : DozentenvonLV) {
						if (lv.getId() == alleD.elementAt(listBox3.getSelectedIndex()).getId()) {
							Window.alert("Die Lehrveranstaltung ist bereits hinzugefuegt");
							check = false;
							break;
						}
					}
					if (check) {
						if (DozentenvonLV == null) {
							DozentenvonLV = new Vector<Dozent>();
						}
						DozentenvonLV.addElement(alleD.elementAt(listBox3.getSelectedIndex()));
						dozentenAnzeigen();
					}
				}				
				
			}
		});
		
		verwaltung.auslesenAlleDozenten(new AsyncCallback<Vector<Dozent>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			public void onSuccess(Vector<Dozent> result) {
				alleD = result;
				for (Dozent lv : result) {
					listBox3.addItem(lv.getNachname());
				}
			}
		});
		
	}
	
	
	void dozentenAnzeigen() {
		lvTable.removeAllRows();
		lvTable.setText(0, 0, "Lehrveranstaltung");
		lvTable.setText(0, 1, "entfernen");
		
		if (shownLehrveranstaltung != null) {
			if ((shownLehrveranstaltung.getDozenten() != null) && (shownLehrveranstaltung.getDozenten().size() > 0)) {
				for(Dozent lv : shownLehrveranstaltung.getDozenten()) {
					final int row = lvTable.getRowCount();
					lvTable.setWidget(row, 0, new Label(lv.getNachname()));
					
					Button loeschenButton = new Button("X");
					loeschenButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							
							int rowIndex = lvTable.getCellForEvent(event).getRowIndex();
							lvTable.removeRow(rowIndex);
							
							shownLehrveranstaltung.getDozenten().removeElementAt(rowIndex - 1);
								
						}
					});
					
					lvTable.setWidget(row, 1, loeschenButton);
					
				}
			}
		}			
		else {
			if ((DozentenvonLV != null) && (DozentenvonLV.size() > 0)) {
				for(Dozent lv : DozentenvonLV) {
					final int row = lvTable.getRowCount();
					lvTable.setWidget(row, 0, new Label(lv.getNachname()));
					
					Button loeschenButton = new Button("X");
					loeschenButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							
							int rowIndex = lvTable.getCellForEvent(event).getRowIndex();
							lvTable.removeRow(rowIndex);
							
							DozentenvonLV.removeElementAt(rowIndex - 1);
								
						}
					});
					
					lvTable.setWidget(row, 1, loeschenButton);
					
				}
			}
		}
		
	}
	
	
	void aendernGewaehlteLehrveranstaltung() {
		
		shownLehrveranstaltung.setBezeichnung(this.lvbezeichnungTb.getText());
		
		
//		shownLehrveranstaltung.setNachname(this.nachnameTb.getText());
//		shownLehrveranstaltung.setPersonalnummer(Integer.parseInt(this.personalNummerTb.getValue()));
		
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
		verwaltung.aendernLehrveranstaltung(shownLehrveranstaltung, new AsyncCallback<Lehrveranstaltung>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
		/*		
				verwaltung.auslesenAlleLehrveranstaltungen(shownLehrveranstaltung, new AsyncCallback<Vector<Lehrveranstaltung>>() {
					public void onFailure(Throwable caught) {
						DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
						Window.alert(caught.getMessage());
					}
					public void onSuccess(Vector<Lehrveranstaltung> result) {
						DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
						lvTable.clear();
						dtvm.setSelectedLehrveranstaltung(result.elementAt(0));
						
					}
				});
				*/
			}
			
			public void onSuccess(Lehrveranstaltung result) {
				DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
				dtvm.updateLehrveranstaltung(shownLehrveranstaltung);
				Window.alert("Lehrveranstaltung wurde geaendert");
			}
		});
		
	}
	
	void setDtvm(DozentTreeViewModel dtvm) {
		this.dtvm = dtvm;
	}
	
	void setShownDozent(Lehrveranstaltung lv) {
		this.shownLehrveranstaltung = lv;
	}
	
	void fillForm() {
		this.lvbezeichnungTb.setText(shownLehrveranstaltung.getBezeichnung());
		
//		this.nachnameTb.setText(shownDozent.getNachname());
//		this.personalNummerTb.setText(new Integer(shownDozent.getPersonalnummer()).toString());
	}
	
	public void noVisibiltyAendernButtons() {
		aendernButton.setVisible(false);
		lehrveranstaltungLoeschenButton.setVisible(false);
		lehrveranstaltungAnlegenButton.setVisible(true);
	}
	
	public void visibiltyAendernButtons() {
		aendernButton.setVisible(true);
		lehrveranstaltungLoeschenButton.setVisible(true);
		lehrveranstaltungAnlegenButton.setVisible(false);
	}
	
	public void clearForm() {
		this.shownLehrveranstaltung = null;
		
//		this.vornameTb.setText("");
//		this.nachnameTb.setText("");
//		this.personalNummerTb.setText("");
//		this.lvTable.removeAllRows();
//		this.LVvonNeuerDozent = null;
	}

}

