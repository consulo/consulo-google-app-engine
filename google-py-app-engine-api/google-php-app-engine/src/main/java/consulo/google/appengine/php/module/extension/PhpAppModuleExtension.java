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

package consulo.google.appengine.php.module.extension;

import consulo.content.bundle.SdkType;
import consulo.google.appengine.php.sdk.PhpAppSdkType;
import consulo.google.appengine.python.module.extension.BasePyModuleExtension;
import consulo.module.content.layer.ModuleRootLayer;
import jakarta.annotation.Nonnull;


/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PhpAppModuleExtension extends BasePyModuleExtension<PhpAppModuleExtension>
{
	public PhpAppModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nonnull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return PhpAppSdkType.class;
	}
}
