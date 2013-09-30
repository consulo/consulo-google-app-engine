package org.consulo.google.appengine.sdk;

import java.io.File;

import javax.swing.Icon;

import org.consulo.google.appengine.GoogleAppEngineIcons;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public abstract class GoogleAppEngineSdk extends SdkType
{
	public GoogleAppEngineSdk(@NonNls String name)
	{
		super(name);
	}

	@NotNull
	public abstract String getLanguageName();

	@NotNull
	@Override
	public String getPresentableName()
	{
		return String.format("Google App Engine (%s)", getLanguageName());
	}

	@Nullable
	@Override
	public String suggestHomePath()
	{
		return null;
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return GoogleAppEngineIcons.AppEngine;
	}

	@Nullable
	@Override
	public Icon getGroupIcon()
	{
		return getIcon();
	}

	@Nullable
	@Override
	public abstract String getVersionString(String s);

	@Override
	public boolean isValidSdkHome(String s)
	{
		return getVersionString(s) != null;
	}

	@Override
	public String suggestSdkName(String s, String s2)
	{
		File file = new File(s2);
		return file.getName();
	}

	@Nullable
	@Override
	public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator)
	{
		return null;
	}

	@Override
	public void saveAdditionalData(SdkAdditionalData sdkAdditionalData, Element element)
	{

	}
}
