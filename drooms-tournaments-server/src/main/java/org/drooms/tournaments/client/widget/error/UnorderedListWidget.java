package org.drooms.tournaments.client.widget.error;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class UnorderedListWidget extends ComplexPanel {
    public UnorderedListWidget() {
        setElement(Document.get().createULElement());
    }

    @Override
    public void add(Widget child) {
        super.add(child, getElement());
    }
}
