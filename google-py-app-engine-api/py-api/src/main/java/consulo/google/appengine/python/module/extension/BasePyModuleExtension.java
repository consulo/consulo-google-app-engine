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

package consulo.google.appengine.python.module.extension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.FormBuilder;
import com.jetbrains.python.sdk.PythonSdkType;
import consulo.bundle.SdkUtil;
import consulo.roots.ModuleRootLayer;
import consulo.roots.ui.configuration.SdkComboBox;
import consulo.util.pointers.NamedPointer;

/**
 * @author VISTALL
 * @since 28.09.13.
 */
public abstract class BasePyModuleExtension<T extends BasePyModuleExtension<T>> extends AbstractPyModuleExtension<T>
{
	private static final String RUNTIME_SDK_NAME = "runtime-sdk-name";
	protected NamedPointer<Sdk> myRuntimeSdkPointer;

	public BasePyModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@NotNull
	protected JComponent createRuntimeCheckBox()
	{
		SdkModel model = ProjectStructureConfigurable.getInstance(getModule().getProject()).getProjectSdksModel();

		final SdkComboBox box = new SdkComboBox(model, Conditions.<SdkTypeId>is(PythonSdkType.getInstance()), true);
		if(myRuntimeSdkPointer == null)
		{
			box.setSelectedNoneSdk();
		}
		else
		{
			box.setSelectedSdk(myRuntimeSdkPointer.getName());
		}

		box.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SdkComboBox.SdkComboBoxItem selectedItem = box.getSelectedItem();
				String sdkName = selectedItem.getSdkName();
				myRuntimeSdkPointer = sdkName == null ? null : SdkUtil.createPointer(sdkName);
			}
		});

		return FormBuilder.createFormBuilder().addLabeledComponent("Runtime SDK: ", box).getPanel();
	}

	@Override
	public void commit(@NotNull T mutableModuleExtension)
	{
		super.commit(mutableModuleExtension);

		myRuntimeSdkPointer = mutableModuleExtension.myRuntimeSdkPointer;
	}

	@SuppressWarnings("unused")
	public boolean isModifiedImpl(T t)
	{
		return super.isModifiedImpl(t) || !Comparing.equal(myRuntimeSdkPointer, t.myRuntimeSdkPointer);
	}

	@Override
	public Sdk getRuntimeSdk()
	{
		return myRuntimeSdkPointer == null ? null : myRuntimeSdkPointer.get();
	}

	@Override
	protected void getStateImpl(@NotNull Element element)
	{
		super.getStateImpl(element);

		if(myRuntimeSdkPointer != null)
		{
			element.setAttribute(RUNTIME_SDK_NAME, myRuntimeSdkPointer.getName());
		}
	}

	@Override
	protected void loadStateImpl(@NotNull Element element)
	{
		super.loadStateImpl(element);

		String runtimeSdkName = element.getAttributeValue(RUNTIME_SDK_NAME);
		myRuntimeSdkPointer = StringUtil.isEmpty(runtimeSdkName) ? null : SdkUtil.createPointer(runtimeSdkName);
	}
}
