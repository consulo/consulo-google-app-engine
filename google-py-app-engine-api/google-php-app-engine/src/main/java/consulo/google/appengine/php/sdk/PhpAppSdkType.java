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

package consulo.google.appengine.php.sdk;

import javax.annotation.Nonnull;

import consulo.google.appengine.python.sdk.GoogleAppEnginePySdkType;
import com.intellij.openapi.projectRoots.SdkType;
import consulo.php.sdk.PhpSdkType;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PhpAppSdkType extends GoogleAppEnginePySdkType
{
	public PhpAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_PHP");
	}

	@Nonnull
	@Override
	public String getLanguageName()
	{
		return "PHP";
	}

	@Nonnull
	@Override
	public SdkType getLanguageSdkType()
	{
		return PhpSdkType.getInstance();
	}
}
