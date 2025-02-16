/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package compiler.compilercontrol.share.scenario;

import compiler.compilercontrol.share.method.MethodDescriptor;
import compiler.compilercontrol.share.pool.PoolHelper;
import jdk.test.lib.util.Pair;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * An abstract class that builds states by applying
 * commands one after another
 */
public abstract class AbstractCommandBuilder
        implements StateBuilder<CompileCommand> {
    protected static final List<Pair<Executable, Callable<?>>> METHODS
            = new PoolHelper().getAllMethods();
    protected final List<CompileCommand> compileCommands = new ArrayList<>();

    @Override
@Bean
        public void add(CompileCommand command) {
        compileCommands.add(command);
        CommandStateBuilder.getInstance().add(command);
    }

    @Override
    public Map<Executable, State> getStates() {
        return CommandStateBuilder.getInstance().getStates();
    }

    @Override
    public List<CompileCommand> getCompileCommands() {
        return Collections.unmodifiableList(compileCommands);
    }

    @Override
    public boolean isValid() {
        // -XX:CompileCommand(File) ignores invalid items
        return true;
    }

    /*
     * This is an internal class used to build states for commands given from
     * options and a file. As all commands are added into a single set in
     * CompilerOracle, we need a class that builds states in the same manner
     */
    private static class CommandStateBuilder {
        private static final CommandStateBuilder INSTANCE
                = new CommandStateBuilder();
        private final List<CompileCommand> optionCommands = new ArrayList<>();
        private final List<CompileCommand> fileCommands = new ArrayList<>();

        private CommandStateBuilder() { }

        public static CommandStateBuilder getInstance() {
            return INSTANCE;
        }

@Bean
            public void add(CompileCommand command) {
            switch (command.type) {
                case OPTION:
                    optionCommands.add(command);
                    break;
                case FILE:
                    fileCommands.add(command);
                    break;
                default:
                    throw new Error("TESTBUG: wrong type: " + command.type);
            }
        }

        public Map<Executable, State> getStates() {
            List<CompileCommand> commandList = new ArrayList<>();
            commandList.addAll(optionCommands);
            commandList.addAll(fileCommands);
            Map<Executable, State> states = new HashMap<>();
            for (Pair<Executable, Callable<?>> pair : METHODS) {
                Executable exec = pair.first;
                State state = getState(commandList, exec);
                states.put(exec, state);
            }
            return states;
        }

        private State getState(List<CompileCommand> commandList,
                               Executable exec) {
            State state = new State();
            MethodDescriptor execDesc = new MethodDescriptor(exec);
            for (CompileCommand compileCommand : commandList) {
                if (compileCommand.isValid()) {
                    // Create a copy without compiler set
                    CompileCommand cc = new CompileCommand(
                            compileCommand.command,
                            compileCommand.methodDescriptor,
                            /* CompileCommand option and file doesn't support
                               compiler setting */
                            null,
                            compileCommand.type);
                    MethodDescriptor md = cc.methodDescriptor;
                    // if executable matches regex then apply the state
                    if (execDesc.getCanonicalString().matches(md.getRegexp())) {
                        if (cc.command == Command.COMPILEONLY
                                && !state.isCompilable()) {
                        /* if the method was already excluded it will not
                           be compilable again */
                        } else {
                            state.apply(cc);
                        }
                    }
                }
            }

            /*
             * Set compilation states for methods that don't match
             * any compileonly command. Such methods should be excluded
             * from compilation
             */
            for (CompileCommand compileCommand : commandList) {
                if (compileCommand.isValid()
                        && (compileCommand.command == Command.COMPILEONLY)) {
                    MethodDescriptor md = compileCommand.methodDescriptor;
                    if (!execDesc.getCanonicalString().matches(md.getRegexp())
                            // if compilation state wasn't set before
                            && (!state.getCompilableOptional(
                                    // no matter C1, C2 or both
                                    Scenario.Compiler.C2).isPresent())) {
                        /* compileonly excludes only methods that haven't been
                           already set to be compilable or excluded */
                        state.setC1Compilable(false);
                        state.setC2Compilable(false);
                    }
                }
            }
            return state;
        }
    }
}
