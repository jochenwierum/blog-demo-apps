package de.jowisoftware.myDI;

public interface DIContext {

    Object getBean(String id);

    <T> T getBean(Class<? extends T> clazz);
}
