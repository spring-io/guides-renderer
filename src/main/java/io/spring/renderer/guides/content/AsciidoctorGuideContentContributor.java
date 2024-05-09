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

package io.spring.renderer.guides.content;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import io.spring.renderer.guides.GuideContentModel;
import io.spring.renderer.guides.GuideRenderingException;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import org.springframework.stereotype.Component;

/**
 * Render the README.adoc file with Asciidoctor and contribute the guide content and its
 * table of contents.
 */
@Component
public class AsciidoctorGuideContentContributor implements GuideContentContributor {

	private static final String README_FILENAME = "README.adoc";

	private final Asciidoctor asciidoctor;

	public AsciidoctorGuideContentContributor(Asciidoctor asciidoctor) {
		this.asciidoctor = asciidoctor;
	}

	@Override
	public void contribute(GuideContentModel guideContent, File repositoryRoot) {
		try {
			Attributes attributes = Attributes.builder().allowUriRead(true).skipFrontMatter(true).build();
			File readmeAdocFile = new File(repositoryRoot.getAbsolutePath() + File.separator + README_FILENAME);
			Options options = Options.builder()
				.safe(SafeMode.SAFE)
				.baseDir(repositoryRoot)
				.headerFooter(true)
				.attributes(attributes)
				.build();
			StringWriter writer = new StringWriter();
			this.asciidoctor.convert(new FileReader(readmeAdocFile), writer, options);
			Document doc = Jsoup.parse(writer.toString());
			guideContent.setContent(doc.select("#content").html() + "\n<!-- rendered by Sagan Renderer Service -->");
			guideContent.setTableOfContents(findTableOfContents(doc));
		}
		catch (IOException e) {
			throw new GuideRenderingException(guideContent.getName(), e);
		}
	}

	/**
	 * Extract top level table-of-content entries, and discard lower level links
	 * @param doc the rendered HTML guide
	 * @return HTML of the top tier table of content entries
	 */
	private String findTableOfContents(Document doc) {
		Elements toc = doc.select("div#toc > ul.sectlevel1");
		toc.select("ul.sectlevel2").forEach(Node::remove);
		toc.forEach(part -> part.select("a[href]")
			.stream()
			.filter(anchor -> doc.select(anchor.attr("href"))
				.get(0)
				.parent()
				.classNames()
				.stream()
				.anyMatch(clazz -> clazz.startsWith("reveal")))
			.forEach(href -> href.parent().remove()));
		return toc.toString();
	}

}
