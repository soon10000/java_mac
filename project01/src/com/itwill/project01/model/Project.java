package com.itwill.project01.model;

public class Project {
	

	public static final String COL_ID = "ID";
	public static final String COL_STORE = "STORE";
	public static final String COL_CATEGORI = "CATEGORI";
	public static final String COL_CITY = "CITY";
	public static final String COL_BOROUGH = "BOROUGH";
	public static final String COL_ADDRESS = "ADDRESS";
	public static final String COL_MEMO = "MEMO";
	public static final String COL_VISITE = "VISITE";
	public static final String COL_USERID = "USERID";
	
	
	private int id;
	private String store;
	private String categori;
	private String city;
	private String borough;
	private String address;
	private String memo;
	private int visite;
	private String userID;
	
	public Project() {}
	
	public Project(int id, String store, String categori, String city, String borough, String address, String memo,
			int visite, String userID) {
		this.id = id;
		this.store = store;
		this.categori = categori;
		this.city = city;
		this.borough = borough;
		this.address = address;
		this.memo = memo;
		this.visite = visite;
		this.userID = userID;
	}


	
	
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getStore() {
		return store;
	}


	public void setStore(String store) {
		this.store = store;
	}


	public String getCategori() {
		return categori;
	}


	public void setCategori(String categori) {
		this.categori = categori;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getBorough() {
		return borough;
	}


	public void setBorough(String borough) {
		this.borough = borough;
	}


	public String getAddress() {
		return address;
	}


	public void setAdress(String address) {
		this.address = address;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}


	public int getVisite() {
		return visite;
	}


	public void setVisite(int visite) {
		this.visite = visite;
	}


	@Override
	public String toString() {
		return "Project [store=" + store + ", categori=" + categori + ", city=" + city + ", borough=" + borough
				+ ", adress=" + address + ", memo=" + memo + ", visite=" + visite + "]";
	}
	
	
	
	

}
