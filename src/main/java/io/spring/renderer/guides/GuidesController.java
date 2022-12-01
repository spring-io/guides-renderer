/*
 * Copyright 2022-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.renderer.guides;

import java.util.List;
import java.util.stream.Collectors;

import io.spring.renderer.RendererProperties;
import io.spring.renderer.github.GithubClient;
import io.spring.renderer.github.GithubResourceNotFoundException;
import io.spring.renderer.github.Repository;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * API for listing guides repositories and rendering them as {@link GuideContentModel}
 */
@RestController
@RequestMapping(path = "/guides", produces = MediaTypes.HAL_JSON_VALUE)
public class GuidesController {

	private final GuideRenderer guideRenderer;

	private final GithubClient githubClient;

	private final RendererProperties properties;

	private final GuideModelAssembler guideAssembler = new GuideModelAssembler();

	public GuidesController(GuideRenderer guideRenderer, GithubClient github, RendererProperties properties) {
		this.guideRenderer = guideRenderer;
		this.githubClient = github;
		this.properties = properties;
	}

	@ExceptionHandler(GithubResourceNotFoundException.class)
	public ResponseEntity resourceNotFound() {
		return ResponseEntity.notFound().build();
	}

	@GetMapping("")
	public CollectionModel<GuideModel> listGuides() {
		List<GuideModel> guideModels = this.guideAssembler
				.toCollectionModel(
						this.githubClient.fetchOrgRepositories(this.properties.getGithub().getOrganization()))
				.getContent().stream().filter(guide -> !guide.getType().equals(GuideType.UNKNOWN))
				.collect(Collectors.toList());
		CollectionModel<GuideModel> resources = CollectionModel.of(guideModels);
		for (GuideType type : GuideType.values()) {
			if (!GuideType.UNKNOWN.equals(type)) {
				resources.add(linkTo(methodOn(GuidesController.class).showGuide(type.getSlug(), null))
						.withRel(type.getSlug()));
			}
		}
		return resources;
	}

	@GetMapping("/{type}/{guide}")
	public ResponseEntity<GuideModel> showGuide(@PathVariable String type, @PathVariable String guide) {
		GuideType guideType = GuideType.fromSlug(type);
		if (GuideType.UNKNOWN.equals(guideType)) {
			return ResponseEntity.notFound().build();
		}
		Repository repository = this.githubClient.fetchOrgRepository(this.properties.getGithub().getOrganization(),
				guideType.getPrefix() + guide);
		GuideModel guideModel = this.guideAssembler.toModel(repository);
		if (guideModel.getType().equals(GuideType.UNKNOWN)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(guideModel);
	}

	@GetMapping("/{type}/{guide}/content")
	public ResponseEntity<GuideContentModel> renderGuide(@PathVariable String type, @PathVariable String guide) {
		GuideType guideType = GuideType.fromSlug(type);
		if (GuideType.UNKNOWN.equals(guideType)) {
			return ResponseEntity.notFound().build();
		}
		GuideContentModel guideContentModel = this.guideRenderer.render(guideType, guide);
		guideContentModel
				.add(linkTo(methodOn(GuidesController.class).renderGuide(guideType.getSlug(), guide)).withSelfRel());
		guideContentModel
				.add(linkTo(methodOn(GuidesController.class).showGuide(guideType.getSlug(), guide)).withRel("guide"));
		return ResponseEntity.ok(guideContentModel);
	}

}