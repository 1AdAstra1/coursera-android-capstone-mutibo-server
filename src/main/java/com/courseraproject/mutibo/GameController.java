package com.courseraproject.mutibo;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.courseraproject.mutibo.model.Game;
import com.courseraproject.mutibo.model.GameRepository;
import com.courseraproject.mutibo.model.Movie;
import com.courseraproject.mutibo.model.MovieRepository;
import com.courseraproject.mutibo.model.Set;
import com.courseraproject.mutibo.model.SetRepository;
import com.courseraproject.mutibo.model.User;
import com.courseraproject.mutibo.model.UserRepository;

/**
 * Handles requests for the application game mechanics
 */
@RestController
public class GameController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private SetRepository setRepository;

	@Autowired
	private MovieRepository movieRepository;

	private static final int BAD_RATING_THRESHOLD = -3;

	@RequestMapping(value = "/game", method = RequestMethod.POST)
	public Game newGame(Authentication auth, final HttpServletResponse response) {
		User u = (User) auth.getPrincipal();
		response.setStatus(201);
		Game game = u.getCurrentGame();
		if (game == null) {
			game = new Game(u);
			u.setCurrentGame(game);
			gameRepository.save(game);
			userRepository.save(u);
		}
		return game;
	}

	@RequestMapping(value = "/game/{id}/set", method = RequestMethod.GET)
	public Set nextSet(@PathVariable("id") long gameId, Authentication auth) {
		User user = (User) auth.getPrincipal();
		Game game = user.getCurrentGame();
		// I don't like ambiguous state-dependent paths (so I do have game IDs
		// in them, despite the fact that they're totally optional),
		// but I also don't want users to attempt playing somebody else's games
		if (game.getId() != gameId) {
			throw new ResourceNotFoundException();
		}
		Set nextSet = game.getCurrentSet();

		if (nextSet == null) {
			// if anyone knows an easier way to select 1 row with a custom
			// randomized query, please tell me
			Pageable first = new PageRequest(0, 1);
			Iterator<Set> itemIterator = setRepository.findNextUnanswered(
					game.getAnsweredSetIds(), first).iterator();
			if (itemIterator.hasNext()) {
				nextSet = itemIterator.next();
			} else {
				//this user has answered all Sets!
				game.finish();
				gameRepository.delete(game);
				userRepository.save(user);
				throw new ResourceNotFoundException();
			}
			game.setCurrentSet(nextSet);
			gameRepository.save(game);
		}
		return nextSet;
	}

	@RequestMapping(value = "/game/{id}/set/{set_id}", method = RequestMethod.POST)
	public HashMap<String, Integer> action(@PathVariable("id") long gameId,
			@PathVariable("set_id") long setId,
			@RequestParam("answer") String answer, Authentication auth) {
		User user = (User) auth.getPrincipal();
		Game game = user.getCurrentGame();
		if (game.getId() != gameId) {
			throw new ResourceNotFoundException();
		}
		Set set = getSetById(setId);
		int status = game.action(answer);
		if (status == Game.ActionResult.GAME_OVER.getStatusCode()) {
			gameRepository.delete(game);
		} else {
			gameRepository.save(game);
		}
		userRepository.save(user);

		HashMap<String, Integer> result = new HashMap<String, Integer>();
		result.put("status", status);
		result.put("score", game.getScore());
		int hasBeenRated = set.hasBeenRated(user.getId()) ? 1 : 0;
		result.put("hasBeenRated", hasBeenRated);
		return result;
	}

	@RequestMapping(value = "/set", method = RequestMethod.POST)
	public Set addSet(@RequestBody Set newSet,
			final HttpServletResponse response) {
		// quite weird, but JPA does not natively support cascading 'upsert'
		// operation for
		// many-to-many entities with external IDs, will have to add movies one
		// by one
		// to avoid constraint violation on repeated entries
		for (Movie m : newSet.getMovies()) {
			if (movieRepository.findById(m.getId()) == null) {
				movieRepository.save(m);
			}
		}
		setRepository.save(newSet);
		response.setStatus(201);
		return newSet;
	}

	@RequestMapping(value = "/set/{id}", method = RequestMethod.PUT)
	public void rateSet(@PathVariable("id") long setId,
			@RequestParam("vote") boolean vote, Authentication auth,
			final HttpServletResponse response) {
		Set set = getSetById(setId);
		User u = (User) auth.getPrincipal();
		if (set.hasBeenRated(u.getId())) {
			throw new BadRequestException();
		}
		set.rate(vote, u.getId());
		if (set.getRating() < BAD_RATING_THRESHOLD) {
			setRepository.delete(set);
		} else {
			setRepository.save(set);
		}
		response.setStatus(204);
	}

	private Set getSetById(long setId) {
		Set set = setRepository.findOne(setId);
		if (set == null) {
			throw new ResourceNotFoundException();
		}
		return set;
	}

}
