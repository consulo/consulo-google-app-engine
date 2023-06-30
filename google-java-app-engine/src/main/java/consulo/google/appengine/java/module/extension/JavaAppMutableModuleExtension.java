/*
 * Copyright 2013-2016 consulo.io
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

package consulo.google.appengine.java.module.extension;

import consulo.application.AllIcons;
import consulo.compiler.artifact.Artifact;
import consulo.compiler.artifact.ArtifactManager;
import consulo.compiler.artifact.ArtifactPointerUtil;
import consulo.content.bundle.Sdk;
import consulo.disposer.Disposable;
import consulo.javaee.artifact.ExplodedWarArtifactType;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.MutableModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.module.extension.swing.SwingMutableModuleExtension;
import consulo.module.ui.extension.ModuleExtensionSdkBoxBuilder;
import consulo.ui.Component;
import consulo.ui.Label;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.SimpleTextAttributes;
import consulo.ui.ex.awt.*;
import consulo.ui.layout.VerticalLayout;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.Comparing;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppMutableModuleExtension extends JavaAppModuleExtension implements MutableModuleExtensionWithSdk<JavaAppModuleExtension>, SwingMutableModuleExtension
{
	public JavaAppMutableModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nonnull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public Component createConfigurationComponent(@Nonnull Disposable disposable, @Nonnull Runnable runnable)
	{
		return VerticalLayout.create().add(Label.create("Unsupported platform"));
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nonnull Disposable disposable, @Nonnull Runnable runnable)
	{
		JPanel panel = new JPanel(new VerticalFlowLayout());
		panel.add(ModuleExtensionSdkBoxBuilder.createAndDefine(this, runnable).build());

		Collection<? extends Artifact> artifactsByType = ArtifactManager.getInstance(getProject()).getArtifactsByType(ExplodedWarArtifactType.getInstance());

		List<String> map = ContainerUtil.map(artifactsByType, (Function<Artifact, String>) artifact -> artifact.getName());

		final ComboBox box = new ComboBox(new CollectionComboBoxModel(map, myArtifactPointer == null ? null : myArtifactPointer.getName()));
		box.setRenderer(new ColoredListCellRenderer()
		{
			@Override
			protected void customizeCellRenderer(JList jList, Object o, int i, boolean b, boolean b2)
			{
				String artifactName = (String) o;
				if(artifactName == null)
				{
					append("");
					return;
				}
				Artifact artifact = ArtifactManager.getInstance(getModule().getProject()).findArtifact(artifactName);
				if(artifact != null)
				{
					append(artifactName);
					setIcon(artifact.getArtifactType().getIcon());
				}
				else
				{
					append(artifactName, SimpleTextAttributes.ERROR_ATTRIBUTES);
					setIcon(AllIcons.Toolbar.Unknown);
				}
			}
		});

		box.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String selectedItem = (String) box.getSelectedItem();
				if(StringUtil.isEmptyOrSpaces(selectedItem))
				{
					myArtifactPointer = null;
				}
				else
				{
					myArtifactPointer = ArtifactPointerUtil.getPointerManager(getModule().getProject()).create((String) selectedItem);
				}
			}
		});

		panel.add(LabeledComponent.left(box, "Artifact"));

		return panel;
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified(@Nonnull JavaAppModuleExtension extension)
	{
		return isModifiedImpl(extension) || !Comparing.equal(myArtifactPointer, extension.myArtifactPointer);
	}
}
