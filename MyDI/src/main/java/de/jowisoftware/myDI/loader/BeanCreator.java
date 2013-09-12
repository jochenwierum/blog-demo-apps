package de.jowisoftware.myDI.loader;

import de.jowisoftware.myDI.exceptions.DIInitializationException;

public class BeanCreator {
    public Object createBean(final String id, final String className) {
        try {
            final Class<?> classInstance = Class.forName(className);
            return classInstance.newInstance();
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            throw new DIInitializationException("Could not initialize bean "
                    + id + " of type " + className, e);
        }
    }
}
