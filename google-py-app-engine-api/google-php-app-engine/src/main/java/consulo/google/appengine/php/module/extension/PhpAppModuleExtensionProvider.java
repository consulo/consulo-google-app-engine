package consulo.google.appengine.php.module.extension;

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
public class PhpAppModuleExtensionProvider implements ModuleExtensionProvider<PhpAppModuleExtension>
{
	@Nonnull
	@Override
	public String getId()
	{
		return "google-app-engine-php";
	}

	@Nullable
	@Override
	public String getParentId()
	{
		return "php";
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
	public ModuleExtension<PhpAppModuleExtension> createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new PhpAppModuleExtension(getId(), moduleRootLayer);
	}

	@Nonnull
	@Override
	public MutableModuleExtension<PhpAppModuleExtension> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new PhpAppMutableModuleExtension(getId(), moduleRootLayer);
	}
}
