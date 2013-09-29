package org.consulo.google.appengine.php.module.extension;

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
public class PhpAppMutableModuleExtension extends PhpAppModuleExtension implements MutableModuleExtensionWithSdk<PhpAppModuleExtension>
{
	private PhpAppModuleExtension myPhpAppModuleExtension;

	public PhpAppMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull PhpAppModuleExtension phpAppModuleExtension)
	{
		super(id, module);
		myPhpAppModuleExtension = phpAppModuleExtension;
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
		return isModifiedImpl(myPhpAppModuleExtension);
	}

	@Override
	public void commit()
	{
		myPhpAppModuleExtension.commit(this);
	}
}
