package com.smi.drools.config;

import ma.glasnost.orika.impl.DefaultMapperFactory.MapperFactoryBuilder;

public interface OrikaMapperFactoryBuilderConfigurer {

    /**
     * Configures the {@link MapperFactoryBuilder}.
     *
     * @param orikaMapperFactoryBuilder the {@link MapperFactoryBuilder}.
     */
    void configure(MapperFactoryBuilder<?, ?> orikaMapperFactoryBuilder);

}