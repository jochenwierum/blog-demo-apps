package de.jowisoftware.myDI.loader;

import de.jowisoftware.myDI.DIContext;
import de.jowisoftware.myDI.exceptions.DIInitializationException;
import de.jowisoftware.myDI.functions.ContextAware;
import de.jowisoftware.myDI.functions.Initializable;
import de.jowisoftware.myDI.functions.SelfAware;

public class BeanFunctionCaller {
    public void initialize(final Object bean, final DIContext context,
            final String id) {
        try {
            if (bean instanceof SelfAware) {
                ((SelfAware) bean).setName(id);
            }

            if (bean instanceof ContextAware) {
                ((ContextAware) bean).setContext(context);
            }

            if (bean instanceof Initializable) {
                ((Initializable) bean).initialize();
            }
        } catch (final RuntimeException e) {
            throw new DIInitializationException("Could not initialize bean "
                    + id, e);
        }
    }
}
