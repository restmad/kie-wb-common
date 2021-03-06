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
package org.kie.workbench.common.services.backend.compiler.nio.impl;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;

/***
 * Filter used with DirectoryStreams to avoid the load of hidden files
 */
public class DotFileFilter implements DirectoryStream.Filter<Path> {

    public DotFileFilter() {
    }

    public boolean accept(Path path) {
        if (path != null) {
            return path.getFileName().toString().startsWith(".");
        } else {
            return false;
        }
    }
}
