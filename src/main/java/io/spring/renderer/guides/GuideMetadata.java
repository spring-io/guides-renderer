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

import java.util.Set;

import io.spring.renderer.github.Repository;

/**
 * Metadata needed to create a {@link GuideModel}.
 *
 * @author Madhura Bhave
 */
class GuideMetadata {

	private final Repository repository;

	private final String academyUrl;

	private final Set<String> category;

	GuideMetadata(Repository repository, String academyUrl, Set<String> category) {
		this.repository = repository;
		this.academyUrl = academyUrl;
		this.category = category;
	}

	public Repository getRepository() {
		return this.repository;
	}

	public String getAcademyUrl() {
		return this.academyUrl;
	}

	public Set<String> getCategory() {
		return this.category;
	}

}
