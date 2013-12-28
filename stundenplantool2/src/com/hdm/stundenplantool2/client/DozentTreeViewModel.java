package com.hdm.stundenplantool2.client;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.hdm.stundenplantool2.shared.*;
import com.hdm.stundenplantool2.shared.bo.*;

import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.view.client.ListDataProvider;




public class DozentTreeViewModel implements TreeViewModel {
	
	private BusinessObject selectedBO = null;
	private Dozent selectedDozent = null;
	private Lehrveranstaltung selectedLehrveranstaltung = null;
	private Raum selectedRaum = null;
	private Semesterverband selectedSemesterverband = null;
	private Studiengang selectedStudiengang = null;
	private Zeitslot selectedZeitslot = null;
	private Belegung selectedBelegung = null;
	private String knoten = null;
	
	private static DozentForm dF;
	
	Stundenplantool2 spt2;
	
	
	private VerwaltungAsync verwaltung = GWT.create(Verwaltung.class);
		
	private ListDataProvider<Lehrveranstaltung> lehrveranstaltungDataProvider;
	private ListDataProvider<Dozent> dozentDataProvider;
	private ListDataProvider<Raum> raumDataProvider;
	private ListDataProvider<Semesterverband> semesterVerbandDataProvider;
	private ListDataProvider<Studiengang> studiengangDataProvider;
	private ListDataProvider<Zeitslot> zeitslotDataProvider;
	private ListDataProvider<Belegung> belegungDataProvider;
	private ListDataProvider<String> dummyDataProvider;
		
