package dependencyInjector.customAnnotations.annotationProcessor;

import java.util.HashMap;
import java.util.Map;

public enum InstanceSingleton {
    INSTANCE;

    Map<Class, Object> instances = new HashMap<>();

    public <T, C, O> T getInstance(C C, O o) {
        // TODO
        return null;
    }

}
