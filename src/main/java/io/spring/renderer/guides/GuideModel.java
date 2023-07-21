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

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * A Spring Guide ("Getting started guide", "Topical guide" or "Tutorial") is a document
 * for learning Spring technologies, backed by a Github repository.
 */
@Relation(collectionRelation = "guides")
class GuideModel extends RepresentationModel<GuideModel> {

	private String name;

	private String repositoryName;

	private String title;

	private String description;

	private GuideType type;

	private String githubUrl;

	private String gitUrl;

	private String sshUrl;

	private String cloneUrl;

	private String[] projects;

	private String academyUrl;

	GuideModel(GuideMetadata guideMetadata) {
		Repository repository = guideMetadata.getRepository();
		this.type = GuideType.fromRepositoryName(repository.getName());
		this.name = this.type.stripPrefix(repository.getName());
		this.repositoryName = repository.getFullName();
		String description = repository.getDescription();
		if (description != null) {
			String[] split = repository.getDescription().split("::");
			this.title = split[0].trim();
			this.description = (split.length > 1) ? split[1].trim() : "";
		}
		else {
			this.title = "";
			this.description = "";
		}
		this.githubUrl = repository.getHtmlUrl();
		this.gitUrl = repository.getGitUrl();
		this.sshUrl = repository.getSshUrl();
		this.cloneUrl = repository.getCloneUrl();
		if (repository.getTopics() != null) {
			this.projects = repository.getTopics().toArray(new String[0]);
		}
		else {
			this.projects = new String[0];
		}
		this.academyUrl = guideMetadata.getAcademyUrl();
	}

	public String getName() {
		return this.name;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public GuideType getType() {
		return this.type;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public String getGitUrl() {
		return gitUrl;
	}

	public String getSshUrl() {
		return sshUrl;
	}

	public String getCloneUrl() {
		return cloneUrl;
	}

	public String[] getProjects() {
		return this.projects;
	}

	public String getAcademyUrl() {
		return this.academyUrl;
	}

}