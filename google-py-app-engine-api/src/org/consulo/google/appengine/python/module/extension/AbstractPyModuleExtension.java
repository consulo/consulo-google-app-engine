package org.consulo.google.appengine.python.module.extension;

import java.io.File;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.consulo.python.sdk.PythonSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.remoteServer.configuration.deployment.ModuleDeploymentSource;
import com.intellij.remoteServer.impl.configuration.deployment.ModuleDeploymentSourceImpl;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public abstract class AbstractPyModuleExtension<T extends AbstractPyModuleExtension<T>>
		extends ModuleExtensionWithSdkImpl<T>
		implements GoogleAppEngineModuleExtension<ModuleDeploymentSource, T>
{
	public AbstractPyModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	public abstract Sdk getRuntimeSdk();

	@NotNull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@Nullable
	@Override
	public ModuleDeploymentSource getDeploymentSource()
	{
		if(getSdk() == null)
		{
			return null;
		}
		if(getRuntimeSdk() == null)
		{
			return null;
		}
		return new ModuleDeploymentSourceImpl(ModuleUtilCore.createPointer(getModule()));
	}

	@NotNull
	@Override
	public GeneralCommandLine createCommandLine(@NotNull ModuleDeploymentSource deploymentSource, String email, boolean oauth2)
	{
		GeneralCommandLine commandLine = new GeneralCommandLine();
		commandLine.setExePath(PythonSdkType.getInstance().getExecutableFile(getRuntimeSdk()));
		commandLine.addParameter("-u");
		commandLine.addParameter(getSdk().getHomePath() + File.separator + "appcfg.py");
		commandLine.addParameter("--no_cookies");
		commandLine.addParameter("--email");
		commandLine.addParameter(email);
		if(oauth2)
		{
			commandLine.addParameter("--oauth2");
		}
		else
		{
			commandLine.addParameter("--passin");
		}
		commandLine.addParameter("update");
		commandLine.addParameter(deploymentSource.getModule().getModuleDirPath());

		return commandLine;
	}

	@SuppressWarnings("unused")
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}
}
