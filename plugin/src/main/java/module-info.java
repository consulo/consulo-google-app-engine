/**
 * @author VISTALL
 * @since 29/06/2023
 */
open module consulo.google.app.engine
{
	requires transitive consulo.ide.api;
	requires transitive consulo.google.app.engine.api;

	// TODO remove in future
	requires java.desktop;
	requires forms.rt;
}