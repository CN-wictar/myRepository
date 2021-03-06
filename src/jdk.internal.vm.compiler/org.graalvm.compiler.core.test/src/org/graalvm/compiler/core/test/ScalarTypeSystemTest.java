/*
 * Copyright (c) 2011, 2019, Oracle and/or its affiliates. All rights reserved.
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


package org.graalvm.compiler.core.test;

import org.graalvm.compiler.debug.DebugContext;
import org.graalvm.compiler.nodes.StructuredGraph;
import org.graalvm.compiler.nodes.StructuredGraph.AllowAssumptions;
import org.graalvm.compiler.nodes.spi.CoreProviders;
import org.junit.Test;

/**
 * In the following tests, the scalar type system of the compiler should be complete enough to see
 * the relation between the different conditions.
 */
public class ScalarTypeSystemTest extends GraalCompilerTest {

    public static int referenceSnippet1(int a) {
        if (a > 0) {
            return 1;
        } else {
            return 2;
        }
    }

    @Test(expected = AssertionError.class)
    public void test1() {
        test("test1Snippet", "referenceSnippet1");
    }

    public static int test1Snippet(int a) {
        if (a > 0) {
            if (a > -1) {
                return 1;
            } else {
                return 3;
            }
        } else {
            return 2;
        }
    }

    @Test(expected = AssertionError.class)
    public void test2() {
        test("test2Snippet", "referenceSnippet1");
    }

    public static int test2Snippet(int a) {
        if (a > 0) {
            if (a == -15) {
                return 3;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    @Test(expected = AssertionError.class)
    public void test3() {
        test("test3Snippet", "referenceSnippet2");
    }

    public static int referenceSnippet2(int a, int b) {
        if (a > b) {
            return 1;
        } else {
            return 2;
        }
    }

    public static int test3Snippet(int a, int b) {
        if (a > b) {
            if (a == b) {
                return 3;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    public static int referenceSnippet3(int a, int b) {
        if (a == b) {
            return 1;
        } else {
            return 2;
        }
    }

    @Test(expected = AssertionError.class)
    public void test6() {
        test("test6Snippet", "referenceSnippet3");
    }

    public static int test6Snippet(int a, int b) {
        if (a == b) {
            if (a == b + 1) {
                return 3;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    private void test(final String snippet, final String referenceSnippet) {
        // No debug scope to reduce console noise for @Test(expected = ...) tests
        StructuredGraph graph = parseEager(snippet, AllowAssumptions.NO);
        graph.getDebug().dump(DebugContext.BASIC_LEVEL, graph, "Graph");
        CoreProviders context = getProviders();
        createCanonicalizerPhase().apply(graph, context);
        StructuredGraph referenceGraph = parseEager(referenceSnippet, AllowAssumptions.NO);
        assertEquals(referenceGraph, graph);
    }
}
