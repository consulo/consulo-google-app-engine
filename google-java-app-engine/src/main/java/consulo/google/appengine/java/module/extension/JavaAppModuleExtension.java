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

package consulo.google.appengine.java.module.extension;

import consulo.compiler.artifact.Artifact;
import consulo.compiler.artifact.ArtifactPointer;
import consulo.compiler.artifact.ArtifactPointerUtil;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkType;
import consulo.google.appengine.java.sdk.JavaAppSdkType;
import consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import consulo.java.language.module.extension.JavaModuleExtension;
import consulo.language.util.ModuleUtilCore;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.content.layer.extension.ModuleExtensionWithSdkBase;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.remoteServer.configuration.deployment.ArtifactDeploymentSource;
import consulo.remoteServer.configuration.deployment.DeploymentSourceFactory;
import org.jdom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppModuleExtension extends ModuleExtensionWithSdkBase<JavaAppModuleExtension> implements GoogleAppEngineModuleExtension<ArtifactDeploymentSource, JavaAppModuleExtension>
{
	protected ArtifactPointer myArtifactPointer;

	public JavaAppModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nonnull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
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

		DeploymentSourceFactory factory = getProject().getInstance(DeploymentSourceFactory.class);
		return factory.createArtifactDeploymentSource(myArtifactPointer.getName());
	}

	@Override
	public void commit(@Nonnull JavaAppModuleExtension mutableModuleExtension)
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
	protected void loadStateImpl(@Nonnull Element element)
	{
		super.loadStateImpl(element);

		String artifactName = element.getAttributeValue("artifact-name");
		if(artifactName != null)
		{
			myArtifactPointer = ArtifactPointerUtil.getPointerManager(getModule().getProject()).create(artifactName);
		}
	}

	@Override
	protected void getStateImpl(@Nonnull Element element)
	{
		super.getStateImpl(element);

		if(myArtifactPointer != null)
		{
			element.setAttribute("artifact-name", myArtifactPointer.getName());
		}
	}

	@Nonnull
	@Override
	public GeneralCommandLine createCommandLine(@Nonnull ArtifactDeploymentSource deploymentSource, String email, boolean oauth2) throws ExecutionException
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
		commandLine.setExePath(JavaAppSdkType.getExecutable(sdk.getHomePath(), JavaAppSdkType.APPCFG));
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

	@Nonnull
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
		commandLine.setExePath(JavaAppSdkType.getExecutable(sdk.getHomePath(), "dev_appserver"));
		commandLine.addParameter(deploymentSource.getArtifact().getOutputPath());
		return commandLine;
	}
}
