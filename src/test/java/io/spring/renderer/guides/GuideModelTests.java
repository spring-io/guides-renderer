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

import java.util.Arrays;
import java.util.Collections;

import io.spring.renderer.github.Repository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link GuideModel}
 */
public class GuideModelTests {

	@Test
	public void nullRepositoryDescription() {
		Repository repository = new Repository(12L, "gs-sample-guide", "spring-guides/gs-sample-guide", null,
				"http://example.org/spring-guides/gs-sample-guide",
				"git://example.org/spring-guides/gs-sample-guide.git",
				"git@example.org:spring-guides/gs-sample-guide.git",
				"https://example.org/spring-guides/gs-sample-guide.git", null);
		GuideMetadata guideMetadata = new GuideMetadata(repository, null, Collections.singleton("test"));
		GuideModel guideModel = new GuideModel(guideMetadata);
		assertThat(guideModel.getName()).isEqualTo("sample-guide");
		assertThat(guideModel.getRepositoryName()).isEqualTo("spring-guides/gs-sample-guide");
		assertThat(guideModel.getTitle()).isEmpty();
		assertThat(guideModel.getDescription()).isEmpty();
		assertThat(guideModel.getType()).isEqualTo(GuideType.GETTING_STARTED);
		assertThat(guideModel.getGithubUrl()).isEqualTo("http://example.org/spring-guides/gs-sample-guide");
		assertThat(guideModel.getCloneUrl()).isEqualTo("https://example.org/spring-guides/gs-sample-guide.git");
		assertThat(guideModel.getGitUrl()).isEqualTo("git://example.org/spring-guides/gs-sample-guide.git");
		assertThat(guideModel.getSshUrl()).isEqualTo("git@example.org:spring-guides/gs-sample-guide.git");
		assertThat(guideModel.getProjects()).isEmpty();
		assertThat(guideModel.getCategory()).containsExactly("test");
	}

	@Test
	public void noGuideProjects() {
		Repository repository = new Repository(12L, "tut-sample-guide", "spring-guides/tut-sample-guide",
				"Title :: Description", "http://example.org/spring-guides/tut-sample-guide",
				"git://example.org/spring-guides/tut-sample-guide.git",
				"git@example.org:spring-guides/tut-sample-guide.git",
				"https://example.org/spring-guides/tut-sample-guide.git", null);
		GuideMetadata guideMetadata = new GuideMetadata(repository, "http://test.academy", Collections.emptySet());
		GuideModel guideModel = new GuideModel(guideMetadata);
		assertThat(guideModel.getName()).isEqualTo("sample-guide");
		assertThat(guideModel.getRepositoryName()).isEqualTo("spring-guides/tut-sample-guide");
		assertThat(guideModel.getTitle()).isEqualTo("Title");
		assertThat(guideModel.getDescription()).isEqualTo("Description");
		assertThat(guideModel.getType()).isEqualTo(GuideType.TUTORIAL);
		assertThat(guideModel.getProjects()).isEmpty();
		assertThat(guideModel.getAcademyUrl()).isEqualTo("http://test.academy");
	}

	@Test
	public void withGuideProjects() {
		Repository repository = new Repository(12L, "top-sample-guide", "spring-guides/top-sample-guide",
				"Title :: Description", "http://example.org/spring-guides/top-sample-guide",
				"git://example.org/spring-guides/top-sample-guide.git",
				"git@example.org:spring-guides/top-sample-guide.git",
				"https://example.org/spring-guides/top-sample-guide.git",
				Arrays.asList("spring-framework", "spring-boot"));
		GuideMetadata guideMetadata = new GuideMetadata(repository, null, Collections.emptySet());
		GuideModel guideModel = new GuideModel(guideMetadata);
		assertThat(guideModel.getName()).isEqualTo("sample-guide");
		assertThat(guideModel.getRepositoryName()).isEqualTo("spring-guides/top-sample-guide");
		assertThat(guideModel.getTitle()).isEqualTo("Title");
		assertThat(guideModel.getDescription()).isEqualTo("Description");
		assertThat(guideModel.getType()).isEqualTo(GuideType.TOPICAL);
		assertThat(guideModel.getProjects()).contains("spring-framework", "spring-boot");
	}

	@Test
	public void deprecatedGuide() {
		Repository repository = new Repository(12L, "deprecated-gs-sample-guide",
				"spring-guides/deprecated-gs-sample-quide", "Title :: Description",
				"http://example.org/spring-guides/deprecated-gs-sample-guide",
				"git://example.org/spring-guides/deprecated-gs-sample-guide.git",
				"git@example.org:spring-guides/deprecated-gs-sample-guide.git",
				"https://example.org/spring-guides/deprecated-gs-sample-guide.git",
				Arrays.asList("spring-framework", "spring-boot"));
		GuideMetadata guideMetadata = new GuideMetadata(repository, null, Collections.emptySet());
		GuideModel guideModel = new GuideModel(guideMetadata);
		assertThat(guideModel.getName()).isEqualTo("deprecated-gs-sample-guide");
		assertThat(guideModel.getTitle()).isEqualTo("Title");
		assertThat(guideModel.getDescription()).isEqualTo("Description");
		assertThat(guideModel.getType()).isEqualTo(GuideType.UNKNOWN);
		assertThat(guideModel.getProjects()).contains("spring-framework", "spring-boot");
	}

}
