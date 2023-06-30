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

import consulo.execution.DefaultExecutionResult;
import consulo.execution.ExecutionResult;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.runner.ProgramRunner;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.cmd.GeneralCommandLine;
import consulo.remoteServer.configuration.deployment.DeploymentConfiguration;
import consulo.remoteServer.configuration.deployment.DeploymentSource;
import consulo.remoteServer.runtime.local.LocalRunner;

import javax.annotation.Nonnull;

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
			final ProcessHandler processHandler = ProcessHandlerBuilder.create(localServerCommandLine).build();

			console.attachToProcess(processHandler);

			return new DefaultExecutionResult(console, processHandler);
		}
		catch(ExecutionException e)
		{
			throw new ExecutionException(e);
		}
	}
}
