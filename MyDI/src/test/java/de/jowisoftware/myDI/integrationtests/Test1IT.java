package de.jowisoftware.myDI.integrationtests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jowisoftware.myDI.DIContext;
import de.jowisoftware.myDI.DIXmlContext;
import de.jowisoftware.myDI.integrationtests.test1.Class1;
import de.jowisoftware.myDI.integrationtests.test1.Class2;

/*
 * First goal: initialize Beans and apply simple values.
 * The test makes pretty clear what we want.
 *
 * See dependencies.test1.xml for the wiring setup.
 */

public class Test1IT {
    @Test
    public void contextIsInitializedCorrectly() {
        final DIContext context = new DIXmlContext(
                "/integrationTests/dependencies.test1.xml");

        final Object bean1 = context.getBean("bean1");
        final Object bean2 = context.getBean("bean2");

        assertThat(bean1, is(not(nullValue())));
        assertThat(bean2, is(not(nullValue())));
        assertThat(bean1, is(instanceOf(Class1.class)));
        assertThat(bean2, is(instanceOf(Class2.class)));

        assertThat(((Class1) bean1).getProp1(), is(equalTo("val1")));
        assertThat(((Class1) bean1).getProp2(), is(equalTo("val2")));
        assertThat(((Class2) bean2).getProp3(), is(equalTo(4)));
        assertThat(((Class2) bean2).getProp4(), is(equalTo(-5.4)));

        assertThat(context.getBean("bean1"), is(sameInstance(bean1)));
    }
}
