/**
 * @author VISTALL
 * @since 29/06/2023
 */
module consulo.google.app.engine.java
{
	requires consulo.google.app.engine.api;
	requires consulo.java;
	requires com.intellij.xml;
	requires consulo.jakartaee.web.api;
	requires consulo.jakartaee.web.impl;

	// TODO remove in future
	requires java.desktop;
}