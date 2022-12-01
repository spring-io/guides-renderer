/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer;

import javax.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for the Sagan Renderer application
 */
@ConfigurationProperties("renderer")
@Validated
public class RendererProperties {

	private final Github github = new Github();

	public Github getGithub() {
		return this.github;
	}

	public static class Github {

		/**
		 * Access token to query public github endpoints.
		 * https://developer.github.com/v3/auth/#authenticating-for-saml-sso
		 */
		@Pattern(regexp = "([0-9a-zA-Z_]*)?")
		private String token;

		public String getToken() {
			return this.token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		/**
		 * Name of the Github organization to fetch guides from.
		 */
		private String organization = "spring-guides";

		public String getOrganization() {
			return this.organization;
		}

		public void setOrganization(String organization) {
			this.organization = organization;
		}

	}

}
