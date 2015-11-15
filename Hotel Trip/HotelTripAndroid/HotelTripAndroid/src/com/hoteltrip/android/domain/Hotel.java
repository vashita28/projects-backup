package com.hoteltrip.android.domain;

public class Hotel {

	private String name;
	private String streetName;
	private String address;
	private Double pricePerNight;
	private int reviewScore;
	private Double lat;
	private Double lon;
	private float starRating;
	private int distance;
	private int hotelImageDrawable;
	private boolean hotelIsFavorite;

	public Hotel(String name, String streetName, String address,
			Double pricePerNight, int reviewScore, Double lat, Double lon,
			float starRating, int distance, int imageDrawable,
			boolean hotelIsFavorite) {
		super();
		this.name = name;
		this.streetName = streetName;
		this.address = address;
		this.pricePerNight = pricePerNight;
		this.reviewScore = reviewScore;
		this.lat = lat;
		this.lon = lon;
		this.starRating = starRating;
		this.distance = distance;
		this.hotelImageDrawable = imageDrawable;
		this.hotelIsFavorite = hotelIsFavorite;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getPricePerNight() {
		return pricePerNight;
	}

	public void setPricePerNight(Double pricePerNight) {
		this.pricePerNight = pricePerNight;
	}

	public int getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(int reviewScore) {
		this.reviewScore = reviewScore;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public float getStarRating() {
		return starRating;
	}

	public void setStarRating(float starRating) {
		this.starRating = starRating;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getHotelImage() {
		return hotelImageDrawable;
	}

	public void setHotelImage(int hotelImageDrawable) {
		this.hotelImageDrawable = hotelImageDrawable;
	}

	public boolean getHotelIsFavorite() {
		return hotelIsFavorite;
	}

	public void getHotelIsFavorite(boolean hotelIsFavorite) {
		this.hotelIsFavorite = hotelIsFavorite;
	}
}
