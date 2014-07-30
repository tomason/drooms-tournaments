package org.drooms.tournaments.client.widget.playground.editor;

import javax.annotation.PostConstruct;

import org.drooms.tournaments.client.model.CanvasEventHandler;
import org.drooms.tournaments.client.model.Coordinates;
import org.drooms.tournaments.client.model.PlaygroundModel;
import org.drooms.tournaments.client.model.PortalCell;
import org.drooms.tournaments.client.util.Form;
import org.drooms.tournaments.client.util.FormMode;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.vectomatic.dom.svg.OMSVGElement;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;

// TODO tooltips on the menu
@Templated("PlaygroundEditor.html#root")
public class PlaygroundEditor extends Composite implements Form<String> {
    private final PlaygroundModel model = new PlaygroundModel();

    @DataField
    private MenuBar menu = new MenuBar();

    @DataField
    private Element canvas = model.getSvgElement();

    private boolean dragging = false;
    private Tool selectedTool = Tool.NONE;

    @PostConstruct
    public void init() {
        menu.addItem("Grid", new ScheduledCommand() {
            @Override
            public void execute() {
                model.toggleGridVisibility();
            }
        });
        menu.addItem("Portals", new ScheduledCommand() {
            @Override
            public void execute() {
                model.togglePortalLinesVisibility();
            }
        });
        menu.addSeparator();
    }

    @Override
    public void setMode(FormMode mode) {
        if (mode == FormMode.NEW) {
            menu.addItem("Empty space", new ScheduledCommand() {
                @Override
                public void execute() {
                    switchTool(Tool.SPACE);
                }
            });
            menu.addItem("Wall", new ScheduledCommand() {
                @Override
                public void execute() {
                    switchTool(Tool.WALL);
                }
            });
            menu.addItem("Start position", new ScheduledCommand() {
                @Override
                public void execute() {
                    switchTool(Tool.START);
                }
            });
            menu.addItem("Portal", new ScheduledCommand() {
                @Override
                public void execute() {
                    switchTool(Tool.PORTAL);
                }
            });
            menu.addSeparator();
            menu.addItem("h+", new ScheduledCommand() {
                @Override
                public void execute() {
                    model.setHeight(model.getHeight() + 1);
                }
            });
            menu.addItem("h-", new ScheduledCommand() {
                @Override
                public void execute() {
                    if (model.getHeight() > 0) {
                        model.setHeight(model.getHeight() - 1);
                    }
                }
            });
            menu.addItem("w+", new ScheduledCommand() {
                @Override
                public void execute() {
                    model.setWidth(model.getWidth() + 1);
                }
            });
            menu.addItem("w-", new ScheduledCommand() {
                @Override
                public void execute() {
                    if (model.getWidth() > 0) {
                        model.setWidth(model.getWidth() - 1);
                    }
                }
            });
            model.registerEventHandler(new CanvasEventHandler() {
                @Override
                public void onCanvasMouseDown(MouseDownEvent event, Coordinates coordinates) {
                    if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
                        dragging = true;
                        draw(coordinates);
                        if (selectedTool == Tool.PORTAL) {
                            portalMouseDown(coordinates);
                        }
                    }
                }

                @Override
                public void onCanvasMouseUp(MouseUpEvent event, Coordinates coordinates) {
                    dragging = false;
                }

                @Override
                public void onCanvasMouseMove(MouseMoveEvent event, Coordinates coordinates) {
                    if (dragging && event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
                        draw(coordinates);
                    }
                    if (selectedTool == Tool.PORTAL) {
                        portalMouseMove(coordinates);
                    }
                }
            });
        }
    }

    public void setValue(String value) {
        model.setSource(value);
    }

    public String getValue() {
        return model.getSource();
    }

    private enum Tool {
        NONE, SPACE, WALL, START, PORTAL
    }

    private void draw(Coordinates coordinates) {
        switch (selectedTool) {
        case SPACE:
            model.setEmpty(coordinates);
            break;
        case WALL:
            model.setWall(coordinates);
            break;
        case START:
            model.setStart(coordinates);
            break;
        default:
            break;
        }
    }

    private Coordinates newPortalStart;
    private OMSVGElement newPortalStartElement;
    private OMSVGElement newPortalLine;
    private OMSVGElement newPortalEndElement;

    private void portalMouseDown(Coordinates coordinates) {
        if (newPortalStartElement == null) {
            // starting new portal
            newPortalStart = coordinates;
            newPortalStartElement = PortalCell.newPortalElement(model.getDocument(), newPortalStart);
            model.addTempElement(newPortalStartElement);
        } else {
            // ending the portal
            clearTempElements();
            model.setPortal(newPortalStart, coordinates);
        }
    }

    private void portalMouseMove(Coordinates coordinates) {
        if (newPortalStartElement != null && !newPortalStart.equals(coordinates)) {
            if (newPortalEndElement != null) {
                model.removeTempElement(newPortalEndElement);
            }
            if (newPortalLine != null) {
                model.removeTempElement(newPortalLine);
            }

            newPortalLine = PortalCell.newPortalLine(model.getDocument(), newPortalStart, coordinates);
            newPortalEndElement = PortalCell.newPortalElement(model.getDocument(), coordinates);

            model.addTempElement(newPortalLine);
            model.addTempElement(newPortalEndElement);
        }
    }

    private void switchTool(Tool tool) {
        selectedTool = tool;
        clearTempElements();
    }

    private void clearTempElements() {
        if (newPortalStartElement != null) {
            model.removeTempElement(newPortalStartElement);
            newPortalStartElement = null;
        }
        if (newPortalEndElement != null) {
            model.removeTempElement(newPortalEndElement);
            newPortalEndElement = null;
        }
        if (newPortalLine != null) {
            model.removeTempElement(newPortalLine);
            newPortalLine = null;
        }
    }
}
