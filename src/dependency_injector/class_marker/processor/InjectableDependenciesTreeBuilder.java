package dependency_injector.class_marker.processor;

import dependency_injector.utils.TreeNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InjectableDependenciesTreeBuilder {

    private final Logger LOGGER = Logger.getLogger(InjectableDependenciesTreeBuilder.class.getName());

    private final ComponentProcessor componentProcessor;
    private final InjectProcessor injectProcessor;

    public InjectableDependenciesTreeBuilder(ComponentProcessor componentProcessor, InjectProcessor injectProcessor) {
        this.componentProcessor = componentProcessor;
        this.injectProcessor = injectProcessor;
    }

    public List<TreeNode<InjectableMetadata>> buildInjectableFieldsTree(final List<Class<?>> classesToInject) {
        final List<TreeNode<InjectableMetadata>> tree = new ArrayList<>();
        for (Class<?> classToInject : classesToInject) {
            try {
                if (componentProcessor.isComponent(classToInject)) {
                    tree.add(buildInjectableFieldsTree(classToInject, null));
                }
            } catch (Exception e) {
                LOGGER.info("Couldn't handle class: " + classesToInject);
            }
        }
        return tree;
    }

    private TreeNode<InjectableMetadata> buildInjectableFieldsTree(
        final Class<?> classToInject,
        final Field fieldToInject
    ) {
        TreeNode<InjectableMetadata> root = new TreeNode<>(new InjectableMetadata(fieldToInject, classToInject));

        List<Field> withAnnotation = injectProcessor.getWithAnnotation(classToInject.getDeclaredFields());
        for (Field field : withAnnotation) {
            Class<?> fieldClass = field.getType();
            try {
                if (componentProcessor.isComponent(classToInject)) {
                    root.addChild(buildInjectableFieldsTree(fieldClass, field));
                }
            } catch (Exception e) {
                LOGGER.info("Couldn't handle the next field: " + fieldClass);
            }
        }

        LOGGER.info("Injectable fields have been collected");
        return root;
    }
}

