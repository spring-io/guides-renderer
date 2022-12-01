/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.github;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Github repository information
 */
public class Repository {

	private Long id;

	private String name;

	private String fullName;

	private String description;

	private String htmlUrl;

	private String gitUrl;

	private String sshUrl;

	private String cloneUrl;

	private List<String> topics;

	@JsonCreator
	public Repository(@JsonProperty("id") Long id, @JsonProperty("name") String name,
			@JsonProperty("full_name") String fullName, @JsonProperty("description") String description,
			@JsonProperty("html_url") String htmlUrl, @JsonProperty("git_url") String gitUrl,
			@JsonProperty("ssh_url") String sshUrl, @JsonProperty("clone_url") String cloneUrl,
			@JsonProperty("topics") List<String> topics) {
		this.name = name;
		this.fullName = fullName;
		this.description = description;
		this.htmlUrl = htmlUrl;
		this.gitUrl = gitUrl;
		this.sshUrl = sshUrl;
		this.cloneUrl = cloneUrl;
		this.topics = topics;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getDescription() {
		return this.description;
	}

	public String getHtmlUrl() {
		return this.htmlUrl;
	}

	public String getGitUrl() {
		return this.gitUrl;
	}

	public String getSshUrl() {
		return sshUrl;
	}

	public String getCloneUrl() {
		return cloneUrl;
	}

	public List<String> getTopics() {
		return topics;
	}

}
