package org.consulo.google.appengine.server;

import com.intellij.remoteServer.configuration.ServerConfigurationBase;
import com.intellij.util.xmlb.annotations.Attribute;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class GoogleAppEngineServerConfiguration extends ServerConfigurationBase<GoogleAppEngineServerConfiguration>
{
	private String myEmail;

	@Attribute("email")
	public String getEmail()
	{
		return myEmail;
	}

	public void setEmail(String email)
	{
		myEmail = email;
	}
}
