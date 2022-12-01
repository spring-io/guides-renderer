/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.github;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class RateLimitHealthIndicator extends AbstractHealthIndicator {

	private final GithubClient githubClient;

	public RateLimitHealthIndicator(GithubClient githubClient) {
		this.githubClient = githubClient;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		RateLimit rateLimitInfo = this.githubClient.fetchRateLimitInfo();
		builder = builder.withDetails(rateLimitInfo.asMap());
		if (rateLimitInfo.getRemaining() > 0) {
			builder.up();
		}
		else {
			builder.outOfService();
		}
	}

}
