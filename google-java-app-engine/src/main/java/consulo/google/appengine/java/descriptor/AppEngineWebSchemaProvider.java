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
package consulo.google.appengine.java.descriptor;

import com.intellij.java.language.psi.JavaPsiFacade;
import com.intellij.xml.XmlSchemaProvider;
import consulo.annotation.component.ExtensionImpl;
import consulo.content.bundle.Sdk;
import consulo.google.appengine.java.module.extension.JavaAppModuleExtension;
import consulo.google.appengine.java.sdk.JavaAppSdkType;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiPackage;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.xml.psi.xml.XmlFile;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nik
 */
@ExtensionImpl
public class AppEngineWebSchemaProvider extends XmlSchemaProvider
{
	private static final Set<String> FILE_NAMES = new HashSet<String>(Arrays.asList(AppEngineXmlConstants.APP_ENGINE_WEB_XML_NAME, AppEngineXmlConstants.JDO_CONFIG_XML_NAME));

	@Override
	public boolean isAvailable(@Nonnull XmlFile file)
	{
		if(!FILE_NAMES.contains(file.getName()))
		{
			return false;
		}
		final Module module = ModuleUtilCore.findModuleForPsiElement(file);
		return module != null && ModuleUtilCore.getExtension(module, JavaAppModuleExtension.class) != null;
	}

	@Override
	public XmlFile getSchema(@Nonnull @NonNls String url, @Nullable Module module, @Nonnull PsiFile baseFile)
	{
		if(module == null)
		{
			return null;
		}

		if(url.startsWith("http://appengine.google.com/ns/"))
		{
			JavaAppModuleExtension facet = ModuleUtilCore.getExtension(module, JavaAppModuleExtension.class);
			if(facet != null)
			{
				Sdk sdk = facet.getSdk();
				if(sdk == null)
				{
					return null;
				}
				final File file = JavaAppSdkType.getWebSchemeFile(sdk);
				final VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
				if(virtualFile != null)
				{
					final PsiFile psiFile = PsiManager.getInstance(module.getProject()).findFile(virtualFile);
					if(psiFile instanceof XmlFile)
					{
						return (XmlFile) psiFile;
					}
				}
			}
		}
		else if(url.startsWith("http://java.sun.com/xml/ns/jdo/jdoconfig"))
		{
			final PsiPackage jdoPackage = JavaPsiFacade.getInstance(module.getProject()).findPackage("javax.jdo");
			final GlobalSearchScope scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module);
			if(jdoPackage != null)
			{
				for(PsiDirectory directory : jdoPackage.getDirectories(scope))
				{
					final PsiFile file = directory.findFile("jdoconfig_2_3.xsd");
					if(file instanceof XmlFile)
					{
						return (XmlFile) file;
					}
				}
			}
		}

		return null;
	}

}
