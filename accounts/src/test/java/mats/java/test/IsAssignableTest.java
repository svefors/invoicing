package mats.java.test;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 9/15/12
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class IsAssignableTest {

    class Foo {

    }

    class Bar {

    }

    @Test
    public void testNullIsAssignable() {
        // Checks the type of the value
        Foo foo = null;

        Object newValue = foo;

        Bar value = null;

        Class<Bar> type = Bar.class;

        if (newValue != null && !type.isAssignableFrom(newValue.getClass())) {
            throw new IllegalArgumentException("Invalid value type "
                    + newValue.getClass().getName()
                    + " for ObjectProperty of type " + type.getName() + ".");
        }
        // the cast is safe after an isAssignableFrom check
        value = (Bar) newValue;
    }
}
