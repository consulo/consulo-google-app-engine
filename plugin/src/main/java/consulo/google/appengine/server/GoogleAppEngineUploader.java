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

import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.ProgressManager;
import consulo.application.progress.Task;
import consulo.credentialStorage.PasswordSafeException;
import consulo.document.FileDocumentManager;
import consulo.execution.ExecutionManager;
import consulo.execution.action.CloseAction;
import consulo.execution.executor.DefaultRunExecutor;
import consulo.execution.executor.Executor;
import consulo.execution.ui.RunContentDescriptor;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.execution.ui.layout.RunnerLayoutUi;
import consulo.execution.ui.layout.RunnerLayoutUiFactory;
import consulo.google.appengine.module.extension.GoogleAppEngineModuleExtension;
import consulo.google.appengine.server.ui.GoogleAppEngineAccountDialog;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.ProcessOutputTypes;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessAdapter;
import consulo.process.event.ProcessEvent;
import consulo.project.Project;
import consulo.remoteServer.configuration.deployment.DeploymentSource;
import consulo.remoteServer.runtime.deployment.DeploymentRuntime;
import consulo.remoteServer.runtime.deployment.ServerRuntimeInstance;
import consulo.remoteServer.runtime.log.LoggingHandler;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.ActionPlaces;
import consulo.ui.ex.action.DefaultActionGroup;
import consulo.ui.ex.action.IdeActions;
import consulo.util.dataholder.Key;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.OutputStream;
import java.io.PrintWriter;

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
	public static GoogleAppEngineUploader createUploader(@Nonnull GoogleAppEngineDeploymentSource source, @Nullable GoogleAppEngineServerConfiguration configuration, @Nonnull ServerRuntimeInstance.DeploymentOperationCallback callback, @Nullable LoggingHandler loggingHandler)
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
			public void run(@Nonnull ProgressIndicator indicator)
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
			final RunnerLayoutUi ui = RunnerLayoutUiFactory.getInstance(project).create("Upload", "Upload Application", "Upload Application", project);
			final DefaultActionGroup group = new DefaultActionGroup();
			ui.getOptions().setLeftToolbar(group, ActionPlaces.UNKNOWN);
			ui.addContent(ui.createContent("upload", console.getComponent(), "Upload Application", null, console.getPreferredFocusableComponent()));

			final ProcessHandler processHandler = ProcessHandlerBuilder.create(commandLine).build();
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
					public void undeploy(@Nonnull UndeploymentTaskCallback callback)
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
