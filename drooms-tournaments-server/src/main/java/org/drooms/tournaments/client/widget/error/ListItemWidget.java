package org.drooms.tournaments.client.widget.error;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SimplePanel;

public class ListItemWidget extends SimplePanel implements HasText {
    public ListItemWidget() {
        super(Document.get().createLIElement());
    }

    public ListItemWidget(String text) {
        this();
        setText(text);
    }

    @Override
    public String getText() {
        return getElement().getInnerText();
    }

    @Override
    public void setText(String text) {
        getElement().setInnerText(text);
    }
}
