package de.jowisoftware.myDI;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXB;

import de.jowisoftware.myDI.exceptions.DIInitializationException;
import de.jowisoftware.myDI.exceptions.InvalidBeanException;
import de.jowisoftware.myDI.loader.BeanCreator;
import de.jowisoftware.myDI.loader.BeanFunctionCaller;
import de.jowisoftware.myDI.loader.BeanInitializer;
import de.jowisoftware.myDI.model.Beans;
import de.jowisoftware.myDI.model.Tbean;

public class DIXmlContext implements DIContext {
    private final Map<String, Object> beans = new HashMap<>();

    public DIXmlContext(final String xmlFile) {
        final Beans beansDef = loadXmlFile(xmlFile);
        initializeBeans(beansDef);
    }

    private Beans loadXmlFile(final String xmlFile) {
        try (final InputStream is = getClass().getResourceAsStream(xmlFile)) {
            return JAXB.unmarshal(is, Beans.class);
        } catch (final IOException e) {
            throw new DIInitializationException("Could not read xml file", e);
        }
    }

    private void initializeBeans(final Beans beansDef) {
        firstPassInit(beansDef);
        secondPassInit(beansDef);
        thirdPassInit(beans);
    }

    private void firstPassInit(final Beans beansDef) {
        final BeanCreator creator = new BeanCreator();
        for (final Tbean bean : beansDef.getBean()) {
            final String id = bean.getId();
            beans.put(id, creator.createBean(id, bean.getClazz()));
        }
    }

    private void secondPassInit(final Beans beansDef) {
        final BeanInitializer initializer = new BeanInitializer(beans);

        for (final Tbean bean : beansDef.getBean()) {
            initializer.initializeBean(bean);
        }
    }

    private void thirdPassInit(final Map<String, Object> beans2) {
        final BeanFunctionCaller caller = new BeanFunctionCaller();
        for (final Entry<String, Object> entry : beans.entrySet()) {
            caller.initialize(entry.getValue(), this, entry.getKey());
        }
    }

    @Override
    public Object getBean(final String id) {
        if (beans.containsKey(id)) {
            return beans.get(id);
        } else {
            throw new InvalidBeanException("Bean with id " + id
                    + " does not exist");
        }
    }

    @Override
    public <T> T getBean(final Class<? extends T> clazz) {
        Object result = null;
        for (final Object bean : beans.values()) {
            if (clazz.isInstance(bean)) {
                if (result == null) {
                    result = bean;
                } else {
                    throw new InvalidBeanException(
                            "Ambiguous class "
                                    + clazz.getName()
                                    + ": at least two beans implement this class");
                }
            }
        }

        if (result == null) {
            throw new InvalidBeanException("A bean which implements "
                    + clazz.getName() + " does not exist");
        }

        @SuppressWarnings("unchecked")
        final T castedResult = (T) result;
        return castedResult;
    }

}
