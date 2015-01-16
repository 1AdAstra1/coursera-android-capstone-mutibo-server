package com.courseraproject.mutibo.model;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"user", "currentSet", "answeredSetIds"})
public class Game {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;
	private ArrayList<Long> answeredSetIds = new ArrayList<Long>();

	@OneToOne
	private Set currentSet = null;
	private int wrongAnswers = 0;
	private int score = 0;

	public static final int MAX_WRONG_ANSWERS = 3;
	public enum ActionResult {
		CORRECT(0), INCORRECT(1), GAME_OVER(2);
		private int statusCode;
		ActionResult(int statusCode) {
			this.statusCode = statusCode;
		}
		
		public int getStatusCode() {
			return this.statusCode;
		}
	}
	
	public long getId() {
		return id;
	}

	public int getWrongAnswers() {
		return wrongAnswers;
	}

	public void setWrongAnswers(int wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public User getUser() {
		return user;
	}
	
	public Game() {
		super();
		this.answeredSetIds.add((long)0);
	}
	
	public Game(User user) {
		this();
		this.user = user;
	}
	
	public ArrayList<Long> getAnsweredSetIds() {
		return answeredSetIds;
	}

	
	public void setCurrentSet(Set currentSet) {
		this.currentSet = currentSet;
	}

	public Set getCurrentSet() {
		return currentSet;
	}

	public int action(String answer) {
		int status;
		if(this.currentSet.guess(answer)) {
			status = ActionResult.CORRECT.getStatusCode();
			this.score += 1;
		} else {
			this.wrongAnswers += 1;
			if(this.wrongAnswers >= MAX_WRONG_ANSWERS) {
				finish();
				return ActionResult.GAME_OVER.getStatusCode();
			} else {
				status = ActionResult.INCORRECT.getStatusCode();
			}
		}
		this.answeredSetIds.add(this.currentSet.getId());
		this.currentSet = null;
		return status;		
	}
	
	public void finish() {
		user.updateStats(score);
	}
}
