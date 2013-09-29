package org.consulo.google.appengine.server;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import com.intellij.remoteServer.configuration.deployment.DelegateDeploymentSource;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;

/**
 * @author VISTALL
 * @since 28.09.13.
 */
public class GoogleAppEngineDeploymentSource extends DelegateDeploymentSource
{
	private GoogleAppEngineModuleExtension myModuleExtension;

	public GoogleAppEngineDeploymentSource(DeploymentSource deploymentSource, GoogleAppEngineModuleExtension moduleExtension)
	{
		super(deploymentSource);
		myModuleExtension = moduleExtension;
	}

	public GoogleAppEngineModuleExtension getModuleExtension()
	{
		return myModuleExtension;
	}
}
