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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GithubResourceNotFoundException extends RuntimeException {

	private final String resourceName;

	public GithubResourceNotFoundException(String orgName, String repositoryName, Throwable cause) {
		super("Could not find github repository [" + orgName + "/" + repositoryName + "]", cause);
		this.resourceName = "Repository [" + orgName + "/" + repositoryName + "]";
	}

	public GithubResourceNotFoundException(String orgName, Throwable cause) {
		super("Could not find github organization [" + orgName + "]", cause);
		this.resourceName = "Organization [" + orgName + "]";
	}

	public String getResourceName() {
		return this.resourceName;
	}

}
