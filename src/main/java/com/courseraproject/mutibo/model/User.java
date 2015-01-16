package com.courseraproject.mutibo.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties({"authorities", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "currentGame"})
public class User implements UserDetails{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private int lastScore = 0;
	private int highScore = 0;
	private int numGames = 0;
	
	@OneToOne(mappedBy="user", fetch=FetchType.LAZY)
	private Game currentGame = null;
	private String name;
	private String photoUrl;


	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AuthType authType;

	@Column(nullable = false)
	private String externalId;


	@Column
	@ElementCollection(fetch=FetchType.EAGER)
	private Collection<GrantedAuthority> authorities;


	public static UserDetails create(String username, AuthType authType,
			String appToken, String externalId, String... authorities) {
		return new User(username, authType, appToken, externalId, AuthorityUtils.createAuthorityList(authorities));
	}

	public User(String username, AuthType authType,
			String appToken, String externalId, Collection<GrantedAuthority> authorities) {
		super();
		name = username;
		this.authType = authType;
		this.password = appToken;
		this.authorities = authorities;
		this.externalId = externalId;		
	}


	public User() {
		super();
	}

	public long getId() {
		return id;
	}

	public String getExternalId() {
		return externalId;
	}

	public int getLastScore() {
		return lastScore;
	}

	public int getHighScore() {
		return highScore;
	}

	public int getNumGames() {
		return numGames;
	}

	public Game getCurrentGame() {
		return currentGame;
	}

	public void updateStats(int score) {
		this.lastScore = score;
		this.numGames += 1;
		if(score > this.highScore) {
			this.highScore = score;
		}
		this.currentGame = null;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public AuthType getAuthType() {
		return authType;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


}
