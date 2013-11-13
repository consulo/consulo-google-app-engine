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

package org.consulo.google.appengine.module.extension;

import javax.swing.Icon;

import org.consulo.google.appengine.GoogleAppEngineIcons;
import org.consulo.module.extension.ModuleExtension;
import org.consulo.module.extension.ModuleExtensionProvider;
import org.consulo.module.extension.MutableModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public abstract class GoogleAppEngineModuleExtensionProvider
		<ImmutableModel extends ModuleExtension, MutableModel extends MutableModuleExtension>
		implements ModuleExtensionProvider<ImmutableModel, MutableModel>
{
	@Nullable
	@Override
	public Icon getIcon()
	{
		return GoogleAppEngineIcons.AppEngine;
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Google App Engine";
	}
}
