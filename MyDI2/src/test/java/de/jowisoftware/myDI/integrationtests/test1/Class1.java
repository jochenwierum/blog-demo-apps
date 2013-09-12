package de.jowisoftware.myDI.integrationtests.test1;

import de.jowisoftware.myDI2.annotations.Resource;

@Resource
public class Class1 {

    public void greet() {
        System.err.println("YEHS!");
    }

}
