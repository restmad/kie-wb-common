/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.services.backend.compiler.internalNIO.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.maven.model.Model;
import org.kie.workbench.common.services.backend.compiler.PluginPresents;
import org.kie.workbench.common.services.backend.compiler.configuration.Compilers;
import org.kie.workbench.common.services.backend.compiler.configuration.ConfigurationProvider;
import org.kie.workbench.common.services.backend.compiler.impl.DefaultPomEditor;
import org.kie.workbench.common.services.backend.compiler.impl.PomPlaceHolder;
import org.kie.workbench.common.services.backend.compiler.internalNIO.InternalNIOCompilationRequest;
import org.uberfire.java.nio.file.Files;
import org.uberfire.java.nio.file.Path;
import org.uberfire.java.nio.file.Paths;
import org.uberfire.java.nio.file.StandardOpenOption;

/***
 * Default NIO2 Internal impl specialization of the generic DefaultPomEditor
 */
public class InternalNIODefaultPomEditor extends DefaultPomEditor {

    public InternalNIODefaultPomEditor(Set<PomPlaceHolder> history,
                                       ConfigurationProvider config,
                                       Compilers compiler) {
        super(history,
              config,
              compiler);
    }

    public PomPlaceHolder readSingle(Path pom) {
        PomPlaceHolder holder = new PomPlaceHolder();
        try {
            Model model = reader.read(new ByteArrayInputStream(Files.readAllBytes(pom)));
            holder = new PomPlaceHolder(pom.toAbsolutePath().toString(),
                                        model.getArtifactId(),
                                        model.getGroupId(),
                                        model.getVersion(),
                                        model.getPackaging());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return holder;
    }

    public void write(Path pom,
                      InternalNIOCompilationRequest request) {

        try {
            Model model = reader.read(new ByteArrayInputStream(Files.readAllBytes(pom)));
            if (model == null) {
                logger.error("Model null from pom file:",
                             pom.toString());
                return;
            }

            PomPlaceHolder pomPH = new PomPlaceHolder(pom.toAbsolutePath().toString(),
                                                      model.getArtifactId(),
                                                      model.getGroupId(),
                                                      model.getVersion(),
                                                      model.getPackaging(),
                                                      Files.readAllBytes(Paths.get(pom.toAbsolutePath().toString())));

            if (!history.contains(pomPH)) {

                PluginPresents plugs = updatePom(model);
                request.getInfo().lateAdditionKiePluginPresent(plugs.isKiePluginPresent());

                if (plugs.isKiePluginPresent()) {
                    String args[] = addCreateClasspathMavenArgs(request.getKieCliRequest().getArgs());
                    request.getKieCliRequest().setArgs(args);
                }
                if (plugs.pomOverwriteRequired()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    writer.write(baos,
                                 model);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Pom changed:{}",
                                     new String(baos.toByteArray(),
                                                StandardCharsets.UTF_8));
                    }
                    Path pomParent = Paths.get(pom.getParent().toAbsolutePath().toString(),
                                               POM_NAME);
                    Files.delete(pomParent);
                    Files.write(pomParent,
                                baos.toByteArray(),
                                StandardOpenOption.CREATE_NEW);//enhanced pom
                }
                history.add(pomPH);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
