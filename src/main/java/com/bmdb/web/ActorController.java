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
import com.bmdb.business.ActorRepository;

import com.bmdb.util.JsonResponse;

@Controller
@RequestMapping(path = "/actors")
public class ActorController {
	@Autowired
	private ActorRepository actorRepo;

	// get a list of all actors
	@GetMapping(path = "/")
	public @ResponseBody JsonResponse getAllActors() {
		com.bmdb.util.JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(actorRepo.findAll());

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
//doeesn't work need changes
	@GetMapping(path = "/getActorByGender")
	public @ResponseBody JsonResponse getActorByGender(@PathVariable String gender) {
		JsonResponse jr = null;
		try {
			Actor a = actorRepo.findByGender(gender);
			String male = "";
			if (gender == male) {

				jr = JsonResponse.getInstance(a);
			} else {
				jr = JsonResponse.getInstance(new Exception("No Actors found for gender= " + gender));
			}

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public @ResponseBody JsonResponse getActor(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<Actor> a = actorRepo.findById(id);
			if (a.isPresent()) {
				// good call to DB return a valid actor
				jr = JsonResponse.getInstance(a);
			} else {
				jr = JsonResponse.getInstance(new Exception("No actor found for id= " + id));
			}

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PostMapping(path="/")
	public @ResponseBody JsonResponse addNewActor(@RequestBody Actor a) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request
		JsonResponse jsonResponse = null;
		jsonResponse = JsonResponse.getInstance(saveActor(a));	
		return jsonResponse;
	}
	
	@PutMapping("/{id}")
	public @ResponseBody JsonResponse updateActor(@PathVariable int id, @RequestBody Actor a) {
		// should check to see if user exists first		
		return saveActor(a);
	}	

	private @ResponseBody JsonResponse saveActor(Actor a) {
		JsonResponse jsonResponse = null;
		try {
			actorRepo.save(a);
			jsonResponse = JsonResponse.getInstance(a);
		} catch (DataIntegrityViolationException e) {
			jsonResponse = JsonResponse.getInstance(new Exception(e.getMessage()));
		}
		return jsonResponse;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody JsonResponse removeActor(@PathVariable int id) {
		JsonResponse jsonResponse = null;
		Optional<Actor> a = actorRepo.findById(id);
		if (a.isPresent()) {
			actorRepo.deleteById(id);
			jsonResponse = JsonResponse.getInstance(a);
		} else {
			jsonResponse = JsonResponse.getInstance(new Exception("Actor delete unsuccessful, actor " + id + " does not exist."));
		}
		return jsonResponse;
	}
}
