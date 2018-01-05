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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.util.ui.JBUI;
import consulo.annotations.RequiredDispatchThread;
import consulo.extension.ui.ModuleExtensionSdkBoxBuilder;
import consulo.module.extension.MutableModuleExtensionWithSdk;
import consulo.roots.ModuleRootLayer;
import consulo.util.ui.components.VerticalLayoutPanel;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PhpAppMutableModuleExtension extends PhpAppModuleExtension implements MutableModuleExtensionWithSdk<PhpAppModuleExtension>
{
	public PhpAppMutableModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@RequiredDispatchThread
	@Override
	@Nullable
	public javax.swing.JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		VerticalLayoutPanel verticalLayoutPanel = JBUI.Panels.verticalPanel();
		verticalLayoutPanel.addComponent(ModuleExtensionSdkBoxBuilder.createAndDefine(this, runnable).build());
		verticalLayoutPanel.addComponent(createRuntimeCheckBox());
		return verticalLayoutPanel;
	}

	@Override
	public boolean isModified(@NotNull PhpAppModuleExtension extension)
	{
		return isModifiedImpl(extension);
	}
}