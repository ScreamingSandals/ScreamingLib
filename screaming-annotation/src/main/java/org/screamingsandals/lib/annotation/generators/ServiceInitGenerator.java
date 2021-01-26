package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

@RequiredArgsConstructor(staticName = "builder")
public class ServiceInitGenerator {
    private final MethodSpec.Builder methodSpec;
    private final Map<String, BiConsumer<StringBuilder, List<Object>>> initArguments = new HashMap<>() {
        {
            put("org.screamingsandals.lib.utils.Controllable", (statement, processedArguments) -> {
                statement.append("this.$N.child()");
                processedArguments.add("pluginControllable");
            });
            put("org.screamingsandals.lib.plugin.PluginContainer", (statement, processedArguments) -> {
                statement.append("this.$N");
                processedArguments.add("pluginContainer");
            });
            put("org.slf4j.Logger", (statement, processedArguments) -> {
                statement.append("$N");
                processedArguments.add("slf4jLogger");
            });
        }
    };

    public ServiceInitGenerator add(String name, BiConsumer<StringBuilder, List<Object>> initArgGen) {
        return add(List.of(name), initArgGen);
    }

    public ServiceInitGenerator add(List<String> names, BiConsumer<StringBuilder, List<Object>> initArgGen) {
        names.forEach(s -> initArguments.put(s, initArgGen));
        return this;
    }

    public void process(ServiceContainer serviceContainer) {
        var typeElement = serviceContainer.getService();
        var initMethod = typeElement.getEnclosedElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.METHOD && "init".equals(element.getSimpleName().toString()))
                .findFirst();

        if (initMethod.isEmpty()) {
            initMethod = typeElement.getEnclosedElements()
                    .stream()
                    .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                    .findFirst();
        }

        if (initMethod.isPresent()) {
            var method = (ExecutableElement) initMethod.get();
            var arguments = method.getParameters();
            var processedArguments = new ArrayList<>();
            var statement = new StringBuilder();
            if (method.getKind() == ElementKind.CONSTRUCTOR) {
                statement.append("new $T(");
            } else {
                statement.append("$T.init(");
            }
            processedArguments.add(typeElement);
            var first = new AtomicBoolean(true);
            arguments.forEach(variableElement -> {
                if (!first.get()) {
                    statement.append(",");
                } else {
                    first.set(false);
                }
                if (initArguments.containsKey(variableElement.asType().toString())) {
                    initArguments.get(variableElement.asType().toString()).accept(statement, processedArguments);
                } else {
                    throw new UnsupportedOperationException("Init method of " + typeElement.getQualifiedName() + " has wrong argument!");
                }
            });
            statement.append(")");
            methodSpec.addStatement(statement.toString(), processedArguments.toArray());
        } else {
            throw new UnsupportedOperationException("Can't auto initialize " + typeElement.getQualifiedName() + " without init method");
        }
    }
}