	private ProvidesKey<Object> keyProvider = new ProvidesKey<Object>() {
		public Integer getKey(Object object) {
						
			if (object == null) {
				return null;
			}
			else if (object instanceof Lehrveranstaltung) {
				return new Integer(object.hashCode());
			} 
			else if (object instanceof Dozent) {
				return new Integer(object.hashCode());
			}
			else if (object instanceof Raum) {
				return new Integer(object.hashCode());
			}
			else if (object instanceof Semesterverband) {
				return new Integer(object.hashCode());
			}
			else if (object instanceof Studiengang) {
				return new Integer(object.hashCode());
			}
			else if (object instanceof Zeitslot) {
				return new Integer(object.hashCode());
			}
			else if (object instanceof Belegung) {
				return new Integer(object.hashCode());
			}
			
			else if (object instanceof String && (String)object == "Editor") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Report") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Anlegen") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Dozent") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Lehrveranstaltung") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Belegung") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Raum") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Semesterverband") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Studiengang") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Zeitslot") {
				return new Integer(object.hashCode());
			}			
			else if (object instanceof String && (String)object == "Verwalten") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Dozenten") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Lehrveranstaltungen") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Belegungen") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Raeume") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Semesterverbaende") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Studiengaenge") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Zeitslots") {
				return new Integer(object.hashCode());
			}			
			else if (object instanceof String && (String)object == "Stundenplan") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Dozentenplan") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Raumplan") {
				return new Integer(object.hashCode());
			}
			else if (object instanceof String && (String)object == "Montag") {
				return new Integer(object.hashCode());
			}	
			else if (object instanceof String && (String)object == "Dienstag") {
				return new Integer(object.hashCode());
			}	
			else if (object instanceof String && (String)object == "Mittwoch") {
				return new Integer(object.hashCode());
			}	
			else if (object instanceof String && (String)object == "Donnerstag") {
				return new Integer(object.hashCode());
			}	
			else if (object instanceof String && (String)object == "Freitag") {
				return new Integer(object.hashCode());
			}	
			else if (object instanceof String && (String)object == "Samstag") {
				return new Integer(object.hashCode());
			}	
			else if (object instanceof String && (String)object == "Sonntag") {
				return new Integer(object.hashCode());
			}	
			else {
				return null;
			}
		}
	};
	
	
	private SingleSelectionModel<Object> selectionModel = new SingleSelectionModel<Object>(keyProvider);
		
	
	public DozentTreeViewModel(DozentForm dF) {
		
		this.dF = dF;
		dF.setDtvm(this);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			public void onSelectionChange(SelectionChangeEvent event) {
				Object selection = selectionModel.getSelectedObject();
				
				if (selection instanceof Dozent) {
															
					DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
					
					verwaltung.auslesenDozent((Dozent)selection, new AsyncCallback<Vector<Dozent>>() {
						public void onFailure(Throwable caught) {
							DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
							Window.alert(caught.getMessage());
						}
						public void onSuccess(Vector<Dozent> result) {
							setSelectedDozent(result.elementAt(0));							
						}
					});
					
							
				}
				else if (selection instanceof Lehrveranstaltung) {
					setSelectedLehrveranstaltung((Lehrveranstaltung)selection);
				}
				else if (selection instanceof Raum) {
					setSelectedRaum((Raum)selection);
				}
				else if (selection instanceof Semesterverband) {
					setSelectedSemesterverband((Semesterverband)selection);
				}
				else if (selection instanceof Studiengang) {
					setSelectedStudiengang((Studiengang)selection);
				}
				else if (selection instanceof Zeitslot) {
					setSelectedZeitslot((Zeitslot)selection);
				}
				else if (selection instanceof Belegung) {
					setSelectedBelegung((Belegung)selection);
				}
				else if (selection instanceof String && (String)selection == "Dozent") {
					setSelectedString((String)selection);
					dozentAnlegenMaske();
				}
			}
		});
		
	}
	
	public void setSelectedDozent(Dozent dozent) {
		spt2.setDozentFormToMain();
		this.dF.visibiltyAendernButtons();
		this.selectedDozent = dozent;
		this.selectedBO = dozent;
		this.dF.setShownDozent(dozent);
		this.dF.fillForm();
		this.dF.lehrveranstaltungenAnzeigen();
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}
	
	public void dozentAnlegenMaske() {
		this.selectedDozent = null;
		spt2.setDozentFormToMain();
		dF.noVisibiltyAendernButtons();
		dF.clearForm();
	}
	
	public void setSelectedLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		this.selectedLehrveranstaltung = lehrveranstaltung;
	}
	
	public void setSelectedRaum(Raum raum) {
		this.selectedRaum = raum;
	}
	
	public void setSelectedSemesterverband(Semesterverband semesterverband) {
		this.selectedSemesterverband = semesterverband;
	}
	
	public void setSelectedStudiengang(Studiengang studiengang) {
		this.selectedStudiengang = studiengang;
	}
	
	public void setSelectedZeitslot(Zeitslot zeitslot) {
		this.selectedZeitslot = zeitslot;
	}
	
	public void setSelectedBelegung(Belegung belegung) {
		this.selectedBelegung = belegung;
	}
	
	public void setSelectedString(String knoten) {
		this.knoten = knoten;
	}
	
	public ListDataProvider<Lehrveranstaltung> getLVDataProvider() {
		return this.lehrveranstaltungDataProvider;
	}
	
	
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if (value instanceof String && (String)value == "Root") {
			dummyDataProvider = new ListDataProvider<String>();
			
			String editor = "Editor";
			String report = "Report";
			
			dummyDataProvider.getList().add(editor);
			dummyDataProvider.getList().add(report);
			
			return new DefaultNodeInfo<String>(dummyDataProvider, new DummyCell(), selectionModel, null );
		}
		
		if (value instanceof String && (String)value == "Editor") {
			dummyDataProvider = new ListDataProvider<String>();
			
			String anlegen = "Anlegen";
			String verwalten = "Verwalten";
			
			dummyDataProvider.getList().add(anlegen);
			dummyDataProvider.getList().add(verwalten);
			
			return new DefaultNodeInfo<String>(dummyDataProvider, new DummyCell(), selectionModel, null );
		}
		
		if (value instanceof String && (String)value == "Anlegen") {
			dummyDataProvider = new ListDataProvider<String>();
			
			String lehrveranstaltung = "Lehrveranstaltung";
			String dozent = "Dozent";
			String belegung = "Belegung";
			String raum = "Raum";
			String semesterverband = "Semesterverband";
			String studiengang = "Studiengang";
			String zeitslot = "Zeitslot";
			
			dummyDataProvider.getList().add(lehrveranstaltung);
			dummyDataProvider.getList().add(dozent);
			dummyDataProvider.getList().add(belegung);
			dummyDataProvider.getList().add(raum);
			dummyDataProvider.getList().add(semesterverband);
			dummyDataProvider.getList().add(studiengang);
			dummyDataProvider.getList().add(zeitslot);
			
			return new DefaultNodeInfo<String>(dummyDataProvider, new DummyCell(), selectionModel, null );
		}
		
				
		if (value instanceof String && (String)value == "Verwalten") {
			dummyDataProvider = new ListDataProvider<String>();
			
			String lehrveranstaltungen = "Lehrveranstaltungen";
			String dozenten = "Dozenten";
			String belegungen = "Belegungen";
			String raeume = "Raeume";
			String semesterverbaende = "Semesterverbaende";
			String studiengaenge = "Studiengaenge";
			String zeitslots = "Zeitslots";
			
			dummyDataProvider.getList().add(lehrveranstaltungen);
			dummyDataProvider.getList().add(dozenten);
			dummyDataProvider.getList().add(belegungen);
			dummyDataProvider.getList().add(raeume);
			dummyDataProvider.getList().add(semesterverbaende);
			dummyDataProvider.getList().add(studiengaenge);
			dummyDataProvider.getList().add(zeitslots);
			
			return new DefaultNodeInfo<String>(dummyDataProvider, new DummyCell(), selectionModel, null );
		}
		
		if (value instanceof String && (String)value == "Zeitslots") {
			dummyDataProvider = new ListDataProvider<String>();
			
			String montag = "Montag";
			String dienstag = "Dienstag";
			String mittwoch = "Mittwoch";
			String donnerstag = "Donnerstag";
			String freitag = "Freitag";
			String samstag = "Samstag";
			String sonntag = "Sonntag";
			
			dummyDataProvider.getList().add(montag);
			dummyDataProvider.getList().add(dienstag);
			dummyDataProvider.getList().add(mittwoch);
			dummyDataProvider.getList().add(donnerstag);
			dummyDataProvider.getList().add(freitag);
			dummyDataProvider.getList().add(samstag);
			dummyDataProvider.getList().add(sonntag);
			
			return new DefaultNodeInfo<String>(dummyDataProvider, new DummyCell(), selectionModel, null );
		}
		
		
		if (value instanceof String && (String)value == "Lehrveranstaltungen") {
			lehrveranstaltungDataProvider = new ListDataProvider<Lehrveranstaltung>();
			verwaltung.auslesenAlleLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>>() {
				public void onFailure(Throwable t) {
					Window.alert(t.toString());
				}
				
				public void onSuccess(Vector<Lehrveranstaltung> lehrveranstaltungen) {
					for (Lehrveranstaltung lehrveranstaltung : lehrveranstaltungen) {
						lehrveranstaltungDataProvider.getList().add(lehrveranstaltung);
					}
				}
			});
			
			return new DefaultNodeInfo<Lehrveranstaltung>(lehrveranstaltungDataProvider, new LehrveranstaltungCell(), selectionModel, null );
		}
		
		if (value instanceof String && (String)value == "Dozenten") {
			
			
			
			dozentDataProvider = new ListDataProvider<Dozent>();		
			
						
			verwaltung.auslesenAlleDozenten(new AsyncCallback<Vector<Dozent>>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}		

				public void onSuccess(Vector<Dozent> dozenten) {
					for (Dozent dozent : dozenten) {
						dozentDataProvider.getList().add(dozent);
					}
				}
			});
			
			return new DefaultNodeInfo<Dozent>(dozentDataProvider, new DozentCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value == "Raeume") {
			raumDataProvider = new ListDataProvider<Raum>();
			verwaltung.auslesenAlleRaeume(new AsyncCallback<Vector<Raum>>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}		

				public void onSuccess(Vector<Raum> raeume) {
					for (Raum raum : raeume) {
						raumDataProvider.getList().add(raum);
					}
				}
			});
			
			
			return new DefaultNodeInfo<Raum>(raumDataProvider, new RaumCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value == "Semesterverbaende") {
			semesterVerbandDataProvider = new ListDataProvider<Semesterverband>();
			verwaltung.auslesenAlleSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}		

				public void onSuccess(Vector<Semesterverband> semesterverbaende) {
					for (Semesterverband semesterverband : semesterverbaende) {
						semesterVerbandDataProvider.getList().add(semesterverband);
					}
				}
			});
			
			
			return new DefaultNodeInfo<Semesterverband>(semesterVerbandDataProvider, new SemesterverbandCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value == "Studiengaenge") {
			studiengangDataProvider = new ListDataProvider<Studiengang>();
			verwaltung.auslesenAlleStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}		

				public void onSuccess(Vector<Studiengang> studiengaenge) {
					for (Studiengang studiengang : studiengaenge) {
						studiengangDataProvider.getList().add(studiengang);
					}
				}
			});
			
			
			return new DefaultNodeInfo<Studiengang>(studiengangDataProvider, new StudiengangCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value == "Montag") {
			zeitslotDataProvider = new ListDataProvider<Zeitslot>();
			verwaltung.auslesenAlleZeitslots(new AsyncCallback<Vector<Zeitslot>>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}		

				public void onSuccess(Vector<Zeitslot> zeitslots) {
					for (Zeitslot zeitslot : zeitslots) {
						zeitslotDataProvider.getList().add(zeitslot);
					}
				}
			});
			
			
			return new DefaultNodeInfo<Zeitslot>(zeitslotDataProvider, new ZeitslotCell(), selectionModel, null);
		}
		
		if (value instanceof String && (String)value == "Belegungen") {
			belegungDataProvider = new ListDataProvider<Belegung>();
			verwaltung.auslesenAlleBelegungen(new AsyncCallback<Vector<Belegung>>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}		

				public void onSuccess(Vector<Belegung> belegungen) {
					for (Belegung belegung : belegungen) {
						belegungDataProvider.getList().add(belegung);
					}
				}
			});
			
			
			return new DefaultNodeInfo<Belegung>(belegungDataProvider, new BelegungCell(), selectionModel, null);
		}
		
					
		return null;
		
	}
	
	void updateDozent(Dozent dozent) {
		List<Dozent> dozentList = dozentDataProvider.getList();
		int i = 0;
		for(Dozent a : dozentList) {
			if(a.getId() == dozent.getId()) {
				dozentList.set(i, dozent);
				dozentDataProvider.refresh();
				break;
			}
			else {
				i++;
			}
		}
	}
	
	public void loeschenDozent(Dozent dozent) {
		
		int i = 0;
		
		for (Dozent d : dozentDataProvider.getList()) {
			if(d.getId() == dozent.getId()) {
				
				dozentDataProvider.getList().remove(i);
				dozentDataProvider.refresh();				
				break;
			}
			else {
				i++;
			}
		}
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
 * @author Tobi, Gino, Stefan
 * Anpassung LehrveranstaltungForm	
 */
	
	
	
	
public void loeschenLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		
		int i = 0;
		
		for (Lehrveranstaltung d : lehrveranstaltungDataProvider.getList()) {
			if(d.getId() == lehrveranstaltung.getId()) {
				
				lehrveranstaltungDataProvider.getList().remove(i);
				lehrveranstaltungDataProvider.refresh();				
				break;
			}
			else {
				i++;
			}
		}
				
	}
	
	
	
