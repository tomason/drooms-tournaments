package org.drooms.tournaments.client.widget.spinner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

@Templated
public class Spinner extends Composite {

    @Inject
    @DataField
    private TextBox input;

    @Inject
    @DataField
    private Button up;

    @Inject
    @DataField
    private Button down;

    private int value = 0;
    private Integer min;
    private Integer max;
    private int step = 1;

    @PostConstruct
    public void init() {
        setValue(0);
    }

    public void setup(Integer value, Integer min, Integer max, Integer step) {
        this.step = (step != null && step > 0) ? step : 1;

        this.min = min;
        this.max = max;
        if (min != null && max != null && min > max) {
            this.min = null;
            this.max = null;
        }
        setValue(value != null ? value : this.value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (min != null && value <= min) {
            value = min;
            down.setEnabled(false);
        } else {
            down.setEnabled(true);
        }
        if (max != null && value >= max) {
            value = max;
            up.setEnabled(false);
        } else {
            up.setEnabled(true);
        }

        this.value = value;
        input.setValue(Integer.toString(value));
    }

    @EventHandler("input")
    public void inputChanged(ChangeEvent event) {
        try {
            setValue(Integer.parseInt(input.getValue()));
        } catch (NumberFormatException ex) {
            input.setValue(Integer.toString(getValue()));
        }
    }

    @EventHandler("up")
    public void upClicked(ClickEvent event) {
        setValue(value + step);
    }

    @EventHandler("down")
    public void downClicked(ClickEvent event) {
        setValue(value - step);
    }

    public void setEnabled(boolean enabled) {
        input.setEnabled(enabled);
        up.setVisible(enabled);
        down.setVisible(enabled);
    }
}
