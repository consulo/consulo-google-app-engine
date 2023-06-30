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

package consulo.google.appengine.py.impl.sdk;

import com.jetbrains.python.impl.sdk.PythonSdkType;
import consulo.annotation.component.ExtensionImpl;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModificator;
import consulo.content.bundle.SdkType;
import consulo.google.appengine.python.sdk.GoogleAppEnginePySdkType;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
@ExtensionImpl
public class PyAppSdkType extends GoogleAppEnginePySdkType
{
	public PyAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_PYTHON");
	}

	@Override
	public void setupSdkPaths(Sdk sdk)
	{
		VirtualFile homeDirectory = sdk.getHomeDirectory();
		if(homeDirectory == null)
		{
			return;
		}

		SdkModificator sdkModificator = sdk.getSdkModificator();

		VirtualFile lib = homeDirectory.findChild("lib");
		if(lib == null)
		{
			return;
		}

		for(VirtualFile virtualFile : lib.getChildren())
		{
			if(virtualFile.exists())
			{
				sdkModificator.addRoot(virtualFile, BinariesOrderRootType.getInstance());
			}
		}

		sdkModificator.commitChanges();
	}

	@Nonnull
	@Override
	public String getLanguageName()
	{
		return "Python";
	}

	@Nonnull
	@Override
	public SdkType getLanguageSdkType()
	{
		return PythonSdkType.getInstance();
	}
}
