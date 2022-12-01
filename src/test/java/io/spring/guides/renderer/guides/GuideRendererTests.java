/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.guides;

import java.io.IOException;
import java.util.Collections;

import org.asciidoctor.Asciidoctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.spring.guides.renderer.RendererProperties;
import io.spring.guides.renderer.github.GithubClient;
import io.spring.guides.renderer.guides.content.AsciidoctorGuideContentContributor;

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
