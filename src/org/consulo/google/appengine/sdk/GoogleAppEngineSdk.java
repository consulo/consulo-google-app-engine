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

package org.consulo.google.appengine.sdk;

import java.io.File;

import javax.swing.Icon;

import org.consulo.google.appengine.GoogleAppEngineIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.OrderRootType;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public abstract class GoogleAppEngineSdk extends SdkType
{
	public GoogleAppEngineSdk(@NonNls String name)
	{
		super(name);
	}

	@NotNull
	public abstract String getLanguageName();

	@NotNull
	public abstract SdkType getLanguageSdkType();

	@NotNull
	@Override
	public String getPresentableName()
	{
		return String.format("Google App Engine (%s)", getLanguageName());
	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return getLanguageSdkType().isRootTypeApplicable(type);
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return GoogleAppEngineIcons.AppEngine;
	}

	@Nullable
	@Override
	public abstract String getVersionString(String s);

	@Override
	public boolean isValidSdkHome(String path)
	{
		return getVersionString(path) != null;
	}

	@Override
	public String suggestSdkName(String currentSdkName, String sdkHome)
	{
		File file = new File(sdkHome);
		return file.getName();
	}
}
