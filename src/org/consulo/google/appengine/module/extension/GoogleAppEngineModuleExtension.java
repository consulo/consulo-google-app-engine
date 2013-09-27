package org.consulo.google.appengine.module.extension;

import org.consulo.module.extension.ModuleExtensionWithSdk;
import org.jetbrains.annotations.Nullable;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public interface GoogleAppEngineModuleExtension<T extends GoogleAppEngineModuleExtension<T>> extends ModuleExtensionWithSdk<T>
{
	@Nullable
	DeploymentSource getDeploymentSource();
}
