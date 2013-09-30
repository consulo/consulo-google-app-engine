package org.consulo.google.appengine.py.module.extension;

import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.consulo.module.extension.ui.ModuleExtensionWithSdkPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PyAppMutableModuleExtension extends PyAppModuleExtension implements MutableModuleExtensionWithSdk<PyAppModuleExtension>
{
	private PyAppModuleExtension myPyAppModuleExtension;

	public PyAppMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull PyAppModuleExtension pyAppModuleExtension)
	{
		super(id, module);
		myPyAppModuleExtension = pyAppModuleExtension;
	}

	@Nullable
	@SuppressWarnings("unused")
	public javax.swing.JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable)
	{
		return wrapToNorth(new ModuleExtensionWithSdkPanel(this, runnable));
	}

	@Override
	public boolean isModified()
	{
		return isModifiedImpl(myPyAppModuleExtension);
	}

	@Override
	public void commit()
	{
		myPyAppModuleExtension.commit(this);
	}
}
