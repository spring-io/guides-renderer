/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.guides;

import io.spring.guides.renderer.github.Repository;

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

	GuideModel(Repository repository) {
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

}