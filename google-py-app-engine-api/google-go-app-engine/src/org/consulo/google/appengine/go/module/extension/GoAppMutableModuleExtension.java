package org.consulo.google.appengine.go.module.extension;

import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

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
