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

import consulo.annotation.component.ExtensionImpl;
import consulo.project.Project;
import consulo.remoteServer.configuration.deployment.DeploymentSource;
import consulo.remoteServer.configuration.deployment.DeploymentSourceType;
import org.jdom.Element;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 29.09.13.
 */
@ExtensionImpl
public class GoogleAppEngineDeploymentSourceType extends DeploymentSourceType<GoogleAppEngineDeploymentSource>
{
	public GoogleAppEngineDeploymentSourceType()
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

	@Nonnull
	@Override
	public GoogleAppEngineDeploymentSource load(@Nonnull Element element, @Nonnull Project project)
	{
		DeploymentSourceType delegate = byId(element.getAttributeValue("delegate"));

		assert delegate != null;

		String module = element.getAttributeValue("module-name");
		String moduleExtensionId = element.getAttributeValue("module-extension-id");


		DeploymentSource source = delegate.load(element, project);
		return new GoogleAppEngineDeploymentSource(source, project, module, moduleExtensionId);
	}

	@Override
	public void save(@Nonnull GoogleAppEngineDeploymentSource deploymentSource, @Nonnull Element element)
	{
		DeploymentSource delegate = deploymentSource.getDelegate();

		DeploymentSourceType<DeploymentSource> type = (DeploymentSourceType<DeploymentSource>) delegate.getType();

		type.save(delegate, element);

		element.setAttribute("delegate", delegate.getType().getId());
		element.setAttribute("module-name", deploymentSource.getModuleName());
		element.setAttribute("module-extension-id", deploymentSource.getModuleExtensionId());
	}
}
