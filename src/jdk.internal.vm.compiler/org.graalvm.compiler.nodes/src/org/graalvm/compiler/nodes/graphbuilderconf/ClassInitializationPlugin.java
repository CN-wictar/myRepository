/*
 * Copyright (c) 2015, 2018, Oracle and/or its affiliates. All rights reserved.
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


package org.graalvm.compiler.nodes.graphbuilderconf;

import java.util.function.Supplier;

import org.graalvm.compiler.nodes.FrameState;
import org.graalvm.compiler.nodes.ValueNode;

import jdk.vm.ci.meta.ConstantPool;
import jdk.vm.ci.meta.ResolvedJavaType;

/**
 * Plugin for emitting a class initialization barrier (i.e., initializes a class if it's not already
 * initialized).
 *
 * This plugin also supports separating class resolution from class initialization with
 * {@link #supportsLazyInitialization(ConstantPool)} and
 * {@link #loadReferencedType(GraphBuilderContext, ConstantPool, int, int)}.
 *
 * @see "https://bugs.openjdk.java.net/browse/JDK-8146201"
 */
public interface ClassInitializationPlugin extends GraphBuilderPlugin {

    /**
     * Emits a class initialization barrier for {@code type}.
     *
     * @param frameState supplier to create a frame state representing the state just prior to
     *            execution of the class initialization barrier
     * @param classInit if non-null, the node representing the class initialization barrier should
     *            be returned in element 0 of this array
     * @return {@code true} if this method emitted a barrier,{@code false} if not
     */
    boolean apply(GraphBuilderContext builder, ResolvedJavaType type, Supplier<FrameState> frameState, ValueNode[] classInit);

    /**
     * Emits a class initialization barrier for {@code type}.
     *
     * @param frameState supplier to create a frame state representing the state just prior to
     *            execution of the class initialization barrier
     * @return {@code true} if this method emitted a barrier,{@code false} if not
     */
    default boolean apply(GraphBuilderContext builder, ResolvedJavaType type, Supplier<FrameState> frameState) {
        return apply(builder, type, frameState, null);
    }

    /**
     * Determines if {@code cp} has a variation of {@link ConstantPool#loadReferencedType} that can
     * resolved a type without initializing it.
     */
    boolean supportsLazyInitialization(ConstantPool cp);

    /**
     * Ensures that the type referenced by the constant pool entry specified by {@code cp} and
     * {@code cpi} is loaded. If {@code cp} does not support
     * {@linkplain #supportsLazyInitialization(ConstantPool) lazy} initialization, then the type is
     * initialized after resolution.
     *
     * @param cpi the index of the constant pool entry that references the type
     * @param bytecode the opcode of the instruction that references the type
     */
    void loadReferencedType(GraphBuilderContext builder, ConstantPool cp, int cpi, int bytecode);
}
