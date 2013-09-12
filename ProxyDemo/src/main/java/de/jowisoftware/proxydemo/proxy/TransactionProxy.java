package de.jowisoftware.proxydemo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.jowisoftware.proxydemo.database.Database;

public class TransactionProxy {
    private final Database database;

    public TransactionProxy(final Database database) {
        this.database = database;
    }

    public <T> T createProxy(final T object) {
        final Class<?> clazz = object.getClass();
        final Class<?>[] interfaces = clazz.getInterfaces();
        final ClassLoader classLoader = clazz.getClassLoader();

        @SuppressWarnings("unchecked")
        final T proxy = (T) Proxy.newProxyInstance(classLoader, interfaces,
                createInvocationHandler(object));

        return proxy;
    }

    private InvocationHandler createInvocationHandler(final Object target) {
        return new InvocationHandler() {

            @Override
            public Object invoke(final Object proxy, final Method method,
                    final Object[] arguments)
                    throws Throwable {

                database.beginTransaction();

                final Object result;
                try {
                    result = method.invoke(target, arguments);
                } catch (final InvocationTargetException e) {
                    database.rollbackTransaction();
                    throw e.getCause();
                }

                database.commitTransaction();
                return result;
            }
        };
    }
}
