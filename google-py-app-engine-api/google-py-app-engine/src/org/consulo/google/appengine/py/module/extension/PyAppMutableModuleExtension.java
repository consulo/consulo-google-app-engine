package org.consulo.google.appengine.py.module.extension;

import java.awt.BorderLayout;

import javax.swing.JPanel;

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
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new ModuleExtensionWithSdkPanel(this, runnable), BorderLayout.NORTH);
		return panel;
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
