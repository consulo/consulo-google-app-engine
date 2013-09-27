package org.consulo.google.appengine.java.module.extension;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppModuleExtensionProvider extends GoogleAppEngineModuleExtensionProvider<JavaAppModuleExtension, JavaAppMutableModuleExtension>
{
	@NotNull
	@Override
	public Class<JavaAppModuleExtension> getImmutableClass()
	{
		return JavaAppModuleExtension.class;
	}

	@NotNull
	@Override
	public JavaAppModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new JavaAppModuleExtension(s, module);
	}

	@NotNull
	@Override
	public JavaAppMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull JavaAppModuleExtension javaAppModuleExtension)
	{
		return new JavaAppMutableModuleExtension(s, module, javaAppModuleExtension);
	}
}
