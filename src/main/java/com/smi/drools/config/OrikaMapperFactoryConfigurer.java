package com.smi.drools.config;

import ma.glasnost.orika.MapperFactory;

public interface OrikaMapperFactoryConfigurer {

    /**
     * Configures the {@link MapperFactory}.
     *
     * @param orikaMapperFactory the {@link MapperFactory}.
     */
    void configure(MapperFactory orikaMapperFactory);

}