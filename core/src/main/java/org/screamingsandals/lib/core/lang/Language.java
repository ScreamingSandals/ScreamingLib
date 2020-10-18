package org.screamingsandals.lib.core.lang;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

/**
 * @author ScreamingSandals team
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Language extends LanguageBase implements I {

    public Language(Object plugin) {
        super(plugin, null, null, "");
    }

    public Language(Object plugin, String globalLanguage) {
        super(plugin, globalLanguage, null, "");
    }

    public Language(Object plugin, String globalLanguage, String customPrefix) {
        super(plugin, globalLanguage, null, customPrefix);
    }

    public Language(Object plugin, File customDataFolder) {
        super(plugin, null, customDataFolder, "");
    }

    public Language(Object plugin, String globalLanguage, File customDataFolder, String customPrefix) {
        super(plugin, globalLanguage, customDataFolder, customPrefix);
    }
}
