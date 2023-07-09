package consulo.google.appengine.go.module.extension;

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
public class GoAppModuleExtensionProvider implements ModuleExtensionProvider<GoAppModuleExtension>
{
	@Nonnull
	@Override
	public String getId()
	{
		return "google-app-engine-go";
	}

	@Nullable
	@Override
	public String getParentId()
	{
		return "google-go";
	}

	@Nonnull
	@Override
	public LocalizeValue getName()
	{
		return GoogleAppEngineLocalize.name();
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return GoogleAppEngineIconGroup.appengine();
	}

	@Nonnull
	@Override
	public ModuleExtension<GoAppModuleExtension> createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new GoAppModuleExtension(getId(), moduleRootLayer);
	}

	@Nonnull
	@Override
	public MutableModuleExtension<GoAppModuleExtension> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new GoAppMutableModuleExtension(getId(), moduleRootLayer);
	}
}
