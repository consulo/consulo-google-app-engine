package org.consulo.google.appengine.go.module.extension;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class GoAppModuleExtensionProvider extends GoogleAppEngineModuleExtensionProvider<GoAppModuleExtension, GoAppMutableModuleExtension>
{
	@NotNull
	@Override
	public Class<GoAppModuleExtension> getImmutableClass()
	{
		return GoAppModuleExtension.class;
	}

	@NotNull
	@Override
	public GoAppModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new GoAppModuleExtension(s, module);
	}

	@NotNull
	@Override
	public GoAppMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull GoAppModuleExtension goAppModuleExtension)
	{
		return new GoAppMutableModuleExtension(s, module, goAppModuleExtension);
	}
}
