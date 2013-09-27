package org.consulo.google.appengine.py.sdk;

import org.consulo.google.appengine.sdk.GoogleAppEngineSdk;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PyAppSdkType extends GoogleAppEngineSdk
{
	public PyAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_PYTHON");
	}

	@NotNull
	@Override
	public String getLanguageName()
	{
		return "Python";
	}

	@Override
	public boolean isValidSdkHome(String s)
	{
		return true;
	}
}
