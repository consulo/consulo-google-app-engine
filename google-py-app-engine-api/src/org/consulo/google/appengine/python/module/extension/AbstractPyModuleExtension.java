package org.consulo.google.appengine.python.module.extension;

import javax.swing.JComponent;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import com.intellij.remoteServer.impl.configuration.deployment.ModuleDeploymentSourceImpl;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public abstract class AbstractPyModuleExtension<T extends AbstractPyModuleExtension<T>>
		extends ModuleExtensionWithSdkImpl<T>
		implements GoogleAppEngineModuleExtension<T>
{
	public AbstractPyModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@NotNull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@Nullable
	@Override
	public DeploymentSource getDeploymentSource()
	{
		return new ModuleDeploymentSourceImpl(ModuleUtilCore.createPointer(getModule()));
	}

	@Nullable
	@SuppressWarnings("unused")
	public JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable)
	{
		return null;
	}

	@SuppressWarnings("unused")
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@SuppressWarnings("unused")
	public boolean isModifiedImpl(T t)
	{
		return super.isModifiedImpl(t);
	}
}
