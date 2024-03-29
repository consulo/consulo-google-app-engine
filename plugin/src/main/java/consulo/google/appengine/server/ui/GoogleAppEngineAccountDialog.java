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
package consulo.google.appengine.server.ui;

import consulo.application.CommonBundle;
import consulo.credentialStorage.PasswordSafe;
import consulo.credentialStorage.PasswordSafeException;
import consulo.google.appengine.server.GoogleAppEngineServerConfiguration;
import consulo.project.Project;
import consulo.project.ProjectPropertiesComponent;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.ui.ex.awt.Messages;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

/**
 * @author nik
 */
public class GoogleAppEngineAccountDialog extends DialogWrapper
{
  public static final String PASSWORD_KEY = "GOOGLE_APP_ENGINE_PASSWORD";
  private static final String EMAIL_KEY = "GOOGLE_APP_ENGINE_ACCOUNT_EMAIL";
  private JPanel myMainPanel;
  private JCheckBox myRememberPasswordCheckBox;
  private JPasswordField myPasswordField;
  private JTextField myUserEmailField;
  private final Project myProject;
  @Nullable
  private final GoogleAppEngineServerConfiguration myConfiguration;

  public GoogleAppEngineAccountDialog(@Nonnull Project project, @Nullable GoogleAppEngineServerConfiguration configuration) {
    super(project);
    myProject = project;
    myConfiguration = configuration;
    setTitle("Google App Engine Account");
    myUserEmailField.setText(StringUtil.notNullize(getStoredEmail(myConfiguration, project)));
    init();
  }

  public String getEmail() {
    return myUserEmailField.getText();
  }

  public String getPassword() {
    return new String(myPasswordField.getPassword());
  }

  @Override
  protected JComponent createCenterPanel() {
    return myMainPanel;
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return myUserEmailField;
  }

  @Nullable
  public static String getStoredEmail(@Nullable GoogleAppEngineServerConfiguration configuration, @Nonnull Project project) {
    if (configuration != null) {
      return configuration.getEmail();
    }
    return ProjectPropertiesComponent.getInstance(project).getValue(EMAIL_KEY);//todo[nik] remove this
  }

  @Override
  protected void doOKAction() {
    final String email = getEmail();
    if (myConfiguration != null) {
      myConfiguration.setEmail(email);
    }
    else {
      ProjectPropertiesComponent.getInstance(myProject).setValue(EMAIL_KEY, email);
    }
    if (myRememberPasswordCheckBox.isSelected()) {
      try {
        PasswordSafe.getInstance().storePassword(myProject, GoogleAppEngineAccountDialog.class, getPasswordKey(email), getPassword());
      }
      catch (PasswordSafeException e) {
        Messages.showErrorDialog(myProject, "Cannot store password: " + e.getMessage(), CommonBundle.getErrorTitle());
        return;
      }
    }
    super.doOKAction();
  }

  private static String getPasswordKey(String email) {
    return PASSWORD_KEY + "_" + email;
  }

  @Nullable
  public static String getStoredPassword(Project project, String email) throws PasswordSafeException {
    if (StringUtil.isEmpty(email)) {
      return null;
    }

    return PasswordSafe.getInstance().getPassword(project, GoogleAppEngineAccountDialog.class, getPasswordKey(email));
  }
}
