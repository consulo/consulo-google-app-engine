package org.consulo.google.appengine.php.module.extension;

import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.consulo.module.extension.ui.ModuleExtensionWithSdkPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.util.ui.FormBuilder;

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
	public javax.swing.JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable)
	{
		return FormBuilder.createFormBuilder()
				.addComponent(new ModuleExtensionWithSdkPanel(this, runnable))
				.addComponent(createRuntimeCheckBox()).setVertical(true).getPanel();
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
