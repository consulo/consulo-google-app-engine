package consulo.google.appengine.java.module.extension;

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
public class JavaAppModuleExtensionProvider implements ModuleExtensionProvider<JavaAppModuleExtension>
{
	@Nonnull
	@Override
	public String getId()
	{
		return "google-app-engine-java";
	}

	@Nullable
	@Override
	public String getParentId()
	{
		return "java-web";
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
	public ModuleExtension<JavaAppModuleExtension> createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new JavaAppModuleExtension(getId(), moduleRootLayer);
	}

	@Nonnull
	@Override
	public MutableModuleExtension<JavaAppModuleExtension> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new JavaAppMutableModuleExtension(getId(), moduleRootLayer);
	}
}
