package dependency_injector.test_package.sub_package;

import dependency_injector.class_marker.Component;
import dependency_injector.class_marker.Inject;

@Component
public class ClassE {

    @Inject
    private ClassF classF;
}
