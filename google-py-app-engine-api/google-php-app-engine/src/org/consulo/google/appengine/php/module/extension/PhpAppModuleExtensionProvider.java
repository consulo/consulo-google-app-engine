package org.consulo.google.appengine.php.module.extension;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PhpAppModuleExtensionProvider extends GoogleAppEngineModuleExtensionProvider<PhpAppModuleExtension, PhpAppMutableModuleExtension>
{
	@NotNull
	@Override
	public Class<PhpAppModuleExtension> getImmutableClass()
	{
		return PhpAppModuleExtension.class;
	}

	@NotNull
	@Override
	public PhpAppModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new PhpAppModuleExtension(s, module);
	}

	@NotNull
	@Override
	public PhpAppMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull PhpAppModuleExtension phpAppModuleExtension)
	{
		return new PhpAppMutableModuleExtension(s, module, phpAppModuleExtension);
	}
}
