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

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.remoteServer.configuration.deployment.DeploymentConfiguration;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import com.intellij.remoteServer.configuration.localServer.LocalRunner;
import consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;

/**
 * @author VISTALL
 * @since 01.10.13.
 */
public class GoogleAppEngineLocalRunner implements LocalRunner
{
	@Override
	public ExecutionResult execute(@Nonnull DeploymentSource d,
			DeploymentConfiguration deploymentConfiguration,
			ExecutionEnvironment executionEnvironment,
			@Nonnull Executor executor,
			@Nonnull ProgramRunner programRunner) throws ExecutionException
	{
		GoogleAppEngineDeploymentSource deploymentSource = (GoogleAppEngineDeploymentSource) d;

		GoogleAppEngineModuleExtension<DeploymentSource, ?> moduleExtension = deploymentSource.getModuleExtension();

		final ConsoleView console = TextConsoleBuilderFactory.getInstance().createBuilder(moduleExtension.getModule().getProject()).getConsole();

		GeneralCommandLine localServerCommandLine = moduleExtension.createLocalServerCommandLine(deploymentSource.getDelegate());

		try
		{
			final ProcessHandler processHandler = new OSProcessHandler(localServerCommandLine.createProcess(), localServerCommandLine.getCommandLineString());

			console.attachToProcess(processHandler);

			return new DefaultExecutionResult(console, processHandler);
		}
		catch(ExecutionException e)
		{
			throw new ExecutionException(e);
		}
	}
}
