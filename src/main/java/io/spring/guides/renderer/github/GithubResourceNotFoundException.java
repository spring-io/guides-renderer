/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.github;

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
