package io.spring.renderer.guides;

import java.util.Arrays;
import java.util.Base64;

import io.spring.renderer.github.GithubClient;
import io.spring.renderer.github.Repository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for ensuring that responses from {@link GuidesController} are cached.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CachingTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private GuideRenderer guideRenderer;

	@MockBean
	private GithubClient githubClient;

	private static final String BASIC_AUTH = "basic " + Base64.getEncoder().encodeToString("user:test".getBytes());

	@Test
	void fetchAllGuidesIsCached() {
		Repository restService = new Repository(12L, "gs-rest-service-test", "spring-guides/gs-rest-service-test",
				"REST service sample :: Building a REST service",
				"http://example.org/spring-guides/gs-rest-service-test",
				"git://example.org/spring-guides/gs-rest-service-test.git",
				"git@example.org:spring-guides/gs-rest-service-test.git",
				"https://example.org/spring-guides/gs-rest-service-test.git",
				Arrays.asList("spring-boot", "spring-framework"));
		Repository securingWeb = new Repository(15L, "gs-securing-web-test", "spring-guides/gs-securing-web-test",
				"Securing Web :: Securing a Web Application", "http://example.org/spring-guides/gs-securing-web-test",
				"git@example.org:spring-guides/gs-securing-web-test.git",
				"https://example.org/spring-guides/gs-securing-web-test.git",
				"git://example.org/spring-guides/gs-securing-web-test.git", null);
		given(this.githubClient.fetchOrgRepositories("spring-guides"))
			.willReturn(Arrays.asList(restService, securingWeb));
		RequestEntity<Void> entity = RequestEntity.get("/guides").header("Authorization", BASIC_AUTH).build();
		ResponseEntity<Object> exchange = this.restTemplate.exchange(entity, Object.class);
		assertThat(exchange.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		exchange = this.restTemplate.exchange(entity, Object.class);
		assertThat(exchange.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		verify(this.githubClient, times(1)).fetchOrgRepositories("spring-guides");

	}

	@Test
	void fetchGuideIsCached() {
		Repository restService = new Repository(12L, "gs-rest-service-test", "spring-guides/gs-rest-service-test",
				"REST service sample :: Building a REST service :: spring-boot,spring-framework",
				"http://example.org/spring-guides/gs-rest-service-test",
				"git://example.org/spring-guides/gs-rest-service-test.git",
				"git@example.org:spring-guides/gs-rest-service-test.git",
				"https://example.org/spring-guides/gs-rest-service-test.git",
				Arrays.asList("spring-boot", "spring-framework"));
		given(this.githubClient.fetchOrgRepository("spring-guides", "gs-rest-service-test")).willReturn(restService);
		RequestEntity<Void> entity = RequestEntity.get("/guides/getting-started/rest-service-test")
			.header("Authorization", BASIC_AUTH)
			.build();
		ResponseEntity<Object> exchange = this.restTemplate.exchange(entity, Object.class);
		assertThat(exchange.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		exchange = this.restTemplate.exchange(entity, Object.class);
		assertThat(exchange.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		verify(this.githubClient, times(1)).fetchOrgRepository("spring-guides", "gs-rest-service-test");
	}

	@Test
	void fetchGuideContentIsCached() {
		GuideContentModel content = new GuideContentModel("rest-service", "content", "toc");
		given(this.guideRenderer.render(GuideType.GETTING_STARTED, "rest-service")).willReturn(content);
		RequestEntity<Void> entity = RequestEntity.get("/guides/getting-started/rest-service/content")
			.header("Authorization", BASIC_AUTH)
			.build();
		ResponseEntity<Object> exchange = this.restTemplate.exchange(entity, Object.class);
		assertThat(exchange.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		exchange = this.restTemplate.exchange(entity, Object.class);
		assertThat(exchange.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		verify(this.guideRenderer, times(1)).render(GuideType.GETTING_STARTED, "rest-service");
	}

}
