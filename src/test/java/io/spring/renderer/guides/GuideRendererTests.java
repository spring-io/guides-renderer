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

package io.spring.renderer.guides;

import java.io.IOException;
import java.util.Collections;

import org.asciidoctor.Asciidoctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.spring.renderer.RendererProperties;
import io.spring.renderer.github.GithubClient;
import io.spring.renderer.guides.content.AsciidoctorGuideContentContributor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class GuideRendererTests {

	private GuideRenderer renderer;

	private GithubClient githubClient;

	private RendererProperties properties;

	@BeforeEach
	public void setup() {
		this.properties = new RendererProperties();
		this.githubClient = mock(GithubClient.class);
		this.renderer = new GuideRenderer(this.githubClient, this.properties,
				Collections.singletonList(new AsciidoctorGuideContentContributor(Asciidoctor.Factory.create())));
	}

	@Test
	public void renderAsciidoctorContent() throws Exception {
		given(this.githubClient.downloadRepositoryAsZipball("spring-guides", "gs-sample"))
				.willReturn(readAsBytes("gs-sample.zip"));
		GuideContentModel result = this.renderer.render(GuideType.GETTING_STARTED, "sample");
		assertThat(result.getName()).isEqualTo("sample");
		assertThat(result.getContent()).contains("<p>This is a sample guide.</p>")
				.contains("<!-- rendered by Sagan Renderer Service -->");
		assertThat(result.getTableOfContents())
				.contains("<li><a href=\"#_sample_guide_title\">Sample Guide title</a></li>");
	}

	private byte[] readAsBytes(String path) throws IOException {
		ClassPathResource resource = new ClassPathResource(path, getClass());
		return StreamUtils.copyToByteArray(resource.getInputStream());
	}

}
