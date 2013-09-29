package org.consulo.google.appengine.java.module.extension;

import org.consulo.google.appengine.java.sdk.JavaAppSdkType;
import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.remoteServer.configuration.deployment.ArtifactDeploymentSource;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppModuleExtension extends ModuleExtensionWithSdkImpl<JavaAppModuleExtension>
		implements GoogleAppEngineModuleExtension<ArtifactDeploymentSource, JavaAppModuleExtension>
{
	public JavaAppModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@Override
	protected Class<? extends SdkType> getSdkTypeClass()
	{
		return JavaAppSdkType.class;
	}

	@Nullable
	@Override
	public ArtifactDeploymentSource getDeploymentSource()
	{
		return null;
	}

	@NotNull
	@Override
	public GeneralCommandLine createCommandLine(@NotNull ArtifactDeploymentSource deploymentSource, String email, boolean oauth2)
	{
		return null;
	}
}
