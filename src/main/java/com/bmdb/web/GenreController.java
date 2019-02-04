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

import com.bmdb.business.Genre;
import com.bmdb.business.GenreRepository;
import com.bmdb.util.JsonResponse;



@Controller
@RequestMapping(path="/genres")

public class GenreController {
	

		@Autowired
		private GenreRepository genreRepo;
		
		@GetMapping("/")	
		public @ResponseBody JsonResponse getAllGenres() {
			JsonResponse jsonResponse = null;
			try {
				jsonResponse = JsonResponse.getInstance(genreRepo.findAll());
				
			} catch (Exception e) {
				jsonResponse = JsonResponse.getInstance(e);
			}		
			return jsonResponse;
		}
		
		@GetMapping("/{id}")
		public @ResponseBody JsonResponse getGenre(@PathVariable int id) {
			JsonResponse jsonResponse = null;		
			try {
				Optional<Genre> g = genreRepo.findById(id);
				if (g.isPresent()) {
					jsonResponse = JsonResponse.getInstance(g);
				} else {
					jsonResponse = JsonResponse.getInstance(new Exception("No genre found for id = " + id));
				}
			} catch (Exception e) {
				jsonResponse = JsonResponse.getInstance(e);
			}		
			return jsonResponse;
		}
		
		@PostMapping(path="/")
		public @ResponseBody JsonResponse addNewGenre(@RequestBody Genre g) {
			JsonResponse jsonResponse = null;
			jsonResponse = JsonResponse.getInstance(saveGenre(g));
			return jsonResponse;
		}
		
		@PutMapping("/{id}")
		public @ResponseBody JsonResponse updateGenre(@PathVariable int id, @RequestBody Genre g) {
			// should check to see if user exists first		
			return saveGenre(g);
		}	

		private @ResponseBody JsonResponse saveGenre(Genre g) {
			JsonResponse jsonResponse = null;
			try {
				genreRepo.save(g);
				jsonResponse = JsonResponse.getInstance(g);
			} catch (DataIntegrityViolationException e) {
				jsonResponse = JsonResponse.getInstance(new Exception(e.getMessage()));
			}
			return jsonResponse;
		}
		
		@DeleteMapping("/{id}")
		public @ResponseBody JsonResponse removeGenre(@PathVariable int id) {
			JsonResponse jsonResponse = null;
			Optional<Genre> g = genreRepo.findById(id);
			if (g.isPresent()) {
				genreRepo.deleteById(id);
				jsonResponse = JsonResponse.getInstance(g);
			} else {
				jsonResponse = JsonResponse.getInstance(new Exception("Could not delete Genre " + id ));
			}
			return jsonResponse;
		}

}

