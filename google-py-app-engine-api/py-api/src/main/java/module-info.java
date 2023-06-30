/**
 * @author VISTALL
 * @since 29/06/2023
 */
module consulo.google.app.engine.py.gae.api
{
	requires transitive consulo.google.app.engine.api;
	requires transitive consulo.python.impl;

	requires org.yaml.snakeyaml;

	// TODO remove in future
	requires java.desktop;

	exports consulo.google.appengine.python.module.extension;
	exports consulo.google.appengine.python.sdk;
}