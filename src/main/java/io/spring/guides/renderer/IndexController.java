/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer;

import io.spring.guides.renderer.guides.GuidesController;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Lists all resources at the root of the application
 */
@RestController
public class IndexController {

	@GetMapping(path = "/", produces = MediaTypes.HAL_JSON_VALUE)
	public RepresentationModel index() {
		RepresentationModel resource = new RepresentationModel();
		resource.add(linkTo(methodOn(GuidesController.class).listGuides()).withRel("guides"));
		return resource;
	}

}
