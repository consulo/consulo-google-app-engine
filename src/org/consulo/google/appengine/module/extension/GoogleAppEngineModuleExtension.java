/*
 * Copyright 2013 must-be.org
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

package org.consulo.google.appengine.module.extension;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import consulo.module.extension.ModuleExtensionWithSdk;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public interface GoogleAppEngineModuleExtension<D extends DeploymentSource, T extends GoogleAppEngineModuleExtension<D, T>> extends ModuleExtensionWithSdk<T>
{
	@Nullable
	D getDeploymentSource();

	@NotNull
	GeneralCommandLine createCommandLine(@NotNull D deploymentSource, String email, boolean oauth2) throws ExecutionException;

	@NotNull
	GeneralCommandLine createLocalServerCommandLine(D deploymentSource) throws ExecutionException;
}
