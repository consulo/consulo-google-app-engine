/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.consulo.google.appengine.java.module.extension;

import org.consulo.google.appengine.module.extension.GoogleAppEngineModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppModuleExtensionProvider extends GoogleAppEngineModuleExtensionProvider<JavaAppModuleExtension, JavaAppMutableModuleExtension>
{
	@NotNull
	@Override
	public Class<JavaAppModuleExtension> getImmutableClass()
	{
		return JavaAppModuleExtension.class;
	}

	@NotNull
	@Override
	public JavaAppModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new JavaAppModuleExtension(s, module);
	}

	@NotNull
	@Override
	public JavaAppMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull JavaAppModuleExtension javaAppModuleExtension)
	{
		return new JavaAppMutableModuleExtension(s, module, javaAppModuleExtension);
	}
}
