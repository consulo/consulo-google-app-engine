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

package org.consulo.google.appengine.py.module.extension;

import org.consulo.google.appengine.py.sdk.PyAppSdkType;
import org.consulo.google.appengine.python.module.extension.AbstractPyModuleExtension;
import org.consulo.python.module.extension.PyModuleExtension;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PyAppModuleExtension extends AbstractPyModuleExtension<PyAppModuleExtension>
{
	public PyAppModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Override
	public Sdk getRuntimeSdk()
	{
		return ModuleUtilCore.getSdk(getModule(), PyModuleExtension.class);
	}

	@NotNull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return PyAppSdkType.class;
	}
}
