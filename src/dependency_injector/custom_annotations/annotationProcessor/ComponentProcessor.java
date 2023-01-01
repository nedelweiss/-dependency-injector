package dependency_injector.custom_annotations.annotationProcessor;

import dependency_injector.custom_annotations.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ComponentProcessor {
    public List<Class<?>> process(List<Class<?>> classes) {
        return classes.stream()
                .filter(c -> Optional.ofNullable(c.getAnnotation(Component.class)).isPresent())
                .collect(Collectors.toList());
    }
}
