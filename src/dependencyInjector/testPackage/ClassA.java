package dependencyInjector.testPackage;

import dependencyInjector.customAnnotations.Component;
import dependencyInjector.customAnnotations.Inject;
import dependencyInjector.testPackage.subPackage.ClassB;
import dependencyInjector.testPackage.subPackage.ClassE;

@Component
public class ClassA {
    @Inject
    private ClassD classD;
    @Inject
    private ClassB classB;

    private ClassC classC;
    private ClassC classE;

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
