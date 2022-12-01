/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.spring.guides.renderer.guides.content;

import java.io.File;

import io.spring.guides.renderer.guides.GuideContentModel;

/**
 * Contribute information to the Guide content.
 */
public interface GuideContentContributor {

	/**
	 * Contribute to the guide content by extracting information from the guide
	 * repository.
	 * @param guideContent the guide content to contribute to
	 * @param repositoryRoot the unzipped repository root folder
	 */
	void contribute(GuideContentModel guideContent, File repositoryRoot);

}
