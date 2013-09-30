package org.consulo.google.appengine.py.sdk;

import org.consulo.google.appengine.python.sdk.GoogleAppEnginePySdk;
import org.jetbrains.annotations.NotNull;

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

	@NotNull
	@Override
	public String getLanguageName()
	{
		return "Python";
	}
}
