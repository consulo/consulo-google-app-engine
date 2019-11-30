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

import com.intellij.icons.AllIcons;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactManager;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import consulo.extension.ui.ModuleExtensionSdkBoxBuilder;
import consulo.javaee.artifact.ExplodedWarArtifactType;
import consulo.module.extension.MutableModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.packaging.artifacts.ArtifactPointerUtil;
import consulo.roots.ModuleRootLayer;
import consulo.ui.annotation.RequiredUIAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 27.09.13.
 */
public class JavaAppMutableModuleExtension extends JavaAppModuleExtension implements MutableModuleExtensionWithSdk<JavaAppModuleExtension>
{
	public JavaAppMutableModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@NotNull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@Override
	@Nullable
	@RequiredUIAccess
	public JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		JPanel panel = new JPanel(new VerticalFlowLayout());
		panel.add(ModuleExtensionSdkBoxBuilder.createAndDefine(this, runnable).build());

		Collection<? extends Artifact> artifactsByType = ArtifactManager.getInstance(getProject()).getArtifactsByType(ExplodedWarArtifactType.getInstance());

		List<String> map = ContainerUtil.map(artifactsByType, new Function<Artifact, String>()
		{
			@Override
			public String fun(Artifact artifact)
			{
				return artifact.getName();
			}
		});

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
	public boolean isModified(@NotNull JavaAppModuleExtension extension)
	{
		return isModifiedImpl(extension) || !Comparing.equal(myArtifactPointer, extension.myArtifactPointer);
	}
}
