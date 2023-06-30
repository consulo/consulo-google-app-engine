/**
 * @author VISTALL
 * @since 29/06/2023
 */
module consulo.google.app.engine.api
{
	requires transitive consulo.ide.api;

	exports consulo.google.appengine.api.icon;
	exports consulo.google.appengine.module.extension;
	exports consulo.google.appengine.localize;
	exports consulo.google.appengine.sdk;
}