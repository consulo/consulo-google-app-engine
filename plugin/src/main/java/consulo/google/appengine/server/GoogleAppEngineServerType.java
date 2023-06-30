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
import consulo.configurable.ConfigurationException;
import consulo.configurable.UnnamedConfigurable;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.google.appengine.api.icon.GoogleAppEngineIconGroup;
import consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.project.Project;
import consulo.remoteServer.ServerType;
import consulo.remoteServer.configuration.deployment.DeploymentConfigurator;
import consulo.remoteServer.configuration.deployment.DeploymentSource;
import consulo.remoteServer.configuration.deployment.DummyDeploymentConfiguration;
import consulo.remoteServer.runtime.ServerConnector;
import consulo.remoteServer.runtime.ServerTaskExecutor;
import consulo.remoteServer.runtime.deployment.DeploymentLogManager;
import consulo.remoteServer.runtime.deployment.DeploymentTask;
import consulo.remoteServer.runtime.deployment.ServerRuntimeInstance;
import consulo.remoteServer.runtime.local.LocalRunner;
import consulo.ui.ex.awt.FormBuilder;
import consulo.ui.image.Image;
import consulo.util.collection.SmartList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author nik
 */
@ExtensionImpl
public class GoogleAppEngineServerType extends ServerType<GoogleAppEngineServerConfiguration>
{
	public GoogleAppEngineServerType()
	{
		super("google-app-engine");
	}

	@Nullable
	@Override
	public LocalRunner getLocalRunner()
	{
		return new GoogleAppEngineLocalRunner();
	}

	@Override
	public boolean isConfigurationTypeIsAvailable(@Nonnull Project project)
	{
		return super.isConfigurationTypeIsAvailable(project) && ModuleExtensionHelper.getInstance(project).hasModuleExtension(GoogleAppEngineModuleExtension.class);
	}

	@Nonnull
	@Override
	public String getPresentableName()
	{
		return "Google App Engine";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return GoogleAppEngineIconGroup.appengine();
	}

	@Nonnull
	@Override
	public GoogleAppEngineServerConfiguration createDefaultConfiguration()
	{
		return new GoogleAppEngineServerConfiguration();
	}

	@Nonnull
	@Override
	public UnnamedConfigurable createConfigurable(@Nonnull GoogleAppEngineServerConfiguration configuration)
	{
		return new AppEngineCloudConfigurable(configuration);
	}

	@Nonnull
	@Override
	public ServerConnector<?> createConnector(@Nonnull GoogleAppEngineServerConfiguration configuration, @Nonnull ServerTaskExecutor asyncTasksExecutor)
	{
		return new AppEngineServerConnector(configuration);
	}

	@Nonnull
	@Override
	public DeploymentConfigurator createDeploymentConfigurator(Project project)
	{
		return new AppEngineDeploymentConfigurator(project);
	}

	private static class AppEngineCloudConfigurable implements UnnamedConfigurable
	{
		private final JTextField myEmailField;
		private final GoogleAppEngineServerConfiguration myConfiguration;

		public AppEngineCloudConfigurable(GoogleAppEngineServerConfiguration configuration)
		{
			myConfiguration = configuration;
			myEmailField = new JTextField();
			myEmailField.setPreferredSize(new Dimension(250, myEmailField.getPreferredSize().height));
		}

		@Nullable
		@Override
		public JComponent createComponent()
		{
			return FormBuilder.createFormBuilder().addLabeledComponent("E-mail:", myEmailField).getPanel();
		}

		@Override
		public boolean isModified()
		{
			return !myEmailField.getText().equals(myConfiguration.getEmail());
		}

		@Override
		public void apply() throws ConfigurationException
		{
			myConfiguration.setEmail(myEmailField.getText());
		}

		@Override
		public void reset()
		{
			myEmailField.setText(myConfiguration.getEmail());
		}

		@Override
		public void disposeUIResources()
		{
		}
	}

	private static class AppEngineDeploymentConfigurator extends DeploymentConfigurator<DummyDeploymentConfiguration>
	{
		private final Project myProject;

		public AppEngineDeploymentConfigurator(Project project)
		{
			myProject = project;
		}

		@Nonnull
		@Override
		public List<DeploymentSource> getAvailableDeploymentSources()
		{
			List<DeploymentSource> list = new SmartList<DeploymentSource>();
			for(Module module : ModuleManager.getInstance(myProject).getModules())
			{
				GoogleAppEngineModuleExtension extension = ModuleUtilCore.getExtension(module, GoogleAppEngineModuleExtension.class);
				if(extension == null)
				{
					continue;
				}

				DeploymentSource deploymentSource = extension.getDeploymentSource();
				if(deploymentSource == null)
				{
					continue;
				}
				list.add(new GoogleAppEngineDeploymentSource(deploymentSource, extension));
			}

			return list;
		}

		@Nonnull
		@Override
		public DummyDeploymentConfiguration createDefaultConfiguration(@Nonnull DeploymentSource source)
		{
			return new DummyDeploymentConfiguration();
		}

		@Override
		public SettingsEditor<DummyDeploymentConfiguration> createEditor(@Nonnull DeploymentSource source)
		{
			return null;
		}
	}

	private static class AppEngineServerConnector extends ServerConnector<DummyDeploymentConfiguration>
	{
		private final GoogleAppEngineServerConfiguration myConfiguration;

		public AppEngineServerConnector(GoogleAppEngineServerConfiguration configuration)
		{
			myConfiguration = configuration;
		}

		@Override
		public void connect(@Nonnull final ConnectionCallback<DummyDeploymentConfiguration> callback)
		{
			callback.connected(new AppEngineRuntimeInstance(myConfiguration));
		}
	}

	private static class AppEngineRuntimeInstance extends ServerRuntimeInstance<DummyDeploymentConfiguration>
	{
		private final GoogleAppEngineServerConfiguration myConfiguration;

		public AppEngineRuntimeInstance(GoogleAppEngineServerConfiguration configuration)
		{
			myConfiguration = configuration;
		}

		@Override
		public void deploy(@Nonnull DeploymentTask<DummyDeploymentConfiguration> task, @Nonnull DeploymentLogManager logManager, @Nonnull DeploymentOperationCallback callback)
		{
			GoogleAppEngineDeploymentSource source = (GoogleAppEngineDeploymentSource) task.getSource();

			GoogleAppEngineUploader uploader = GoogleAppEngineUploader.createUploader(source, myConfiguration, callback, logManager.getMainLoggingHandler());
			if(uploader != null)
			{
				uploader.startUploading();
			}
		}

		@Override
		public void computeDeployments(@Nonnull ComputeDeploymentsCallback callback)
		{
			callback.succeeded();
		}

		@Override
		public void disconnect()
		{
		}
	}
}
