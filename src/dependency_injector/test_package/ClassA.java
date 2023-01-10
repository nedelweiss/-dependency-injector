package dependency_injector.test_package;

import dependency_injector.class_marker.Component;
import dependency_injector.class_marker.Inject;
import dependency_injector.test_package.sub_package.ClassB;
import dependency_injector.test_package.sub_package.ClassE;

@Component
public class ClassA {

    @Inject
    private ClassD classD;

    @Inject
    private ClassB classB;

    @Inject
    private ClassE classE;

    private ClassC classC;

    @Inject
    public ClassA(ClassC classC) {

    }

    @Inject
    public ClassA(ClassC classC, ClassE classE) {

    }
}
