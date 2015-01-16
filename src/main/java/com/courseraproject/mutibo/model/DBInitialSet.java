package com.courseraproject.mutibo.model;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 
 * Pre-loading the DB with some Sets for quick game experience
 *
 */
@Component
public class DBInitialSet {

	@Autowired
	private SetRepository setRepository;

	@Autowired
	private MovieRepository movieRepository;

	@PostConstruct
	public void populateDB() throws IOException {
		ArrayList<Set> dummySets = new ArrayList<Set>();

		ArrayList<Movie> movies = new ArrayList<Movie>();
		movies.add(new Movie("812", "Aladdin", "http://image.tmdb.org/t/p/w342/oA2AY6adBYrHSrvMQSfZ14yA0HA.jpg"));
		movies.add(new Movie("109445", "Frozen", "http://image.tmdb.org/t/p/w342/jIjdFXKUNtdf1bwqMrhearpyjMj.jpg"));
		movies.add(new Movie("11224", "Cinderella", "http://image.tmdb.org/t/p/w342/8zkulWy8yzjYEd8wGZp80wadtdD.jpg"));
		movies.add(new Movie("10020","Beauty and the Beast","http://image.tmdb.org/t/p/w342/b9QJr2oblOu1grgOMUZF1xkUJdh.jpg"));
		Set set = new Set(movies, "109445", "Frozen is a newer disney cartoon");
		dummySets.add(set);

		ArrayList<Movie> movies2 = new ArrayList<Movie>();
		movies2.add(new Movie("1832","Dogma","http://image.tmdb.org/t/p/w342/zuJJAEvyiZiddzRx7JRGNDzjP07.jpg"));
		movies2.add(new Movie("48572","Red State","http://image.tmdb.org/t/p/w342/h43jPTebYOabSXqNQqrRQpg6DPc.jpg"));
		movies2.add(new Movie("550","Fight Club","http://image.tmdb.org/t/p/w342/2lECpi35Hnbpa4y46JX0aY3AWTy.jpg"));
		movies2.add(new Movie("2292","Clerks","http://image.tmdb.org/t/p/w342/dmNytzvQTjylI83qrgDoS1KQ0ev.jpg"));
		Set set2 = new Set(movies2, "550", "Fight Club was not directed by Kevin Smith");
		dummySets.add(set2);

		ArrayList<Movie> movies3 = new ArrayList<Movie>();
		movies3.add(new Movie("6435","Practical Magic","http://image.tmdb.org/t/p/w342/cyvhyXn4lu6pnDEhwP3iCz2NFh6.jpg"));
		movies3.add(new Movie("9100","The Craft","http://image.tmdb.org/t/p/w342/5QoAIazIjidUcnD4Vggvmvg42NW.jpg"));
		movies3.add(new Movie("11157","She-Devil","http://image.tmdb.org/t/p/w342/kfoshWXV8oyh5MjvYdFrWTFLGpj.jpg"));
		movies3.add(new Movie("15674","Sabrina the Teenage Witch","http://image.tmdb.org/t/p/w342/q5jpHpIq5Kc4pzGGnabSGdzCKrK.jpg"));
		Set set3 = new Set(movies3, "11157", "She-Devil is not a movie about witches");
		dummySets.add(set3);
		
		ArrayList<Movie> movies4 = new ArrayList<Movie>();
		movies4.add(new Movie("2164","Stargate","http://image.tmdb.org/t/p/w342/muA1cUuVaWTRoaqkMtQGSxRPqJS.jpg"));
		movies4.add(new Movie("10940","Babylon 5: In the Beginning","http://image.tmdb.org/t/p/w342/6sYAd4rOPCwz6vjzlgWs3iNjSnX.jpg"));
		movies4.add(new Movie("686","Contact","http://image.tmdb.org/t/p/w342/yRF1qpaQPZJjiORDsR7eUHzSHbf.jpg"));
		movies4.add(new Movie("152","Star Trek: The Motion Picture","http://image.tmdb.org/t/p/w342/v9QzRtvl7SqvIx5w3P3cIbgWREx.jpg"));
		Set set4 = new Set(movies4, "686", "Contact didn't have a TV show associated with the movie");
		dummySets.add(set4);

		ArrayList<Movie> movies5 = new ArrayList<Movie>();
		movies5.add(new Movie("686","Contact","http://image.tmdb.org/t/p/w342/yRF1qpaQPZJjiORDsR7eUHzSHbf.jpg"));
		movies5.add(new Movie("10679","Iron Sky","http://image.tmdb.org/t/p/w342/4qyVtcCFqrlLMgHtlyxP57PI3ls.jpg"));
		movies5.add(new Movie("840","Close Encounters of the Third Kind","http://image.tmdb.org/t/p/w342/3Av8ZPiMrxFq9XiOjZl0tkoq9Oo.jpg"));
		movies5.add(new Movie("49047","Gravity","http://image.tmdb.org/t/p/w342/2gPjLWIyrWlAn2DgKMOKTBnZYyO.jpg"));
		Set set5 = new Set(movies5, "10679", "Iron Sky is more about comedy than serious sci-fi");
		dummySets.add(set5);

		for(Set s : dummySets) {
			for (Movie m : s.getMovies()) {
				if (movieRepository.findById(m.getId()) == null) {
					movieRepository.save(m);
				}
			}
			setRepository.save(s);
		}
	} 

}  
