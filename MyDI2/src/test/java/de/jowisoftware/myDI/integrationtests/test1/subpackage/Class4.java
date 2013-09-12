package de.jowisoftware.myDI.integrationtests.test1.subpackage;

import de.jowisoftware.myDI2.annotations.Resource;

@Resource
public class Class4 {
    public void greet() {
        System.err.println("Greeting from class 4");
    }
}
