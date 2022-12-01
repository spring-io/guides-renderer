/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.guides;

import org.springframework.hateoas.RepresentationModel;

/**
 * Spring guide content holder.
 */
public class GuideContentModel extends RepresentationModel {

	private String name;

	private String tableOfContents;

	private String content;

	GuideContentModel(String name, String content, String tableOfContents) {
		this.name = name;
		this.content = content;
		this.tableOfContents = tableOfContents;
	}

	GuideContentModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableOfContents() {
		return this.tableOfContents;
	}

	public void setTableOfContents(String tableOfContents) {
		this.tableOfContents = tableOfContents;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}