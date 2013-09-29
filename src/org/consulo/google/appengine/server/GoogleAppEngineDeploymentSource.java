package org.consulo.google.appengine.server;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import org.consulo.module.extension.ModuleExtensionProvider;
import org.consulo.module.extension.ModuleExtensionProviderEP;
import org.consulo.util.pointers.NamedPointer;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.remoteServer.configuration.deployment.DelegateDeploymentSource;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import com.intellij.remoteServer.configuration.deployment.DeploymentSourceType;

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

			ModuleExtensionProvider provider = ModuleExtensionProviderEP.findProvider(myModuleExtensionId);
			if(provider == null)
			{
				return null;
			}
			myExtension = (GoogleAppEngineModuleExtension) ModuleUtilCore.getExtension(module, provider.getImmutableClass());
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

	@NotNull
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
