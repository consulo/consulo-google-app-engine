package org.consulo.google.appengine.py.sdk;

import org.consulo.google.appengine.python.sdk.GoogleAppEnginePySdk;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PyAppSdkType extends GoogleAppEnginePySdk
{
	public PyAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_PYTHON");
	}

	@Override
	public void setupSdkPaths(Sdk sdk)
	{
		VirtualFile homeDirectory = sdk.getHomeDirectory();
		if(homeDirectory == null)
		{
			return;
		}

		SdkModificator sdkModificator = sdk.getSdkModificator();

		VirtualFile lib = homeDirectory.findChild("lib");
		if(lib == null)
		{
			return;
		}

		for(VirtualFile virtualFile : lib.getChildren())
		{
			if(virtualFile.exists())
			{
				sdkModificator.addRoot(virtualFile, OrderRootType.CLASSES);
			}
		}

		sdkModificator.commitChanges();
	}

	@NotNull
	@Override
	public String getLanguageName()
	{
		return "Python";
	}
}
