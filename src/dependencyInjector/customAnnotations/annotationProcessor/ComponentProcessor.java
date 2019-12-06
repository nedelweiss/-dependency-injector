package dependencyInjector.customAnnotations.annotationProcessor;

import dependencyInjector.customAnnotations.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ComponentProcessor {
    private InjectProcessor injectProcessor;

    public ComponentProcessor() {
        this.injectProcessor = new InjectProcessor();
    }

    public List<Object> process(List<Class<?>> classes) {
        List<Class<?>> collect = classes.stream()
                .filter(c -> Optional.ofNullable(c.getAnnotation(Component.class)).isPresent())
                .collect(Collectors.toList());

        return injectProcessor.inject(collect);
    }
}
