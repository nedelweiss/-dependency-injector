package dependency_injector.test_package.sub_package;

import dependency_injector.custom_annotations.Component;
import dependency_injector.custom_annotations.Inject;

@Component
public class ClassG {

    @Inject
    public ClassE classE;

    @Inject
    public ClassB classB;
}
