package de.jowisoftware.myDI.integrationtests.test3;

import de.jowisoftware.myDI.DIContext;
import de.jowisoftware.myDI.functions.ContextAware;
import de.jowisoftware.myDI.functions.Initializable;
import de.jowisoftware.myDI.functions.SelfAware;

public class Class1 implements Initializable, SelfAware, ContextAware {
    private int x;
    private int y;
    private int z;
    private String name;
    private Class1 thisBean;
    private String name2;

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    // Expectation: setName is called first
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    // Expectation: setContext is called after setName
    @Override
    public void setContext(final DIContext context) {
        this.thisBean = (Class1) context.getBean(name);
    }

    // Expectation: initialize is called last
    @Override
    public void initialize() {
        z = x + y;
        name2 = thisBean.name;
    }

    public String getName2() {
        return name2;
    }
}
