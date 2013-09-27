package org.consulo.google.appengine.py.module.extension;

import org.consulo.google.appengine.py.sdk.PyAppSdkType;
import org.consulo.google.appengine.python.module.extension.AbstractPyModuleExtension;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PyAppModuleExtension extends AbstractPyModuleExtension<PyAppModuleExtension>
{
	public PyAppModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@Override
	protected Class<? extends SdkType> getSdkTypeClass()
	{
		return PyAppSdkType.class;
	}
}
