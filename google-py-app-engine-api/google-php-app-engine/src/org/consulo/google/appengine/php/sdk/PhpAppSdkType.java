package org.consulo.google.appengine.php.sdk;

import org.consulo.google.appengine.sdk.GoogleAppEngineSdk;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class PhpAppSdkType extends GoogleAppEngineSdk
{
	public PhpAppSdkType()
	{
		super("GOOGLE_APP_ENGINE_PHP");
	}

	@NotNull
	@Override
	public String getLanguageName()
	{
		return "PHP";
	}

	@Override
	public boolean isValidSdkHome(String s)
	{
		return true;
	}
}
