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

package org.consulo.google.appengine.java.module.extension;

import java.io.File;

import org.consulo.google.appengine.java.sdk.JavaAppSdkType;
import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import org.consulo.java.module.extension.JavaModuleExtension;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactPointer;
import com.intellij.packaging.artifacts.ArtifactPointerUtil;
import com.intellij.remoteServer.configuration.deployment.ArtifactDeploymentSource;
import com.intellij.remoteServer.impl.configuration.deploySource.impl.ArtifactDeploymentSourceImpl;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppModuleExtension extends ModuleExtensionWithSdkImpl<JavaAppModuleExtension>
		implements GoogleAppEngineModuleExtension<ArtifactDeploymentSource, JavaAppModuleExtension>
{
	protected ArtifactPointer myArtifactPointer;

	public JavaAppModuleExtension(@NotNull String id, @NotNull ModifiableRootModel module)
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
		if(myArtifactPointer == null)
		{
			return null;
		}
		return new ArtifactDeploymentSourceImpl(myArtifactPointer);
	}

	@Override
	public void commit(@NotNull JavaAppModuleExtension mutableModuleExtension)
	{
		super.commit(mutableModuleExtension);

		myArtifactPointer = mutableModuleExtension.myArtifactPointer;
	}

	@Nullable
	public Artifact getArtifact()
	{
		return myArtifactPointer == null ? null : myArtifactPointer.get();
	}

	@Override
	protected void loadStateImpl(@NotNull Element element)
	{
		super.loadStateImpl(element);

		String artifactName = element.getAttributeValue("artifact-name");
		if(artifactName != null)
		{
			myArtifactPointer = ArtifactPointerUtil.getPointerManager(getModule().getProject()).create(artifactName);
		}
	}

	@Override
	protected void getStateImpl(@NotNull Element element)
	{
		super.getStateImpl(element);

		if(myArtifactPointer != null)
		{
			element.setAttribute("artifact-name", myArtifactPointer.getName());
		}
	}

	@NotNull
	@Override
	public GeneralCommandLine createCommandLine(@NotNull ArtifactDeploymentSource deploymentSource, String email, boolean oauth2) throws ExecutionException
	{
		Sdk javaSdk = ModuleUtilCore.getSdk(getModule(), JavaModuleExtension.class);
		if(javaSdk == null)
		{
			throw new ExecutionException("No java sdk");
		}

		Sdk sdk = getSdk();
		if(sdk == null)
		{
			throw new ExecutionException("No app engine sdk");
		}

		GeneralCommandLine commandLine = new GeneralCommandLine();
		commandLine.getEnvironment().put("JAVA_HOME", javaSdk.getHomePath());
		commandLine.setExePath(getExecutable("appcfg"));
		commandLine.addParameter("--no_cookies");
		if(oauth2)
		{
			commandLine.addParameter("--oauth2");
		}
		else
		{
			commandLine.addParameter("--email=" + email);
		}
		commandLine.addParameter("update");
		commandLine.addParameter(deploymentSource.getArtifact().getOutputPath());

		return commandLine;
	}

	@NotNull
	@Override
	public GeneralCommandLine createLocalServerCommandLine(ArtifactDeploymentSource deploymentSource)  throws ExecutionException
	{
		Sdk javaSdk = ModuleUtilCore.getSdk(getModule(), JavaModuleExtension.class);
		if(javaSdk == null)
		{
			throw new ExecutionException("No java sdk");
		}

		Sdk sdk = getSdk();
		if(sdk == null)
		{
			throw new ExecutionException("No app engine sdk");
		}

		GeneralCommandLine commandLine = new GeneralCommandLine();
		commandLine.getEnvironment().put("JAVA_HOME", javaSdk.getHomePath());
		commandLine.setExePath(getExecutable("dev_appserver"));
		commandLine.addParameter(deploymentSource.getArtifact().getOutputPath());
		return commandLine;
	}

	private String getExecutable(String name)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getSdk().getHomePath());
		builder.append(File.separator);
		builder.append("bin");
		builder.append(File.separator);
		builder.append(name);
		if(SystemInfo.isWindows)
		{
			builder.append(".cmd");
		}
		return builder.toString();
	}
}
