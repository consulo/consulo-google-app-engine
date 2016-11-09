/*
 * Copyright 2000-2013 JetBrains s.r.o.
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
package consulo.google.appengine.server;

import java.io.OutputStream;
import java.io.PrintWriter;

import consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import consulo.google.appengine.server.ui.GoogleAppEngineAccountDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.execution.ui.actions.CloseAction;
import com.intellij.ide.passwordSafe.PasswordSafeException;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import com.intellij.remoteServer.runtime.deployment.DeploymentRuntime;
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance;
import com.intellij.remoteServer.runtime.log.LoggingHandler;

/**
 * @author nik
 */
public class GoogleAppEngineUploader
{
	private static final Logger LOG = Logger.getInstance("#org.consulo.google.appengine.server.AppEngineUploader");

	private final GoogleAppEngineDeploymentSource mySource;
	private final String myEmail;
	private final String myPassword;
	private final ServerRuntimeInstance.DeploymentOperationCallback myCallback;

	private GoogleAppEngineUploader(GoogleAppEngineDeploymentSource source, String email, String password, ServerRuntimeInstance.DeploymentOperationCallback callback, @Nullable LoggingHandler loggingHandler)
	{
		mySource = source;
		myEmail = email;
		myPassword = password;
		myCallback = callback;
	}

	@Nullable
	public static GoogleAppEngineUploader createUploader(@NotNull GoogleAppEngineDeploymentSource source, @Nullable GoogleAppEngineServerConfiguration configuration, @NotNull ServerRuntimeInstance.DeploymentOperationCallback callback, @Nullable LoggingHandler loggingHandler)
	{
		GoogleAppEngineModuleExtension extension = source.getModuleExtension();
		Project project = extension.getModule().getProject();

		String password = null;
		String email = null;
		try
		{
			email = GoogleAppEngineAccountDialog.getStoredEmail(configuration, project);
			password = GoogleAppEngineAccountDialog.getStoredPassword(project, email);
		}
		catch(PasswordSafeException e)
		{
			LOG.info("Cannot load stored password: " + e.getMessage());
			LOG.info(e);
		}
		if(StringUtil.isEmpty(email) || StringUtil.isEmpty(password))
		{
			final GoogleAppEngineAccountDialog dialog = new GoogleAppEngineAccountDialog(project, configuration);
			dialog.show();
			if(!dialog.isOK())
			{
				return null;
			}

			email = dialog.getEmail();
			password = dialog.getPassword();
		}

		return new GoogleAppEngineUploader(source, email, password, callback, null);
	}

	public void startUploading()
	{
		FileDocumentManager.getInstance().saveAllDocuments();
		ProgressManager.getInstance().run(new Task.Backgroundable(mySource.getModuleExtension().getModule().getProject(), "Uploading application", true, null)
		{
			public void run(@NotNull ProgressIndicator indicator)
			{
				ApplicationManager.getApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						startUploadingProcess();
					}
				});
			}
		});
	}

	private void startUploadingProcess()
	{
		final GeneralCommandLine commandLine;

		GoogleAppEngineModuleExtension<DeploymentSource, ?> moduleExtension = mySource.getModuleExtension();

		Project project = mySource.getModuleExtension().getModule().getProject();

		try
		{
			commandLine = moduleExtension.createCommandLine(mySource.getDelegate(), myEmail, false);

			final Executor executor = DefaultRunExecutor.getRunExecutorInstance();

			final ConsoleView console = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
			final RunnerLayoutUi ui = RunnerLayoutUi.Factory.getInstance(project).create("Upload", "Upload Application", "Upload Application", project);
			final DefaultActionGroup group = new DefaultActionGroup();
			ui.getOptions().setLeftToolbar(group, ActionPlaces.UNKNOWN);
			ui.addContent(ui.createContent("upload", console.getComponent(), "Upload Application", null, console.getPreferredFocusableComponent()));

			final ProcessHandler processHandler = new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
			processHandler.addProcessListener(new MyProcessListener(processHandler));
			console.attachToProcess(processHandler);
			processHandler.startNotify();

			final RunContentDescriptor contentDescriptor = new RunContentDescriptor(console, processHandler, ui.getComponent(), "Upload Application");
			group.add(ActionManager.getInstance().getAction(IdeActions.ACTION_STOP_PROGRAM));
			group.add(new CloseAction(executor, contentDescriptor, project));

			ExecutionManager.getInstance(project).getContentManager().showRunContent(executor, contentDescriptor);
		}
		catch(ExecutionException e)
		{
			e.printStackTrace();
		}
	}

	private class MyProcessListener extends ProcessAdapter
	{
		private final ProcessHandler myProcessHandler;

		private boolean myPasswordEntered;

		public MyProcessListener(ProcessHandler processHandler)
		{
			myProcessHandler = processHandler;
		}

		@Override
		public void onTextAvailable(ProcessEvent event, Key outputType)
		{
			if(!myPasswordEntered && !outputType.equals(ProcessOutputTypes.SYSTEM) && event.getText().contains(myEmail))
			{
				myPasswordEntered = true;
				final OutputStream processInput = myProcessHandler.getProcessInput();
				if(processInput != null)
				{
					//noinspection IOResourceOpenedButNotSafelyClosed
					final PrintWriter input = new PrintWriter(processInput);
					input.println(myPassword);
					input.flush();
				}
			}
		}

		@Override
		public void processTerminated(ProcessEvent event)
		{
			int exitCode = event.getExitCode();
			if(exitCode == 0)
			{
				myCallback.succeeded(new DeploymentRuntime()
				{
					@Override
					public boolean isUndeploySupported()
					{
						return false;
					}

					@Override
					public void undeploy(@NotNull UndeploymentTaskCallback callback)
					{
					}
				});
			}
			else
			{
				myCallback.errorOccurred("Process terminated with exit code " + exitCode);
			}
		}
	}
}
