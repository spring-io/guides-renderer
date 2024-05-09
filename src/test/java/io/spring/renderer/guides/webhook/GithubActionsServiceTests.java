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

package io.spring.renderer.guides.webhook;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * Test for {@link GithubActionsService}.
 *
 * @author Madhura Bhave
 */
class GithubActionsServiceTests {

	private static final String ORG_NAME = "test-org";

	private static final String REPO_NAME = "test-repo";

	private static final String TOKEN = "token";

	private MockRestServiceServer server;

	private RestTemplateBuilder restTemplateBuilder;

	private GithubActionsService service;

	private static final String DISPATCH_PATH = "https://api.github.com/repos/test-org/test-repo/dispatches";

	@BeforeEach
	void setup() {
		MockServerRestTemplateCustomizer mockServerCustomizer = new MockServerRestTemplateCustomizer();
		this.restTemplateBuilder = new RestTemplateBuilder(mockServerCustomizer);
		this.service = new GithubActionsService(this.restTemplateBuilder);
		this.server = mockServerCustomizer.getServer();
	}

	@Test
	void triggerRepositoryDispatchWhenSuccessful() {
		this.server.expect(requestTo(DISPATCH_PATH))
			.andExpect(header("Authorization", "Bearer token"))
			.andExpect(header("Accept", "application/vnd.github+json"))
			.andExpect(content().json("{\"event_type\": \"guides\"}"))
			.andRespond(withNoContent());
		this.service.triggerRespositoryDispatch(ORG_NAME, REPO_NAME, TOKEN);
		this.server.verify();
	}

	@Test
	void triggerRepositoryDispatchWhenUnsuccessful() {
		this.server.expect(requestTo(DISPATCH_PATH))
			.andExpect(header("Authorization", "Bearer token"))
			.andExpect(header("Accept", "application/vnd.github+json"))
			.andExpect(content().json("{\"event_type\": \"guides\"}"))
			.andRespond(withStatus(HttpStatus.NOT_FOUND));
		Assertions.assertThatExceptionOfType(RepositoryDispatchFailedException.class)
			.isThrownBy(() -> this.service.triggerRespositoryDispatch(ORG_NAME, REPO_NAME, TOKEN));
		this.server.verify();
	}

}