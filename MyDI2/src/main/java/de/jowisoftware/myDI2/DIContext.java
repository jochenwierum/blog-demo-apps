package de.jowisoftware.myDI2;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import de.jowisoftware.myDI2.annotations.Bean;
import de.jowisoftware.myDI2.scanner.FileScanner;
import de.jowisoftware.myDI2.scanner.JarScanner;

public class DIContext {
    private final List<Object> beans;

    public DIContext(final String... packages) {
        final List<Class<?>> classes = new LinkedList<>();

        final String[] roots = System.getProperty("java.class.path", ".")
                .split(File.pathSeparator);

        for (final String root : roots) {
            scanRoot(root, packages, classes);
        }

        beans = initializeBeans(classes);
    }

    private void scanRoot(final String root, final String[] packages,
            final List<Class<?>> classes) {
        final File rootFile = new File(root);

        if (rootFile.isDirectory()) {
            new FileScanner(rootFile, packages).scan(classes);
        } else if (rootFile.isFile() && rootFile.getName().endsWith(".jar")) {
            new JarScanner(rootFile, packages).scan(classes);
        } else {
            System.err.println("Warning: cannot scan " + rootFile);
        }
    }

    private List<Object> initializeBeans(final List<Class<?>> classes) {
        final List<Object> result = firstPassInit(classes);
        secondPassInit(result);
        return result;
    }

    private List<Object> firstPassInit(final List<Class<?>> classes) {
        final List<Object> result = new LinkedList<>();
        for (final Class<?> clazz : classes) {
            try {
                result.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public <T> T getBean(final Class<? extends T> clazz) {
        return getBean(clazz, beans);
    }

    private <T> T getBean(final Class<? extends T> clazz,
            final List<Object> beanList) {
        for (final Object bean : beanList) {
            if (clazz.isInstance(bean)) {
                @SuppressWarnings("unchecked")
                final T result = (T) bean;
                return result;
            }
        }
        return null;
    }

    private void secondPassInit(final List<Object> preInitBeans) {
        for (final Object bean : preInitBeans) {
            for (final Field field : bean.getClass().getDeclaredFields()) {
                if (field.getAnnotation(Bean.class) != null) {
                    insertBean(field, bean, preInitBeans);
                }
            }
        }
    }

    private void insertBean(final Field field, final Object bean,
            final List<Object> preInitBeans) {
        final Class<?> targetType = field.getType();
        final Object targetObject = getBean(targetType, preInitBeans);
        field.setAccessible(true);

        try {
            field.set(bean, targetObject);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
