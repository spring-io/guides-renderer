/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.guides;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import io.spring.guides.renderer.github.GithubClient;
import io.spring.guides.renderer.github.GithubResourceNotFoundException;
import io.spring.guides.renderer.github.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.JsonPathExpectationsHelper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link GuidesController}
 */
@WebMvcTest(GuidesController.class)
@WithMockUser
public class GuidesControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private GuideRenderer guideRenderer;

	@MockBean
	private GithubClient githubClient;

	@Test
	public void fetchAllGuides() throws Exception {
		Repository restService = new Repository(12L, "gs-rest-service", "spring-guides/gs-rest-service",
				"REST service sample :: Building a REST service", "http://example.org/spring-guides/gs-rest-service",
				"git://example.org/spring-guides/gs-rest-service.git",
				"git@example.org:spring-guides/gs-rest-service.git",
				"https://example.org/spring-guides/gs-rest-service.git",
				Arrays.asList("spring-boot", "spring-framework"));
		Repository securingWeb = new Repository(15L, "gs-securing-web", "spring-guides/gs-securing-web",
				"Securing Web :: Securing a Web Application", "http://example.org/spring-guides/gs-securing-web",
				"git@example.org:spring-guides/gs-securing-web.git",
				"https://example.org/spring-guides/gs-securing-web.git",
				"git://example.org/spring-guides/gs-securing-web.git", null);

		given(this.githubClient.fetchOrgRepositories("spring-guides"))
				.willReturn(Arrays.asList(restService, securingWeb));

		this.mvc.perform(get("/guides/")).andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.guides[0].name").value("rest-service"))
				.andExpect(jsonPath("$._embedded.guides[0].projects[0]").value("spring-boot"))
				.andExpect(hasLink("$._embedded.guides[0]._links", "self",
						"http://localhost/guides/getting-started/rest-service"))
				.andExpect(jsonPath("$._embedded.guides[1].name").value("securing-web"))
				.andExpect(jsonPath("$._embedded.guides[1].projects").isEmpty())
				.andExpect(hasLink("$._embedded.guides[1]._links", "self",
						"http://localhost/guides/getting-started/securing-web"));
	}

	@Test
	public void fetchAllGuidesFiltersUnknownTypes() throws Exception {
		Repository deprecatedGuide = new Repository(15L, "deprecate-gs-device-detection",
				"spring-guides/deprecate-gs-device-detection",
				"Detecting a Device :: Learn how to use Spring to detect the type of device.",
				"http://example.org/spring-guides/deprecate-gs-device-detection",
				"git://example.org/spring-guides/deprecate-gs-device-detection.git",
				"git@example.org:spring-guides/deprecate-gs-device-detection.git",
				"https://example.org/spring-guides/deprecate-gs-device-detection.git", null);

		given(this.githubClient.fetchOrgRepositories("spring-guides")).willReturn(Arrays.asList(deprecatedGuide));

		this.mvc.perform(get("/guides/")).andExpect(status().isOk()).andExpect(jsonPath("$._embedded").doesNotExist());
	}

	@Test
	public void fetchGuide() throws Exception {
		Repository restService = new Repository(12L, "gs-rest-service", "spring-guides/gs-rest-service",
				"REST service sample :: Building a REST service :: spring-boot,spring-framework",
				"http://example.org/spring-guides/gs-rest-service",
				"git://example.org/spring-guides/gs-rest-service.git",
				"git@example.org:spring-guides/gs-rest-service.git",
				"https://example.org/spring-guides/gs-rest-service.git",
				Arrays.asList("spring-boot", "spring-framework"));
		given(this.githubClient.fetchOrgRepository("spring-guides", "gs-rest-service")).willReturn(restService);

		this.mvc.perform(get("/guides/getting-started/rest-service")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("rest-service"))
				.andExpect(jsonPath("$.repositoryName").value("spring-guides/gs-rest-service"))
				.andExpect(jsonPath("$.title").value("REST service sample"))
				.andExpect(jsonPath("$.description").value("Building a REST service"))
				.andExpect(jsonPath("$.type").value("getting-started"))
				.andExpect(jsonPath("$.githubUrl").value("http://example.org/spring-guides/gs-rest-service"))
				.andExpect(jsonPath("$.gitUrl").value("git://example.org/spring-guides/gs-rest-service.git"))
				.andExpect(jsonPath("$.sshUrl").value("git@example.org:spring-guides/gs-rest-service.git"))
				.andExpect(jsonPath("$.cloneUrl").value("https://example.org/spring-guides/gs-rest-service.git"))
				.andExpect(jsonPath("$.projects[0]").value("spring-boot"))
				.andExpect(hasLink("self", "http://localhost/guides/getting-started/rest-service"));
	}

	@Test
	public void fetchUnknownGuide() throws Exception {
		Repository securingWeb = new Repository(15L, "gs-securing-web", "spring-guides/gs-securing-web",
				"Securing Web :: Securing a Web Application", "http://example.org/spring-guides/gs-securing-web",
				"git://example.org/spring-guides/gs-securing-web.git",
				"git@example.org:spring-guides/gs-securing-web.git",
				"https://example.org/spring-guides/gs-securing-web.git", null);
		given(this.githubClient.fetchOrgRepository("spring-guides", "gs-rest-service"))
				.willThrow(new GithubResourceNotFoundException("spring-guides", "gs-rest-service",
						new HttpClientErrorException(HttpStatus.NOT_FOUND)));

		this.mvc.perform(get("/guides/{guide}", "gs-rest-service"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void fetchGuideContent() throws Exception {
		GuideContentModel content = new GuideContentModel("rest-service", "content", "toc");
		given(this.guideRenderer.render(GuideType.GETTING_STARTED, "rest-service")).willReturn(content);
		this.mvc.perform(get("/guides/getting-started/rest-service/content"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.content").value("content"))
				.andExpect(jsonPath("$.tableOfContents").value("toc"))
				.andExpect(hasLink("self", "http://localhost/guides/getting-started/rest-service/content"))
				.andExpect(hasLink("guide", "http://localhost/guides/getting-started/rest-service"));
	}

	@Test
	public void fetchUnknownGuideContent() throws Exception {
		given(this.guideRenderer.render(GuideType.GETTING_STARTED, "rest-service"))
				.willThrow(new GithubResourceNotFoundException("spring-guides", "gs-securing-web",
						new HttpClientErrorException(HttpStatus.NOT_FOUND)));
		this.mvc.perform(get("/guides/getting-started/rest-service/content"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	static LinksMatcher hasLink(String name, String href) {
		return new LinksMatcher(name, href);
	}

	static LinksMatcher hasLink(String prefix, String name, String href) {
		return new LinksMatcher(prefix, name, href);
	}

	static class LinksMatcher implements ResultMatcher {

		private String prefix;

		private String linkName;

		private String href;

		public LinksMatcher(String linkName, String href) {
			this.linkName = linkName;
			this.href = href;
		}

		public LinksMatcher(String prefix, String linkName, String href) {
			this.prefix = prefix;
			this.linkName = linkName;
			this.href = href;
		}

		@Override
		public void match(MvcResult result) throws Exception {
			Assert.hasText(this.linkName, "The link should have a name");
			String content = result.getResponse().getContentAsString();
			if (StringUtils.isEmpty(this.prefix)) {
				this.prefix = "$._links";
			}
			String hrefExpr = this.prefix + "." + this.linkName + ".href";
			JsonPathExpectationsHelper hrefHelper = new JsonPathExpectationsHelper(hrefExpr);
			hrefHelper.assertValue(content, this.href);
		}

	}

}
