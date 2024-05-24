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

package io.spring.renderer;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import io.spring.renderer.RendererProperties.Webhook.Category;
import jakarta.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for the Sagan Renderer application
 */
@ConfigurationProperties("renderer")
@Validated
public class RendererProperties {

	private final Github github = new Github();

	/**
	 * Mappings from guide names to correspoding spring academy urls.
	 */
	private final Map<String, String> academy = new HashMap<>();

	private final Map<String, Category> category = new HashMap<>();

	public Github getGithub() {
		return this.github;
	}

	public Map<String, String> getAcademy() {
		return this.academy;
	}

	public Map<String, Category> getCategory() {
		return this.category;
	}

	public static class Github {

		/**
		 * Access token to query public github endpoints.
		 * https://developer.github.com/v3/auth/#authenticating-for-saml-sso
		 */
		@Pattern(regexp = "([0-9a-zA-Z_]*)?")
		private String token;

		/**
		 * Name of the Github organization to fetch guides from.
		 */
		private String organization = "spring-guides";

		private Webhook webhook = new Webhook();

		public String getToken() {
			return this.token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getOrganization() {
			return this.organization;
		}

		public void setOrganization(String organization) {
			this.organization = organization;
		}

		public Webhook getWebhook() {
			return this.webhook;
		}

	}

	public static class Webhook {

		/**
		 * Token configured in GitHub webhooks for this application.
		 */
		private String secret = "changeme";

		/**
		 * Org name for dispatching Github Action.
		 */
		private String actionOrg;

		/**
		 * Repository name for dispatching Github Action.
		 */
		private String actionRepo;

		/**
		 * Token with repo scope for for dispatching Github Action.
		 */
		private String dispatchToken;

		public String getSecret() {
			return this.secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public String getActionOrg() {
			return this.actionOrg;
		}

		public void setActionOrg(String actionOrg) {
			this.actionOrg = actionOrg;
		}

		public String getActionRepo() {
			return this.actionRepo;
		}

		public void setActionRepo(String actionRepo) {
			this.actionRepo = actionRepo;
		}

		public String getDispatchToken() {
			return this.dispatchToken;
		}

		public void setDispatchToken(String dispatchToken) {
			this.dispatchToken = dispatchToken;
		}

		public static class Category {

			private String displayName;

			private final Set<String> guide = new LinkedHashSet<>();

			public String getDisplayName() {
				return this.displayName;
			}

			public void setDisplayName(String displayName) {
				this.displayName = displayName;
			}

			public Set<String> getGuide() {
				return this.guide;
			}

		}

	}

}
