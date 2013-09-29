package org.consulo.google.appengine.python.module.extension;

import org.consulo.sdk.SdkUtil;
import org.consulo.util.pointers.NamedPointer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;

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
