package org.consulo.google.appengine.php.module.extension;

import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PhpAppMutableModuleExtension extends PhpAppModuleExtension implements MutableModuleExtensionWithSdk<PhpAppModuleExtension>
{
	private PhpAppModuleExtension myPhpAppModuleExtension;

	public PhpAppMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull PhpAppModuleExtension phpAppModuleExtension)
	{
		super(id, module);
		myPhpAppModuleExtension = phpAppModuleExtension;
	}

	@Override
	public boolean isModified()
	{
		return isModifiedImpl(myPhpAppModuleExtension);
	}

	@Override
	public void commit()
	{
		myPhpAppModuleExtension.commit(this);
	}
}
