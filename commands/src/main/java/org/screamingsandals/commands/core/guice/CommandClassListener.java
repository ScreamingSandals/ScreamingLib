package org.screamingsandals.commands.core.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

class CommandClassListener implements TypeListener {

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        encounter.register((InjectionListener<I>) listener -> {
            if (type.getRawType().isAssignableFrom(CommandClass.class)) {
                CommandClass commandClass = (CommandClass) listener;
                commandClass.register();
            }
        });
    }
}
