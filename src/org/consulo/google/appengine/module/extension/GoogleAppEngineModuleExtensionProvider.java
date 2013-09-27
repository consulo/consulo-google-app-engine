package org.consulo.google.appengine.module.extension;

import javax.swing.Icon;

import org.consulo.google.appengine.GoogleAppEngineIcons;
import org.consulo.module.extension.ModuleExtension;
import org.consulo.module.extension.ModuleExtensionProvider;
import org.consulo.module.extension.MutableModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public abstract class GoogleAppEngineModuleExtensionProvider
		<ImmutableModel extends ModuleExtension, MutableModel extends MutableModuleExtension>
		implements ModuleExtensionProvider<ImmutableModel, MutableModel>
{
	@Nullable
	@Override
	public Icon getIcon()
	{
		return GoogleAppEngineIcons.AppEngine;
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Google App Engine";
	}
}
