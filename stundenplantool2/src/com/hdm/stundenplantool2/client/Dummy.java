package com.hdm.stundenplantool2.client;

public class Dummy {
	
	public String bezeichnung = "";
	
	public static class EditorD extends Dummy {
		
		public String bezeichnung = "Editor";
		
		public static class VerwaltenDE extends EditorD {
			
			public String bezeichnung = "Verwalten";
			
			public static class DozentDEV extends VerwaltenDE {
				
				public String bezeichnung = "Dozenten";
								
			}
			
			public static  class BelegungDEV extends VerwaltenDE {
				
				public String bezeichnung = "Belegungen";
				
			}
			
			public static class LehrveranstaltungDEV extends VerwaltenDE {
				
				public String bezeichnung = "Lehrveranstaltungen";
				
			}
			
			public static class RaumDEV extends VerwaltenDE {
				
				public String bezeichnung = "Raeume";
				
			}
			
			public static class SemesterverbandDEV extends VerwaltenDE {
				
				public String bezeichnung = "Semesterverband";
				
			}
			
			public static class StudiengangDEV extends VerwaltenDE {
				
				public String bezeichnung = "Studiengang";
				
			}
			
			public static class ZeitslotDEV extends VerwaltenDE {
				
				public String bezeichnung = "Zeitslot";
				
			}
		}	
	
	
		public static class AnlegenDE extends EditorD {
			
			public static class DozentDEA extends AnlegenDE {
				
				public String bezeichnung = "Dozent";
				
			}
			
			public static  class BelegungDEA extends AnlegenDE {
				
				public String bezeichnung = "Belegung";
				
			}
			
			public static class LehrveranstaltungDEA extends AnlegenDE {
				
				public String bezeichnung = "Lehrveranstaltung";
				
			}
			
			public static class RaumDEA extends AnlegenDE {
				
				public String bezeichnung = "Raum";
				
			}
			
			public static class SemesterverbandDEA extends AnlegenDE {
				
				public String bezeichnung = "Semesterverband";
				
			}
			
			public static class StudiengangDEA extends AnlegenDE {
				
				public String bezeichnung = "Studiengang";
				
			}
			
			public static class ZeitslotDEA extends AnlegenDE {
				
				public String bezeichnung = "Zeitslot";
				
			}
		
		}
		
	}
	
	public static class ReportD extends Dummy {
		
		public String bezeichnung = "Report";
		
		public static class DozentDR extends ReportD {
			
			public String bezeichnung = "Dozentenplan";
			
		}
				
		public static class RaumDR extends ReportD {
			
			public String bezeichnung = "Raumplan";
			
		}
		
		public static class SemesterverbandDR extends ReportD {
			
			public String bezeichnung = "Stundenplan";
			
		}
		
		
	}
	
	public String getBezeichnung() {
		return bezeichnung;
	}
	
}	

