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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.renderer.RendererProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that handles requests from GitHub webhook set up at
 * <a href="https://github.com/spring-guides/">the org level </a> and triggers the Github
 * Action to update the website. Github requests are signed with a shared secret, using an
 * HMAC sha-1 algorithm.
 */
@RestController
@RequestMapping("/webhook/")
class GuidesWebhookController {

	private static final Log logger = LogFactory.getLog(GuidesWebhookController.class);

	private static final Charset CHARSET = StandardCharsets.UTF_8;

	private static final String HMAC_ALGORITHM = "HmacSHA1";

	private static final String PING_EVENT = "ping";

	private final ObjectMapper objectMapper;

	private final Mac hmac;

	private final GithubActionsService service;

	private final RendererProperties properties;

	@Autowired
	public GuidesWebhookController(ObjectMapper objectMapper, RendererProperties properties,
			GithubActionsService service) throws NoSuchAlgorithmException, InvalidKeyException {
		this.objectMapper = objectMapper;
		this.service = service;
		// initialize HMAC with SHA1 algorithm and secret
		SecretKeySpec secret = new SecretKeySpec(properties.getGithub().getWebhook().getSecret().getBytes(CHARSET),
				HMAC_ALGORITHM);
		this.hmac = Mac.getInstance(HMAC_ALGORITHM);
		this.hmac.init(secret);
		this.properties = properties;
	}

	@PostMapping(path = "guides", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> processGuidesUpdate(@RequestBody String payload,
			@RequestHeader("X-Hub-Signature") String signature,
			@RequestHeader(name = "X-GitHub-Event", required = false, defaultValue = "push") String event)
			throws IOException {
		verifyHmacSignature(payload, signature);
		if (PING_EVENT.equals(event)) {
			return ResponseEntity.ok("{ \"message\": \"Successfully processed ping event\" }");
		}
		Map<?, ?> push = this.objectMapper.readValue(payload, Map.class);
		logPayload(push);
		RendererProperties.Webhook webhook = this.properties.getGithub().getWebhook();
		this.service.triggerRespositoryDispatch(webhook.getActionOrg(), webhook.getActionRepo(),
				webhook.getDispatchToken());
		return ResponseEntity.ok("{ \"message\": \"Successfully processed update\" }");
	}

	@ExceptionHandler(WebhookAuthenticationException.class)
	public ResponseEntity<String> handleWebhookAuthenticationFailure(WebhookAuthenticationException exception) {
		logger.error("Webhook authentication failure: " + exception.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{ \"message\": \"Forbidden\" }");
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handlePayloadParsingException(IOException exception) {
		logger.error("Payload parsing exception", exception);
		return ResponseEntity.badRequest().body("{ \"message\": \"Bad Request\" }");
	}

	private void verifyHmacSignature(String message, String signature) {
		byte[] sig = hmac.doFinal(message.getBytes(CHARSET));
		String computedSignature = "sha1=" + DatatypeConverter.printHexBinary(sig);
		if (!computedSignature.equalsIgnoreCase(signature)) {
			throw new WebhookAuthenticationException(computedSignature, signature);
		}
	}

	private void logPayload(Map<?, ?> push) {
		if (push.containsKey("head_commit")) {
			final Object headCommit = push.get("head_commit");
			if (headCommit != null) {
				final Map<?, ?> headCommitMap = (Map<?, ?>) headCommit;
				logger.info("Received new webhook payload for push with head_commit message: "
						+ headCommitMap.get("message"));
			}
		}
		else {
			logger.info("Received new webhook payload for push, but with no head_commit");
		}
	}

}
