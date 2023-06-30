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

package consulo.google.appengine.java.sdk;

import com.intellij.java.language.projectRoots.JavaSdk;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.SystemInfo;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModificator;
import consulo.content.bundle.SdkType;
import consulo.google.appengine.sdk.GoogleAppEngineSdkType;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.util.CapturingProcessUtil;
import consulo.process.util.ProcessOutput;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.archive.ArchiveVfsUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
@ExtensionImpl
public class JavaAppSdkType extends GoogleAppEngineSdkType
{
	public static final String APPCFG = "appcfg";

	public JavaAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_JAVA");
	}

	@Override
	public void setupSdkPaths(Sdk sdk)
	{
		SdkModificator sdkModificator = sdk.getSdkModificator();

		VirtualFile homeDirectory = sdk.getHomeDirectory();

		VirtualFile sharedDir = homeDirectory.findFileByRelativePath("lib/shared");

		if(sharedDir != null)
		{
			for(VirtualFile virtualFile : sharedDir.getChildren())
			{
				VirtualFile archiveRootForLocalFile = ArchiveVfsUtil.getArchiveRootForLocalFile(virtualFile);
				if(archiveRootForLocalFile != null)
				{
					sdkModificator.addRoot(archiveRootForLocalFile, BinariesOrderRootType.getInstance());
				}
			}
		}

		sdkModificator.commitChanges();
	}

	@Nonnull
	public static String getExecutable(String sdkHome, String file)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(sdkHome);
		builder.append(File.separator);
		builder.append("bin");
		builder.append(File.separator);
		builder.append(file);
		if(SystemInfo.isWindows)
		{
			builder.append(".cmd");
		}
		return builder.toString();
	}

	public static File getWebSchemeFile(@Nonnull Sdk sdk)
	{
		return getWebSchemeFile(sdk.getHomePath());
	}

	public static File getWebSchemeFile(@Nonnull String home)
	{
		return new File(FileUtil.toSystemDependentName(home + "/docs/appengine-web.xsd"));
	}

	@Nonnull
	@Override
	public String getLanguageName()
	{
		return "Java";
	}

	@Nonnull
	@Override
	public SdkType getLanguageSdkType()
	{
		return JavaSdk.getInstance();
	}

	@Nullable
	@Override
	public String getVersionString(String path)
	{
		try
		{
			GeneralCommandLine cmd = new GeneralCommandLine();
			cmd.setExePath(getExecutable(path, APPCFG));
			cmd.addParameter("version");
			cmd.setWorkDirectory(path);

			ProcessOutput processOutput = CapturingProcessUtil.execAndGetOutput(cmd);
			List<String> stdoutLines = processOutput.getStdoutLines();
			if(stdoutLines.isEmpty())
			{
				return null;
			}
			String line = stdoutLines.get(0);
			if(!line.contains(":"))
			{
				return null;
			}
			List<String> split = StringUtil.split(line, ":");
			return split.get(1).trim();
		}
		catch(ExecutionException e)
		{
			return null;
		}
	}

	@Override
	public boolean isValidSdkHome(String path)
	{
		return new File(getExecutable(path, APPCFG)).exists();
	}
}
