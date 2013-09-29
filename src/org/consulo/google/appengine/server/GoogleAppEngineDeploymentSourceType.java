package org.consulo.google.appengine.server;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import com.intellij.remoteServer.configuration.deployment.DeploymentSourceType;

/**
 * @author VISTALL
 * @since 29.09.13.
 */
public class GoogleAppEngineDeploymentSourceType extends DeploymentSourceType<GoogleAppEngineDeploymentSource>
{
	protected GoogleAppEngineDeploymentSourceType()
	{
		super("google-app-engine");
	}

	private static DeploymentSourceType byId(String id)
	{
		for(DeploymentSourceType<?> deploymentSourceType : EP_NAME.getExtensions())
		{
			if(deploymentSourceType.getId().equals(id))
			{
				return deploymentSourceType;
			}
		}
		return null;
	}

	@NotNull
	@Override
	public GoogleAppEngineDeploymentSource load(@NotNull Element element, @NotNull Project project)
	{
		DeploymentSourceType delegate = byId(element.getAttributeValue("delegate"));

		assert delegate != null;

		String module = element.getAttributeValue("module-name");
		String moduleExtensionId = element.getAttributeValue("module-extension-id");


		DeploymentSource source = delegate.load(element, project);
		return new GoogleAppEngineDeploymentSource(source, project, module, moduleExtensionId);
	}

	@Override
	public void save(@NotNull GoogleAppEngineDeploymentSource deploymentSource, @NotNull Element element)
	{
		DeploymentSource delegate = deploymentSource.getDelegate();

		DeploymentSourceType<DeploymentSource> type = (DeploymentSourceType<DeploymentSource>) delegate.getType();

		type.save(delegate, element);

		element.setAttribute("delegate", delegate.getType().getId());
		element.setAttribute("module-name", deploymentSource.getModuleName());
		element.setAttribute("module-extension-id", deploymentSource.getModuleExtensionId());
	}
}
