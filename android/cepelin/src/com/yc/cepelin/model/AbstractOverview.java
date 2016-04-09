package com.yc.cepelin.model;

public abstract class AbstractOverview {

	private int id;
	
	private String title;
	private String shortDescription;

	private double longitude;
	private double latitude;

	private double rating;
	
	private Integer distance = 0;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}	
	
	public double getRating() {
		return rating;
	}	
	
	public void setRating(double rating) {
		this.rating = rating;
	}	

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}	
	
}
