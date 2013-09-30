package org.consulo.google.appengine.go.module.extension;

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
public class GoAppMutableModuleExtension extends GoAppModuleExtension implements MutableModuleExtensionWithSdk<GoAppModuleExtension>
{
	private GoAppModuleExtension myGoAppModuleExtension;

	public GoAppMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull GoAppModuleExtension goAppModuleExtension)
	{
		super(id, module);
		myGoAppModuleExtension = goAppModuleExtension;
	}

	@Nullable
	public javax.swing.JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable)
	{
		return wrapToNorth(FormBuilder.createFormBuilder()
				.addComponent(new ModuleExtensionWithSdkPanel(this, runnable))
				.addComponent(createRuntimeCheckBox()).getPanel());
	}

	@Override
	public boolean isModified()
	{
		return isModifiedImpl(myGoAppModuleExtension);
	}

	@Override
	public void commit()
	{
		myGoAppModuleExtension.commit(this);
	}
}
