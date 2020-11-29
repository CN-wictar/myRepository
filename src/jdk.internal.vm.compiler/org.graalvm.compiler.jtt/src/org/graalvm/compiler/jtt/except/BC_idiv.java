/*
 * Copyright (c) 2007, 2018, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
/*
 */



package org.graalvm.compiler.jtt.except;

import org.junit.Test;

import org.graalvm.compiler.jtt.JTTTest;

public class BC_idiv extends JTTTest {

    public static int test(int a, int b) {
        return a / b;
    }

    @Test
    public void run0() throws Throwable {
        runTest("test", 1, 2);
    }

    @Test
    public void run1() throws Throwable {
        runTest("test", 11, 0);
    }

}
