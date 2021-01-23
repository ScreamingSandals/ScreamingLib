package org.screamingsandals.lib.annotation.generators;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;

public class MinestomMainClassGenerator implements MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<TypeElement> autoInit) {

    }
}
