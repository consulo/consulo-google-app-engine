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
