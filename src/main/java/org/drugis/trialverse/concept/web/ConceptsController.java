package org.drugis.trialverse.concept.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.drugis.trialverse.concept.Concept;
import org.drugis.trialverse.concept.ConceptRepository;
import org.drugis.trialverse.concept.ConceptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/concepts/{id}")
@ExposesResourceFor(Concept.class)
public class ConceptsController {
	private interface Fetcher {
		public List<Concept> fetch(UUID conceptId);
	}
	
	private final ConceptRepository d_concepts;
	private final EntityLinks d_entityLinks; 
		
	@Autowired
	public ConceptsController(ConceptRepository concepts, EntityLinks entityLinks) {
		Assert.notNull(entityLinks, "EntityLinks must not be null!");
		Assert.notNull(concepts, "ConceptRepository must not be null!");
		d_concepts = concepts;
		d_entityLinks = entityLinks;
	}
	
	@ResponseBody
	@RequestMapping(value = "treatments", method = RequestMethod.GET)
	public ResponseEntity<List<Resource<Concept>>> getTreatments(@PathVariable("id") UUID conceptId) {
		Fetcher fetcher = new Fetcher() {
			public List<Concept> fetch(UUID conceptId) {
				return d_concepts.findTreatmentsByIndication(conceptId);
			}
		};
		return fetchConceptsFor(conceptId, fetcher);
	}
	
	@ResponseBody
	@RequestMapping(value = "variables", method = RequestMethod.GET)
	public ResponseEntity<List<Resource<Concept>>> getVariables(@PathVariable("id") UUID conceptId) {
		Fetcher fetcher = new Fetcher() {
			public List<Concept> fetch(UUID conceptId) {
				return d_concepts.findVariablesByIndication(conceptId);
			}
		};
		return fetchConceptsFor(conceptId, fetcher);
	}

	private ResponseEntity<List<Resource<Concept>>> fetchConceptsFor(UUID conceptId, Fetcher fetcher) {
		Concept concept = d_concepts.findOne(conceptId);
		if (concept.getType().equals(ConceptType.INDICATION)) { 
			List<Concept> treatments = fetcher.fetch(conceptId);
			List<Resource<Concept>> result = new ArrayList<>();
			for(Concept treatment : treatments) { 
				Resource<Concept> resource = new Resource<Concept>(treatment);
				result.add(resource);
				resource.add(d_entityLinks.linkForSingleResource(treatment).withSelfRel());
			}
			return new ResponseEntity<List<Resource<Concept>>>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Resource<Concept>>>(HttpStatus.NOT_FOUND);
		}
	}
}
