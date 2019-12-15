package dependencyInjector.customAnnotations.annotationProcessor;

import dependencyInjector.customAnnotations.Component;

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
