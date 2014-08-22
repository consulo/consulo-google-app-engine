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

package org.consulo.google.appengine.go.module.extension;

import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.consulo.module.extension.ui.ModuleExtensionWithSdkPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.roots.ModuleRootLayer;
import com.intellij.util.ui.FormBuilder;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class GoAppMutableModuleExtension extends GoAppModuleExtension implements MutableModuleExtensionWithSdk<GoAppModuleExtension>
{
	public GoAppMutableModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Override
	@Nullable
	public javax.swing.JComponent createConfigurablePanel( @Nullable Runnable runnable)
	{
		return wrapToNorth(FormBuilder.createFormBuilder()
				.addComponent(new ModuleExtensionWithSdkPanel(this, runnable))
				.addComponent(createRuntimeCheckBox()).getPanel());
	}

	@Override
	public boolean isModified(@NotNull GoAppModuleExtension extension)
	{
		return isModifiedImpl(extension);
	}
}
