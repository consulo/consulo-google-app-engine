package org.consulo.google.appengine.python.module.extension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.consulo.python.sdk.PythonSdkType;
import org.consulo.sdk.SdkUtil;
import org.consulo.util.pointers.NamedPointer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.roots.ui.configuration.SdkComboBox;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.FormBuilder;

/**
 * @author VISTALL
 * @since 28.09.13.
 */
public abstract class BasePyModuleExtension<T extends BasePyModuleExtension<T>> extends AbstractPyModuleExtension<T>
{
	private static final String RUNTIME_SDK_NAME = "runtime-sdk-name";
	protected NamedPointer<Sdk> myRuntimeSdkPointer;

	public BasePyModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@NotNull
	protected JComponent createRuntimeCheckBox()
	{
		ProjectSdksModel model = ProjectStructureConfigurable.getInstance(getModule().getProject()).getProjectSdksModel();

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
