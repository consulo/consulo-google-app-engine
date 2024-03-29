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

package consulo.google.appengine.sdk;

import consulo.content.OrderRootType;
import consulo.content.bundle.SdkType;
import consulo.google.appengine.api.icon.GoogleAppEngineIconGroup;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public abstract class GoogleAppEngineSdkType extends SdkType
{
	public GoogleAppEngineSdkType(@NonNls String name)
	{
		super(name);
	}

	@Nonnull
	public abstract String getLanguageName();

	@Nonnull
	public abstract SdkType getLanguageSdkType();

	@Nonnull
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
	public Image getIcon()
	{
		return GoogleAppEngineIconGroup.appengine();
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
