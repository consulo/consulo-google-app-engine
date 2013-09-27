package org.consulo.google.appengine.py.module.extension;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PyAppModuleExtensionProvider extends GoogleAppEngineModuleExtensionProvider<PyAppModuleExtension, PyAppMutableModuleExtension>
{
	@NotNull
	@Override
	public Class<PyAppModuleExtension> getImmutableClass()
	{
		return PyAppModuleExtension.class;
	}

	@NotNull
	@Override
	public PyAppModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new PyAppModuleExtension(s, module);
	}

	@NotNull
	@Override
	public PyAppMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull PyAppModuleExtension pyAppModuleExtension)
	{
		return new PyAppMutableModuleExtension(s, module, pyAppModuleExtension);
	}
}
