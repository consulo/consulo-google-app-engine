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

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.util.ui.JBUI;
import consulo.annotations.RequiredDispatchThread;
import consulo.extension.ui.ModuleExtensionSdkBoxBuilder;
import consulo.module.extension.MutableModuleExtensionWithSdk;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PyAppMutableModuleExtension extends PyAppModuleExtension implements MutableModuleExtensionWithSdk<PyAppModuleExtension>
{
	public PyAppMutableModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@RequiredDispatchThread
	@Override
	@Nullable
	public JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		return JBUI.Panels.verticalPanel().addComponent(ModuleExtensionSdkBoxBuilder.createAndDefine(this,
				runnable).build());
	}

	@Override
	public boolean isModified(@NotNull PyAppModuleExtension extension)
	{
		return isModifiedImpl(extension);
	}
}
