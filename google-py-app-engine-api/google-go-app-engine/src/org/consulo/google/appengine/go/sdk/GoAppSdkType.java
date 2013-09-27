package org.consulo.google.appengine.go.sdk;

import org.consulo.google.appengine.sdk.GoogleAppEngineSdk;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class GoAppSdkType extends GoogleAppEngineSdk
{
	public GoAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_GO");
	}

	@NotNull
	@Override
	public String getLanguageName()
	{
		return "Go";
	}

	@Override
	public boolean isValidSdkHome(String s)
	{
		return true;
	}
}