void updateLehrveranstaltung(Lehrveranstaltung lv) {
	List<Lehrveranstaltung> lvList = lehrveranstaltungDataProvider.getList();
	int i = 0;
	for(Lehrveranstaltung a : lvList) {
		if(a.getId() == lv.getId()) {
			lvList.set(i, lv);
			lehrveranstaltungDataProvider.refresh();
			break;
		}
		else {
			i++;
		}
	}
}
	
	
	
	
	
	
	
	
	
	
	public void addDozent(Dozent dozent) {
		if(dozentDataProvider != null) {
			dozentDataProvider.getList().add(dozentDataProvider.getList().size(), dozent);
			dozentDataProvider.refresh();
		}
	}
	
		
	public boolean isLeaf(Object value) {
		
		if (value instanceof String && (String)value == "Dozent") {
			return true;
		}
		
		else if (value instanceof String && (String)value == "Lehrveranstaltung") {
			return true;
		}
		else if (value instanceof String && (String)value == "Belegung") {
			return true;
		}
		else if (value instanceof String && (String)value == "Raum") {
			return true;
		}
		else if (value instanceof String && (String)value == "Semesterverband") {
			return true;
		}
		else if (value instanceof String && (String)value == "Studiengang") {
			return true;
		}
		else if (value instanceof String && (String)value == "Zeitslot") {
			return true;
		}
		
		else if (value instanceof Dozent) {
			return true;
		}
				
		else {
			return (value instanceof Lehrveranstaltung);
		}
		
	}
	
	public void setStundenplanttol2(Stundenplantool2 spt2) {
		this.spt2 = spt2;
	}
		

}
