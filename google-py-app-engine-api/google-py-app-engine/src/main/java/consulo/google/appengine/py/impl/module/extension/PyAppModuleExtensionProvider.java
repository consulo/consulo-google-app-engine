package consulo.google.appengine.py.impl.module.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.google.appengine.api.icon.GoogleAppEngineIconGroup;
import consulo.google.appengine.localize.GoogleAppEngineLocalize;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.MutableModuleExtension;
import consulo.ui.image.Image;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 30/06/2023
 */
@ExtensionImpl
public class PyAppModuleExtensionProvider implements ModuleExtensionProvider<PyAppModuleExtension>
{
	@Nonnull
	@Override
	public String getId()
	{
		return "google-app-engine-py";
	}

	@Nullable
	@Override
	public String getParentId()
	{
		return "python";
	}

	@Nonnull
	@Override
	public LocalizeValue getName()
	{
		return GoogleAppEngineLocalize.moduleExtensionName();
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return GoogleAppEngineIconGroup.appengine();
	}

	@Nonnull
	@Override
	public ModuleExtension<PyAppModuleExtension> createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new PyAppModuleExtension(getId(), moduleRootLayer);
	}

	@Nonnull
	@Override
	public MutableModuleExtension<PyAppModuleExtension> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new PyAppMutableModuleExtension(getId(), moduleRootLayer);
	}
}
