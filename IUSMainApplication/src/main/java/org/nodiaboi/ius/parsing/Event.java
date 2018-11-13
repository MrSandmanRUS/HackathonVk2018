package org.nodiaboi.ius.parsing;

import java.util.ArrayList;

public class Event {
	
	private String ID = "";
	private String NAME = "";
	private String DATE = "";
	private String CITY = "";
	private String TAG = "";
	private String COST = "0";
	private String AGE = "";
	private String OWNER = "";
	private String LINK = "";
	private ArrayList<String> USERS;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String dATE) {
		DATE = dATE;
	}
	public String getCITY() {
		return CITY;
	}
	public void setCITY(String cITY) {
		CITY = cITY;
	}
	public String getTAG() {
		return TAG;
	}
	public void setTAG(String tAG) {
		TAG = tAG;
	}
	public String getCOST() {
		return COST;
	}
	public void setCOST(String cOST) {
		COST = cOST;
	}
	public String getAGE() {
		return AGE;
	}
	public void setAGE(String aGE) {
		AGE = aGE;
	}
	public String getOWNER() {
		return OWNER;
	}
	public void setOWNER(String oWNER) {
		OWNER = oWNER;
	}
	public String getLINK() {
		return LINK;
	}
	public void setLINK(String lINK) {
		LINK = lINK;
	}
	public ArrayList<String> getUSERS() {
		return USERS;
	}
	public void setUSERS(ArrayList<String> uSERS) {
		USERS = uSERS;
	}
	
	public void println() {
		System.out.println("" +
				ID + "\n" + 
				NAME + "\n" + 
				DATE + "\n" + 
				CITY + "\n" + 
				TAG + "\n" + 
				COST + "\n" + 
				AGE + "\n" + 
				OWNER + "\n" + 
				LINK + "\n" + 
				USERS);
	}
}
