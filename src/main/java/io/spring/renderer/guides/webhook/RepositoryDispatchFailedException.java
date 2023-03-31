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

/**
 * Exception raised when the request to trigger a repository dispatch event returns
 * anything other than an HTTP 2xx status.
 */
class RepositoryDispatchFailedException extends RuntimeException {

	RepositoryDispatchFailedException(int statusCode) {
		super(String.format("Repository Dispatch failed with status code: '%d'", statusCode));
	}

}
