package com.courseraproject.mutibo.model;

import java.util.Collection;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="\"Set\"")
@JsonIgnoreProperties({"ratedBy", "rating"})
public class Set  {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
	      name="sets_movies",
	      joinColumns={@JoinColumn(name="set_id", referencedColumnName="id")},
	      inverseJoinColumns={@JoinColumn(name="movie_id", referencedColumnName="id")})
	private Collection<Movie> movies;
	private String answer;
	private String explanation;
	private int rating = 0;
	private HashMap<Long, Boolean> ratedBy = new HashMap<Long, Boolean>();

	public Set(Collection<Movie> movies, String answer, String explanation) {
		super();
		this.movies = movies;
		this.answer = answer;
		this.explanation = explanation;
	}

	public Set() {
		super();
	}

	public long getId() {
		return id;
	}

	public Collection<Movie> getMovies() {
		return movies;
	}

	public void setMovies(Collection<Movie> movies) {
		this.movies = movies;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public boolean guess(String probableAnswer) {
		return this.answer.equals(probableAnswer);
	}
	
	public boolean hasBeenRated(long userId) {
		return this.ratedBy.containsKey(userId);
	}

	public void rate(boolean vote, long userId) {
		if (vote) {
			this.rating += 1;
		} else {
			this.rating -= 1;
		}
		this.ratedBy.put(userId, vote);
	}

	public int getRating() {
		return rating;
	}

}
