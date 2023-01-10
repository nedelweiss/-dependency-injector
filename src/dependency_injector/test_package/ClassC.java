package dependency_injector.test_package;

import dependency_injector.class_marker.Component;

@Component
public class ClassC {

    private final String name;

    public ClassC() {
        this.name = "";
    }

    public String getName() {
        return name;
    }
}
