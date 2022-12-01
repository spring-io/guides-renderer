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

import io.spring.renderer.github.Repository;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

class GuideModelAssembler extends RepresentationModelAssemblerSupport<Repository, GuideModel> {

	GuideModelAssembler() {
		super(GuidesController.class, GuideModel.class);
	}

	@Override
	public GuideModel toModel(Repository repository) {
		GuideModel resource = new GuideModel(repository);
		resource.add(
				linkTo(methodOn(GuidesController.class).showGuide(resource.getType().getSlug(), resource.getName()))
						.withSelfRel());
		resource.add(
				linkTo(methodOn(GuidesController.class).renderGuide(resource.getType().getSlug(), resource.getName()))
						.withRel("content"));
		resource.add(linkTo(methodOn(GuidesController.class).listGuides()).withRel("guides"));
		return resource;
	}

}
