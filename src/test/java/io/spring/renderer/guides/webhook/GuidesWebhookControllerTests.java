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

import java.nio.charset.StandardCharsets;

import io.spring.renderer.SecurityConfiguration;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StreamUtils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link GuidesWebhookController}.
 */
@WebMvcTest(value = GuidesWebhookController.class,
		properties = { "renderer.github.webhook.secret=token", "renderer.github.webhook.action-org=test-org",
				"renderer.github.webhook.action-repo=test-repo",
				"renderer.github.webhook.dispatch-token=dispatch-token" })
@Import(SecurityConfiguration.class)
class GuidesWebhookControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GithubActionsService service;

	@Test
	void missingHeadersShouldBeRejected() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/webhook/guides").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content("{\"message\": \"this is a test\""))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void invalidHmacSignatureShouldBeRejected() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/webhook/guides").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("X-Hub-Signature", "sha1=wronghmacvalue")
				.header("X-GitHub-Event", "push").content("{\"message\": \"this is a test\""))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.content().string("{ \"message\": \"Forbidden\" }"));
	}

	@Test
	void pingEventShouldHaveResponse() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/webhook/guides").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Hub-Signature", "sha1=9BBB4C351EF0D50F93372CA787F338385981AA41")
				.header("X-GitHub-Event", "ping").content(getTestPayload("ping")))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content()
						.string("{ \"message\": \"Successfully processed ping event\" }"));
	}

	@Test
	void invalidJsonPushEventShouldBeRejected() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/webhook/guides").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Hub-Signature", "sha1=8FCA101BFF427372C4DB6B9B6E48C8E2D2092ADC")
				.header("X-GitHub-Event", "push").content("this is a test message"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("{ \"message\": \"Bad Request\" }"));
	}

	@Test
	void shouldTriggerRepositoryDispatch() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/webhook/guides").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Hub-Signature", "sha1=C8D5B1C972E8DCFB69AB7124678D4C91E11D6F23")
				.header("X-GitHub-Event", "push").content(getTestPayload("push")))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(
						MockMvcResultMatchers.content().string("{ \"message\": \"Successfully processed update\" }"));
		verify(this.service, times(1)).triggerRespositoryDispatch("test-org", "test-repo", "dispatch-token");
	}

	private String getTestPayload(String fileName) throws Exception {
		ClassPathResource resource = new ClassPathResource(fileName + ".json", getClass());
		return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8).replaceAll("[\\n|\\r]", "");
	}

}