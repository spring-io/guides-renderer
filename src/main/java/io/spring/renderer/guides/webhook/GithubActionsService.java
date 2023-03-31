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

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service to connect to Github Actions.
 *
 * @author Madhura Bhave
 */
@Service
class GithubActionsService {

	private final RestTemplate restTemplate;

	private static final String ACCEPT_HEADER = "application/vnd.github+json";

	private static final String DISPATCH_PATH_TEMPLATE = "https://api.github.com/repos/{org}/{repo}/dispatches";

	GithubActionsService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	void triggerRespositoryDispatch(String org, String repo, String token) {
		RequestEntity<String> entity = RequestEntity.post(DISPATCH_PATH_TEMPLATE, org, repo)
				.header("Authorization", "Bearer " + token).header("Accept", ACCEPT_HEADER)
				.body("{\"event_type\": \"guides\"}");
		try {
			this.restTemplate.exchange(entity, Void.class);
		}
		catch (HttpClientErrorException ex) {
			throw new RepositoryDispatchFailedException(ex.getRawStatusCode());
		}

	}

}
