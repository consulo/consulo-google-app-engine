package org.consulo.google.appengine.go.module.extension;

import org.consulo.google.appengine.go.sdk.GoAppSdkType;
import org.consulo.google.appengine.python.module.extension.BasePyModuleExtension;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class GoAppModuleExtension extends BasePyModuleExtension<GoAppModuleExtension>
{
	public GoAppModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@Override
	protected Class<? extends SdkType> getSdkTypeClass()
	{
		return GoAppSdkType.class;
	}
}
