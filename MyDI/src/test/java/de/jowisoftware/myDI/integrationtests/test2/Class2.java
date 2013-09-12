package de.jowisoftware.myDI.integrationtests.test2;

public class Class2 {
    private String prop;
    private Class1 bean;

    public String getProp() {
        return prop;
    }

    public void setProp(final String prop) {
        this.prop = prop;
    }

    public Class1 getBean() {
        return bean;
    }

    public void setBean(final Class1 bean) {
        this.bean = bean;
    }
}
