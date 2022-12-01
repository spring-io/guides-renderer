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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RateLimitHealthIndicatorTests {

	@Mock
	private GithubClient client;

	private RateLimitHealthIndicator healthIndicator;

	@BeforeEach
	public void setup() {
		this.healthIndicator = new RateLimitHealthIndicator(this.client);
	}

	@Test
	void rateLimitUp() {
		given(this.client.fetchRateLimitInfo()).willReturn(createRateLimit(10));
		Health result = this.healthIndicator.getHealth(true);
		assertThat(result.getStatus()).isEqualTo(Status.UP);
	}

	@Test
	void rateLimitOutOfService() {
		given(this.client.fetchRateLimitInfo()).willReturn(createRateLimit(0));
		Health result = this.healthIndicator.getHealth(true);
		assertThat(result.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
	}

	RateLimit createRateLimit(Integer remaining) {
		Map<String, String> map = new HashMap<>();
		map.put("limit", "60");
		map.put("used", "10");
		map.put("remaining", remaining.toString());
		map.put("reset", Long.toString(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()));
		return new RateLimit(map);
	}

}
