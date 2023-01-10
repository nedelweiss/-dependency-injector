package dependency_injector.class_marker.processor;

import dependency_injector.class_marker.Component;

import static java.util.Objects.nonNull;

public class ComponentProcessor {

    public Boolean isComponent(final Class<?> aClass) {
        return nonNull(aClass.getAnnotation(Component.class));
    }
}
