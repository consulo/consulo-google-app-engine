/*
 * Copyright 2013-2016 consulo.io
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

package consulo.google.appengine.python.sdk;

import consulo.google.appengine.sdk.GoogleAppEngineSdkType;
import consulo.logging.Logger;
import org.jetbrains.annotations.NonNls;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;

/**
 * @author VISTALL
 * @since 30.09.13.
 */
public abstract class GoogleAppEnginePySdkType extends GoogleAppEngineSdkType
{
	private static final Logger LOGGER = Logger.getInstance(GoogleAppEnginePySdkType.class);

	public GoogleAppEnginePySdkType(@NonNls String name)
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
			LOGGER.warn(e);
		}
		return null;
	}
}
