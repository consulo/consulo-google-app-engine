package org.consulo.google.appengine.python.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;

import org.consulo.google.appengine.sdk.GoogleAppEngineSdk;
import org.consulo.lombok.annotations.Logger;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;
import com.intellij.openapi.application.ApplicationManager;

/**
 * @author VISTALL
 * @since 30.09.13.
 */
@Logger
public abstract class GoogleAppEnginePySdk extends GoogleAppEngineSdk
{
	public GoogleAppEnginePySdk(@NonNls String name)
	{
		super(name);
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		File file = new File(s, "VERSION");
		if(!file.exists())
		{
			return null;
		}
		Yaml yaml = new Yaml();
		try
		{
			LinkedHashMap data = (LinkedHashMap) yaml.load(new FileInputStream(file));
			return (String) data.get("release");
		}
		catch(Exception e)
		{
			if(ApplicationManager.getApplication().isInternal())
			{
				LOGGER.info(e);
			}
		}
		return null;
	}
}
