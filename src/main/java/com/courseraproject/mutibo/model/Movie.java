package com.courseraproject.mutibo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Movie {
	@Id
	private String id;
	
	private String title;
	
	private String posterUrl;
	
	private String language;
	
	public Movie() {
		super();
	}
	
	public Movie(String id, String title, String posterUrl) {
		super();
		this.id = id;
		this.title = title;
		this.posterUrl = posterUrl;
	}



	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getPosterUrl() {
		return posterUrl;
	}
	
	public void setPosterUrl(String newPosterUrl) {
		this.posterUrl = newPosterUrl;
	}
	
	public String getLanguage() {
		return language;
	}
	

}
