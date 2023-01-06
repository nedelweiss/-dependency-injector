package dependency_injector.test_package;

import dependency_injector.custom_annotations.Component;

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
