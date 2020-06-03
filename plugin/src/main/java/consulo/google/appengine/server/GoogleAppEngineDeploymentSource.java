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

package consulo.google.appengine.server;

import javax.annotation.Nonnull;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import com.intellij.remoteServer.configuration.deployment.DeploymentSourceType;
import consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import consulo.remoteServer.configuration.deployment.DelegateDeploymentSource;
import consulo.util.pointers.NamedPointer;

/**
 * @author VISTALL
 * @since 29.09.13.
 */
public class GoogleAppEngineDeploymentSource extends DelegateDeploymentSource
{
	private final GoogleAppEngineDeploymentSourceType myType;
	private final NamedPointer<Module> myModulePointer;
	private final String myModuleExtensionId;

	private GoogleAppEngineModuleExtension myExtension;

	public GoogleAppEngineDeploymentSource(DeploymentSource delegate, Project project, String name, String id)
	{
		super(delegate);
		myType = DeploymentSourceType.EP_NAME.findExtension(GoogleAppEngineDeploymentSourceType.class);
		myModulePointer = ModuleUtilCore.createPointer(project, name);
		myModuleExtensionId = id;
	}

	public GoogleAppEngineDeploymentSource(DeploymentSource delegate, GoogleAppEngineModuleExtension extension)
	{
		super(delegate);
		myType = DeploymentSourceType.EP_NAME.findExtension(GoogleAppEngineDeploymentSourceType.class);
		myModulePointer = ModuleUtilCore.createPointer(extension.getModule());
		myModuleExtensionId = extension.getId();
		myExtension = extension;
	}

	public GoogleAppEngineModuleExtension getModuleExtension()
	{
		if(myExtension == null)
		{
			Module module = myModulePointer.get();
			if(module == null)
			{
				return null;
			}

			myExtension = (GoogleAppEngineModuleExtension) ModuleUtilCore.getExtension(module, myModuleExtensionId);
		}

		return myExtension;
	}

	@Override
	public boolean isValid()
	{
		return super.isValid() && getModuleExtension() != null && getModule() != null;
	}

	public Module getModule()
	{
		return myModulePointer.get();
	}

	public String getModuleName() 
	{
		return myModulePointer.getName();
	}

	public String getModuleExtensionId()
	{
		return myModuleExtensionId;
	}

	@Nonnull
	@Override
	public DeploymentSourceType<?> getType()
	{
		return myType;
	}

	@Override
	public int hashCode()
	{
		return getDelegate().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof GoogleAppEngineDeploymentSource)
		{
			return getDelegate().equals(((GoogleAppEngineDeploymentSource) obj).getDelegate());
		}
		return false;
	}

	@Override
	public String toString()
	{
		return getDelegate().toString();
	}
}
