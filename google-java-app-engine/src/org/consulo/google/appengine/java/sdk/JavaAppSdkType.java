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

package org.consulo.google.appengine.java.sdk;

import java.io.File;

import org.consulo.google.appengine.sdk.GoogleAppEngineSdk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.io.FileUtil;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppSdkType extends GoogleAppEngineSdk
{
	public JavaAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_JAVA");
	}

	public static File getWebSchemeFile(@NotNull Sdk sdk)
	{
		return getWebSchemeFile(sdk.getHomePath());
	}

	public static File getWebSchemeFile(@NotNull String home)
	{
		return new File(FileUtil.toSystemDependentName(home + "/docs/appengine-web.xsd"));
	}

	@NotNull
	@Override
	public String getLanguageName()
	{
		return "Java";
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		return "1";
	}

	@Override
	public boolean isValidSdkHome(String s)
	{
		return true;
	}
}
