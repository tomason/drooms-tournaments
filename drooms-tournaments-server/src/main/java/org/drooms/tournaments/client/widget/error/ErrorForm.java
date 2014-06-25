package org.drooms.tournaments.client.widget.error;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.ui.Composite;

@Templated
public class ErrorForm extends Composite {
    @DataField
    private UnorderedListWidget errorList = new UnorderedListWidget();

    private List<String> errors = new ArrayList<String>();

    @PostConstruct
    public void init() {
        clear();
    }

    public void addError(String errorMessage) {
        errors.add(errorMessage);
        draw(errors.size() - 1);
    }

    public void clear() {
        errors.clear();
        draw();
    }

    public boolean isValid() {
        return errors.size() == 0;
    }

    private void draw() {
        clearWidgets();

        for (int index = 0; index < errors.size(); index++) {
            draw(index);
        }

        setVisible(!isValid());
    }

    private void draw(int index) {
        if (index >= 0 && index < errors.size() && index <= errorList.getWidgetCount()) {
            if (index == errorList.getWidgetCount()) {
                errorList.add(new ListItemWidget());
            }
            ListItemWidget item = (ListItemWidget) errorList.getWidget(index);
            item.setText(errors.get(index));
        }

        setVisible(!isValid());
    }

    private void clearWidgets() {
        while (errorList.getWidgetCount() > 0) {
            errorList.remove(0);
        }
    }
}
