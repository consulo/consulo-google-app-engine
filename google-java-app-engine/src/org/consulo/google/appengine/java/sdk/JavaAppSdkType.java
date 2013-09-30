package org.consulo.google.appengine.java.sdk;

import org.consulo.google.appengine.sdk.GoogleAppEngineSdk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppSdkType extends GoogleAppEngineSdk
{
	public JavaAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_JAVA");
	}

	@NotNull
	@Override
	public String getLanguageName()
	{
		return "Java";
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		return "1";
	}

	@Override
	public boolean isValidSdkHome(String s)
	{
		return true;
	}
}
