/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.consulo.google.appengine.python;

import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

/**
 * @author VISTALL
 * @since 30.09.13.
 */
public class VersionTest extends Assert
{
	@Test
	public void testVersion()
	{
		Yaml yaml = new Yaml();

		LinkedHashMap versionFileStub = (LinkedHashMap) yaml.load("release: \"1.8.5\"\n" +
				"timestamp: 1378244055\n" +
				"api_versions: ['1']\n" +
				"supported_api_versions:\n" +
				"  python:\n" +
				"    api_versions: ['1']\n" +
				"  python27:\n" +
				"    api_versions: ['1']\n" +
				"  go:\n" +
				"    api_versions: ['go1']\n");

		Object release = versionFileStub.get("release");
		assertEquals(release, "1.8.5");
	}
}
