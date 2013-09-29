package org.consulo.google.appengine.module.extension;

import org.consulo.module.extension.ModuleExtensionWithSdk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public interface GoogleAppEngineModuleExtension<D extends DeploymentSource, T extends GoogleAppEngineModuleExtension<D, T>> extends ModuleExtensionWithSdk<T>
{
	@Nullable
	D getDeploymentSource();

	@NotNull
	GeneralCommandLine createCommandLine(@NotNull D deploymentSource, String email, boolean oauth2);
}
