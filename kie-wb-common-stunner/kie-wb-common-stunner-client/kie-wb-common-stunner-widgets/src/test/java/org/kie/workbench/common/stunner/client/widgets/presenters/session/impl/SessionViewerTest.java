/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.client.widgets.presenters.session.impl;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.client.widgets.presenters.AbstractCanvasHandlerViewerTest;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.SessionViewer;
import org.kie.workbench.common.stunner.client.widgets.views.WidgetWrapperView;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvas;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.controls.select.SelectionControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.zoom.ZoomControl;
import org.kie.workbench.common.stunner.core.client.command.CanvasCommandManager;
import org.kie.workbench.common.stunner.core.client.session.impl.AbstractClientReadOnlySession;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.mockito.Mock;
import org.uberfire.mvp.ParameterizedCommand;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class SessionViewerTest extends AbstractCanvasHandlerViewerTest {

    @Mock
    AbstractClientReadOnlySession session;
    @Mock
    SessionViewer.SessionViewerCallback<AbstractClientReadOnlySession, Diagram> callback;
    @Mock
    CanvasCommandManager<AbstractCanvasHandler> canvasCommandManager;
    @Mock
    ZoomControl<AbstractCanvas> zoomControl;
    @Mock
    SelectionControl<AbstractCanvasHandler, Element> selectionControl;
    @Mock
    WidgetWrapperView view;

    private SessionViewerImpl<AbstractClientReadOnlySession, AbstractCanvasHandler> tested;

    @Before
    public void setup() throws Exception {
        super.init();
        when(canvasHandler.getDiagram()).thenReturn(diagram);
        when(session.getCanvasHandler()).thenReturn(canvasHandler);
        when(session.getCanvas()).thenReturn(canvas);
        when(session.getZoomControl()).thenReturn(zoomControl);
        when(session.getSelectionControl()).thenReturn(selectionControl);
        this.tested = new SessionViewerImpl<AbstractClientReadOnlySession, AbstractCanvasHandler>(canvasCommandManager,
                                                                                                  view);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testOpen() {
        tested.open(session,
                    callback);
        assertEquals(session,
                     tested.getInstance());
        assertEquals(canvasHandler,
                     tested.getSessionHandler());
        assertEquals(diagram,
                     tested.getHandler().getDiagram());
        assertEquals(zoomControl,
                     tested.getDiagramViewer().getZoomControl());
        assertEquals(selectionControl,
                     tested.getDiagramViewer().getSelectionControl());
        verify(canvasHandler,
               times(1)).draw(eq(diagram),
                              any(ParameterizedCommand.class));
        verify(view,
               times(1)).setWidget(any(Widget.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testClear() {
        tested.open(session,
                    callback);
        tested.clear();
        verify(canvasHandler,
               times(1)).clear();
        verify(view,
               times(1)).clear();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDestroy() {
        tested.open(session,
                    callback);
        tested.destroy();
        assertNull(tested.getInstance());
        verify(canvasHandler,
               times(1)).destroy();
        verify(view,
               times(1)).clear();
    }
}
