package com.bmdb.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bmdb.business.Actor;
import com.bmdb.business.Movie;
import com.bmdb.business.MovieRepository;
import com.bmdb.util.JsonResponse;


@Controller
@RequestMapping(path = "/movies")
public class MovieController {
	@Autowired
	private MovieRepository movieRepo;

	@GetMapping(path = "/")
	public @ResponseBody JsonResponse getAllMovies() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(movieRepo.findAll());

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@GetMapping("/{id}")
	public @ResponseBody JsonResponse getMovie(@PathVariable int id) {
		JsonResponse jr=null;
		try {
			Optional<Movie> m = movieRepo.findById(id);
			if(m.isPresent()) {
				
				jr=JsonResponse.getInstance(m);
			}
			else {
				jr=JsonResponse.getInstance(
						new Exception("No stuffy found for id= "+id));
			}
				
			}
			catch(Exception e) {
				jr = JsonResponse.getInstance(e);
		}
			return jr;
		}
	@PutMapping(path="/")
	public @ResponseBody JsonResponse addNewMovie(@RequestBody Movie m) {
		JsonResponse jsonResponse = null;
		jsonResponse = JsonResponse.getInstance(saveMovie(m));	
		return jsonResponse;
	}
	
	@PutMapping("/{id}")
	public @ResponseBody JsonResponse updateMovie(@PathVariable int id, @RequestBody Movie m) {
		// should check to see if user exists first		
		return saveMovie(m);
	}	

	private @ResponseBody JsonResponse saveMovie(Movie m) {
		JsonResponse jsonResponse = null;
		try {
			movieRepo.save(m);
			jsonResponse = JsonResponse.getInstance(m);
		} catch (DataIntegrityViolationException e) {
			jsonResponse = JsonResponse.getInstance(new Exception(e.getMessage()));
		}
		return jsonResponse;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody JsonResponse removeMovie(@PathVariable int id) {
		JsonResponse jsonResponse = null;
		Optional<Movie> m = movieRepo.findById(id);
		if (m.isPresent()) {
			movieRepo.deleteById(id);
			jsonResponse = JsonResponse.getInstance(m);
		} else {
			jsonResponse = JsonResponse.getInstance(new Exception("Movie delete unsuccessful, movie " + id + " does not exist."));
		}
		return jsonResponse;
	}
}
	
	


