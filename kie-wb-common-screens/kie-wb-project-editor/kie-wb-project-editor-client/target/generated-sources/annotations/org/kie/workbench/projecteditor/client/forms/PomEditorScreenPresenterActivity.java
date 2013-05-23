/*
 * Copyright 2012 JBoss Inc
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

package org.kie.workbench.projecteditor.client.forms;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.uberfire.client.workbench.annotations.Identifier;
import org.uberfire.client.workbench.annotations.AssociatedResources;
import org.uberfire.client.workbench.annotations.Priority;
import org.uberfire.client.mvp.AbstractWorkbenchEditorActivity;
import org.uberfire.client.mvp.PlaceManager;

import org.uberfire.shared.mvp.PlaceRequest;

import org.uberfire.client.workbench.widgets.menu.Menus;

import org.uberfire.backend.vfs.Path;

import com.google.gwt.user.client.ui.IsWidget;

@Dependent
@Generated("org.uberfire.annotations.processors.WorkbenchEditorProcessor")
@Identifier("pomScreen")
@AssociatedResources({
    org.kie.workbench.projecteditor.client.type.POMResourceType.class
})

@Priority(0)
/*
 * WARNING! This class is generated. Do not modify.
 */
public class PomEditorScreenPresenterActivity extends AbstractWorkbenchEditorActivity {

    private static final Collection<String> ROLES = Collections.emptyList();

    private static final Collection<String> TRAITS = Collections.emptyList();

    @Inject
    private PomEditorScreenPresenter realPresenter;

    @Inject
    //Constructor injection for testing
    public PomEditorScreenPresenterActivity(final PlaceManager placeManager) {
        super( placeManager );
    }

    @Override
    public void onStart(final Path path, 
                        final PlaceRequest place) {
        super.onStart( path, place );
        realPresenter.init( path, place );
    }

    @Override
    public String getTitle() {
        return realPresenter.getTitle();
    }

    @Override
    public IsWidget getWidget() {
        return realPresenter.asWidget();
    }
    
    @Override
    public Menus getMenus() {
        return realPresenter.buildMenuBar();
    }
    
    @Override
    public Collection<String> getRoles() {
        return ROLES;
    }

    @Override
    public Collection<String> getTraits() {
        return TRAITS;
    }

    @Override
    public String getSignatureId() {
        return "org.kie.workbench.projecteditor.client.forms.PomEditorScreenPresenterActivity";
    }
}
