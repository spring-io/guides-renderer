/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.guides;

public class GuideRenderingException extends RuntimeException {

	private final String repositoryName;

	public GuideRenderingException(String repositoryName, Throwable cause) {
		super("Could not render guide [" + repositoryName + "]", cause);
		this.repositoryName = repositoryName;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

}
