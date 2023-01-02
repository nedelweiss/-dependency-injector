package dependency_injector.test_package;

import dependency_injector.custom_annotations.Component;
import dependency_injector.custom_annotations.Inject;
import dependency_injector.test_package.sub_package.ClassB;
import dependency_injector.test_package.sub_package.ClassE;

@Component
public class ClassA {

    @Inject
    private ClassD classD;
    @Inject
    private ClassB classB;
    private ClassC classC;
    private ClassE classE;

    @Inject
    public ClassA(ClassC classC) {
        this.classC = classC;
    }

    @Inject
    public ClassA(ClassC classC, ClassE classE) {

    }

    public String getClassC() {
        return classC.getName();
    }
}
