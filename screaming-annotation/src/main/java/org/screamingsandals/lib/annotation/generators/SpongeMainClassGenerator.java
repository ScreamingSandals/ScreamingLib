package org.screamingsandals.lib.annotation.generators;

import org.screamingsandals.lib.annotation.utils.ServiceContainer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;

public class SpongeMainClassGenerator extends MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<ServiceContainer> autoInit) {

    }
}
