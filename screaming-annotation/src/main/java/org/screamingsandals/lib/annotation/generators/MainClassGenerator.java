package org.screamingsandals.lib.annotation.generators;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;

public interface MainClassGenerator {
    void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<TypeElement> autoInit) throws IOException;
}
