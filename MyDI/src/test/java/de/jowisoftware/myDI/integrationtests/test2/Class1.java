package de.jowisoftware.myDI.integrationtests.test2;

public class Class1 {
    private String prop;
    private Class2 bean;

    public String getProp() {
        return prop;
    }

    public void setProp(final String prop) {
        this.prop = prop;
    }

    public Class2 getBean() {
        return bean;
    }

    public void setBean(Class2 bean) {
        this.bean = bean;
    }
}
