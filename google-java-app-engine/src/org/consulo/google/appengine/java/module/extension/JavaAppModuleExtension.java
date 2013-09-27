package org.consulo.google.appengine.java.module.extension;

import org.consulo.google.appengine.java.sdk.JavaAppSdkType;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppModuleExtension extends ModuleExtensionWithSdkImpl<JavaAppModuleExtension>
{
	public JavaAppModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@Override
	protected Class<? extends SdkType> getSdkTypeClass()
	{
		return JavaAppSdkType.class;
	}
}
