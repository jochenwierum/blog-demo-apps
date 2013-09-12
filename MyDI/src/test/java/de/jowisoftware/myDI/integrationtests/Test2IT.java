package de.jowisoftware.myDI.integrationtests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jowisoftware.myDI.DIContext;
import de.jowisoftware.myDI.DIXmlContext;
import de.jowisoftware.myDI.integrationtests.test2.Class1;
import de.jowisoftware.myDI.integrationtests.test2.Class2;

/*
 * Second goal: initialize Beans and allow (even circular!) references.
 * All beans have to be dreaded as singleton.
 * Tip: first create the (empty) bean objects, wire things up in a second step.
 *
 * See dependencies.test2.xml for the wiring setup.
 */

public class Test2IT {
    @Test
    public void contextIsInitializedCorrectly() {
        final DIContext context = new DIXmlContext(
                "/integrationTests/dependencies.test2.xml");

        final Class1 bean1 = (Class1) context.getBean("bean1");
        final Class2 bean2 = (Class2) context.getBean("bean2");

        assertThat(bean1.getBean(), is(instanceOf(Class2.class)));
        assertThat(bean2.getBean(), is(instanceOf(Class1.class)));

        assertThat(bean1.getBean().getProp(), is(equalTo(bean2.getProp())));
        assertThat(bean2.getBean().getProp(), is(equalTo(bean1.getProp())));

        assertThat(bean1.getBean(), is(sameInstance(bean2)));
        assertThat(bean2.getBean(), is(sameInstance(bean1)));
    }
}
