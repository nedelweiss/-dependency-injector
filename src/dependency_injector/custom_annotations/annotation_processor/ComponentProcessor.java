package dependency_injector.custom_annotations.annotation_processor;

import dependency_injector.custom_annotations.Component;

import java.util.List;
import java.util.Optional;

public class ComponentProcessor {

    public List<Class<?>> process(final List<Class<?>> classes) {
        return classes.stream()
            .filter(c -> Optional.ofNullable(c.getAnnotation(Component.class)).isPresent())
            .toList();
    }
}
