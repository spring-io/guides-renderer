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

package io.spring.renderer.github;

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
