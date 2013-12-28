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

public class DozentForm extends VerticalPanel {
	
	VerwaltungAsync verwaltung = GWT.create(Verwaltung.class);
	
	TextBox vornameTb = new TextBox();
	TextBox nachnameTb = new TextBox();
	TextBox personalNummerTb = new TextBox();
	
	FlexTable lvTable;
	ListBox listBox;
	Vector<Lehrveranstaltung> alleLV = null;
	Vector<Lehrveranstaltung> LVvonNeuerDozent = null;
	
	Dozent shownDozent = null;
	DozentTreeViewModel dtvm = null;
	
	Button aendernButton;
	Button dozentLoeschenButton;
	Button dozentAnlegenButton;
	
	HorizontalPanel dozentButtonsPanel;
	
	public DozentForm() {
		Grid dozentGrid = new Grid(3,2);
		this.add(dozentGrid);
		
		Label vornameLabel = new Label("Vorname :");
		dozentGrid.setWidget(0, 0, vornameLabel);
		dozentGrid.setWidget(0, 1, vornameTb);
		
		Label nachnameLabel = new Label("Nachname :");
		dozentGrid.setWidget(1, 0, nachnameLabel);
		dozentGrid.setWidget(1, 1, nachnameTb);
		
		Label personalNummerLabel = new Label("Personalnummer :");
		dozentGrid.setWidget(2, 0, personalNummerLabel);
		dozentGrid.setWidget(2, 1, personalNummerTb);
		
		dozentButtonsPanel = new HorizontalPanel();
		this.add(dozentButtonsPanel);
		
		aendernButton = new Button("Aendern");
		aendernButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				aendernGewaehlterDozent();
			}
		});
		
		dozentLoeschenButton = new Button("Dozent loeschen");
		dozentLoeschenButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
				verwaltung.loeschenDozent(shownDozent, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
						Window.alert(caught.getMessage());						
					}
					
					public void onSuccess(Void result) {
						DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
						dtvm.loeschenDozent(shownDozent);
						Window.alert("Dozent wurde geloescht");
					}
				});
				
				
			}
		});
		
		dozentAnlegenButton = new Button("Anlegen");
		dozentAnlegenButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				verwaltung.anlegenDozent(vornameTb.getText(), nachnameTb.getText(), personalNummerTb.getText(), LVvonNeuerDozent, new AsyncCallback<Dozent>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
					public void onSuccess(Dozent result) {
						dtvm.addDozent(result);
						clearForm();
						Window.alert("Dozent wurde angelegt");
					}
				});
			}
		});
		
		dozentButtonsPanel.add(aendernButton);
		dozentButtonsPanel.add(dozentLoeschenButton);
		dozentButtonsPanel.add(dozentAnlegenButton);
		
		dozentAnlegenButton.setVisible(false);
		
		lvTable = new FlexTable();
		lvTable.setText(0, 0, "Lehrveranstaltung");
		lvTable.setText(0, 1, "entfernen");
		
		HorizontalPanel tablePanel = new HorizontalPanel();
		tablePanel.add(lvTable);
		this.add(tablePanel);	
		
		listBox = new ListBox();
		tablePanel.add(listBox);
		Button lvHinzufuegen = new Button("Hinzufuegen");
		tablePanel.add(lvHinzufuegen);
		lvHinzufuegen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				if (shownDozent != null) {
					boolean check = true;					
					for (Lehrveranstaltung lv : shownDozent.getLehrveranstaltungen()) {
						if (lv.getId() == alleLV.elementAt(listBox.getSelectedIndex()).getId()) {
							Window.alert("Die Lehrveranstaltung ist bereits hinzugefuegt");
							check = false;
							break;
						}
					}
					if (check) {
						shownDozent.getLehrveranstaltungen().addElement(alleLV.elementAt(listBox.getSelectedIndex()));
						lehrveranstaltungenAnzeigen();
					}
				}
				else {
					if (LVvonNeuerDozent == null) {
						LVvonNeuerDozent = new Vector<Lehrveranstaltung>();
					}
					boolean check = true;
					for (Lehrveranstaltung lv : LVvonNeuerDozent) {
						if (lv.getId() == alleLV.elementAt(listBox.getSelectedIndex()).getId()) {
							Window.alert("Die Lehrveranstaltung ist bereits hinzugefuegt");
							check = false;
							break;
						}
					}
					if (check) {
						if (LVvonNeuerDozent == null) {
							LVvonNeuerDozent = new Vector<Lehrveranstaltung>();
						}
						LVvonNeuerDozent.addElement(alleLV.elementAt(listBox.getSelectedIndex()));
						lehrveranstaltungenAnzeigen();
					}
				}				
				
			}
		});
		
		verwaltung.auslesenAlleLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			public void onSuccess(Vector<Lehrveranstaltung> result) {
				alleLV = result;
				for (Lehrveranstaltung lv : result) {
					listBox.addItem(lv.getBezeichnung());
				}
			}
		});
		
	}
	
	
	void lehrveranstaltungenAnzeigen() {
		lvTable.removeAllRows();
		lvTable.setText(0, 0, "Lehrveranstaltung");
		lvTable.setText(0, 1, "entfernen");
		
		if (shownDozent != null) {
			if ((shownDozent.getLehrveranstaltungen() != null) && (shownDozent.getLehrveranstaltungen().size() > 0)) {
				for(Lehrveranstaltung lv : shownDozent.getLehrveranstaltungen()) {
					final int row = lvTable.getRowCount();
					lvTable.setWidget(row, 0, new Label(lv.getBezeichnung()));
					
					Button loeschenButton = new Button("X");
					loeschenButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							
							int rowIndex = lvTable.getCellForEvent(event).getRowIndex();
							lvTable.removeRow(rowIndex);
							
							shownDozent.getLehrveranstaltungen().removeElementAt(rowIndex - 1);
								
						}
					});
					
					lvTable.setWidget(row, 1, loeschenButton);
					
				}
			}
		}			
		else {
			if ((LVvonNeuerDozent != null) && (LVvonNeuerDozent.size() > 0)) {
				for(Lehrveranstaltung lv : LVvonNeuerDozent) {
					final int row = lvTable.getRowCount();
					lvTable.setWidget(row, 0, new Label(lv.getBezeichnung()));
					
					Button loeschenButton = new Button("X");
					loeschenButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							
							int rowIndex = lvTable.getCellForEvent(event).getRowIndex();
							lvTable.removeRow(rowIndex);
							
							LVvonNeuerDozent.removeElementAt(rowIndex - 1);
								
						}
					});
					
					lvTable.setWidget(row, 1, loeschenButton);
					
				}
			}
		}
		
	}
	
	
	void aendernGewaehlterDozent() {
		
		shownDozent.setVorname(this.vornameTb.getText());
		shownDozent.setNachname(this.nachnameTb.getText());
		shownDozent.setPersonalnummer(Integer.parseInt(this.personalNummerTb.getValue()));
		
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
		verwaltung.aendernDozent(shownDozent, new AsyncCallback<Dozent>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
				verwaltung.auslesenDozent(shownDozent, new AsyncCallback<Vector<Dozent>>() {
					public void onFailure(Throwable caught) {
						DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
						Window.alert(caught.getMessage());
					}
					public void onSuccess(Vector<Dozent> result) {
						DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
						lvTable.clear();
						dtvm.setSelectedDozent(result.elementAt(0));
						
					}
				});
				
			}
			
			public void onSuccess(Dozent result) {
				DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
				dtvm.updateDozent(shownDozent);
				Window.alert("Dozent wurde geaendert");
			}
		});
		
	}
	
	void setDtvm(DozentTreeViewModel dtvm) {
		this.dtvm = dtvm;
	}
	
	void setShownDozent(Dozent dozent) {
		this.shownDozent = dozent;
	}
	
	void fillForm() {
		this.vornameTb.setText(shownDozent.getVorname());
		this.nachnameTb.setText(shownDozent.getNachname());
		this.personalNummerTb.setText(new Integer(shownDozent.getPersonalnummer()).toString());
	}
	
	public void noVisibiltyAendernButtons() {
		aendernButton.setVisible(false);
		dozentLoeschenButton.setVisible(false);
		dozentAnlegenButton.setVisible(true);
	}
	
	public void visibiltyAendernButtons() {
		aendernButton.setVisible(true);
		dozentLoeschenButton.setVisible(true);
		dozentAnlegenButton.setVisible(false);
	}
	
	public void clearForm() {
		this.shownDozent = null;
		this.vornameTb.setText("");
		this.nachnameTb.setText("");
		this.personalNummerTb.setText("");
		this.lvTable.removeAllRows();
		this.LVvonNeuerDozent = null;
	}

}
