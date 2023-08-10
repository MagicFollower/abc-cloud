package com.abc.system.apollo.context;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

/**
 * EnvironmentChangeEvent
 *
 * @Description EnvironmentChangeEvent
 * @Author Trivis
 * @Date 2023/5/12 20:45
 * @Version 1.0
 */
@Getter
public class EnvironmentChangeEvent extends ApplicationEvent {

    private final Set<String> keys;

    public EnvironmentChangeEvent(Set<String> keys) {
        this(keys, keys);
    }

    public EnvironmentChangeEvent(Object context, Set<String> keys) {
        super(context);
        this.keys = keys;
    }
}

