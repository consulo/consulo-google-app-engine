/*
 * Copyright 2013-2016 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.google.appengine.python.module.extension;

import com.jetbrains.python.impl.sdk.PythonSdkType;
import consulo.content.bundle.Sdk;
import consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.content.layer.extension.ModuleExtensionWithSdkBase;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.remoteServer.configuration.deployment.DeploymentSourceFactory;
import consulo.remoteServer.configuration.deployment.ModuleDeploymentSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public abstract class AbstractPyModuleExtension<T extends AbstractPyModuleExtension<T>> extends ModuleExtensionWithSdkBase<T> implements GoogleAppEngineModuleExtension<ModuleDeploymentSource, T>
{
	public AbstractPyModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	public abstract Sdk getRuntimeSdk();

	@Nonnull
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
		DeploymentSourceFactory factory = getProject().getInstance(DeploymentSourceFactory.class);
		return factory.createModuleDeploymentSource(getModule().getName());
	}

	@Nonnull
	@Override
	public GeneralCommandLine createCommandLine(@Nonnull ModuleDeploymentSource deploymentSource, String email, boolean oauth2)
	{
		GeneralCommandLine commandLine = new GeneralCommandLine();
		commandLine.setExePath(PythonSdkType.getPythonExecutable(getRuntimeSdk().getHomePath()));
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

	@Nonnull
	@Override
	public GeneralCommandLine createLocalServerCommandLine(ModuleDeploymentSource deploymentSource) throws ExecutionException
	{
		GeneralCommandLine commandLine = new GeneralCommandLine();
		commandLine.setExePath(PythonSdkType.getPythonExecutable(getRuntimeSdk().getHomePath()));
		commandLine.addParameter("-u");
		commandLine.addParameter(getSdk().getHomePath() + File.separator + "dev_appserver.py");
		commandLine.addParameter(deploymentSource.getModule().getModuleDirPath());
		return commandLine;
	}

	@SuppressWarnings("unused")
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}
}
