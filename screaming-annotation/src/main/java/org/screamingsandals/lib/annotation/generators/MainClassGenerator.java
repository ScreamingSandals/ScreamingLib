package org.screamingsandals.lib.annotation.generators;

import org.screamingsandals.lib.annotation.utils.ServiceContainer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;

public interface MainClassGenerator {
    void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<ServiceContainer> autoInit) throws IOException;
}
