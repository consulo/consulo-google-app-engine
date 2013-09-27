package org.consulo.google.appengine.py.module.extension;

import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

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
