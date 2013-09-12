package de.jowisoftware.myDI.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import de.jowisoftware.myDI.exceptions.DIInitializationException;
import de.jowisoftware.myDI.model.Tbean;
import de.jowisoftware.myDI.model.Tproperty;

public class BeanInitializer {
    private final Map<String, Object> beans;

    public BeanInitializer(final Map<String, Object> beans) {
        this.beans = beans;
    }

    public void initializeBean(final Tbean bean) {
        for (final Tproperty property : bean.getProperty()) {
            checkProperty(property, bean.getId());

            final Object beanObject = beans.get(bean.getId());

            final Method setterMethod = findSetter(beanObject,
                    property.getName(), bean.getId());

            final Object value = getPropertyValue(property, setterMethod,
                    bean.getId());

            setValue(beanObject, setterMethod, value, bean.getId());
        }
    }

    private void setValue(final Object beanObject,
            final Method setterMethod, final Object value, final String id) {
        try {
            setterMethod.invoke(beanObject, value);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new DIInitializationException("Could not set [" + value
                    + "] in " + setterMethod.toString() + " in bean "
                    + id);
        }
    }

    private Object getPropertyValue(final Tproperty property,
            final Method setterMethod, final String id) {
        final String ref = getRefById(property);
        final Object value;
        if (ref != null) {
            value = beans.get(ref);
        } else {
            value = createValue(setterMethod, property.getValue(), id,
                    property.getName());
        }
        return value;
    }

    private Object createValue(final Method setterMethod, final String value,
            final String id, final String property) {
        final Class<?> type = setterMethod.getParameterTypes()[0];
        try {
            if (type.equals(String.class)) {
                return value;
            } else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
                return Integer.parseInt(value);
            } else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
                return Float.parseFloat(value);
            } else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
                return Double.parseDouble(value);
            } else if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
                return Byte.parseByte(value);
            } else if (type.equals(Short.TYPE) || type.equals(Short.class)) {
                return Short.parseShort(value);
            } else {
                throw new DIInitializationException(
                        "Don't know how to transform '" + value + "' into "
                                + type.getSimpleName() + " in bean " + id
                                + ": " + property);
            }
        } catch (final IllegalArgumentException e) {
            throw new DIInitializationException("Could not use '" + value
                    + "' as value of type " + type.getSimpleName()
                    + " in bean " + id, e);
        }
    }

    private Method findSetter(final Object beanObject, final String name, final String id) {
        final String upperCaseName = name.substring(0, 1).toUpperCase()
                + name.substring(1);
        final String methodName = "set" + upperCaseName;

        for (final Method method : beanObject.getClass().getMethods()) {
            if (method.getName().equals(methodName)
                    && method.getParameterTypes().length == 1
                    && method.getReturnType().equals(Void.TYPE)) {
                return method;
            }
        }
        throw new DIInitializationException("Could not find method "
                + methodName + " in bean " + id);
    }

    private String getRefById(final Tproperty property) {
        final Tbean bean = (Tbean) property.getRef();
        if (bean != null) {
            return bean.getId();
        } else {
            return null;
        }
    }

    private void checkProperty(final Tproperty property, final String id) {
        if (property.getRef() != null && property.getValue() != null) {
            throw new DIInitializationException("Error in bean " + id
                    + ": property " + property.getName()
                    + ": either ref (" + ((Tbean) property.getRef()).getId()
                    + ") or property ('" + property.getValue()
                    + "') must not be defined");
        } else if (property.getRef() == null && property.getValue()==null) {
            throw new DIInitializationException("Error in bean " + id
                    + ": property " + property.getName()
                    + ": either an existing ref or value must be defined");
        }
    }

}
