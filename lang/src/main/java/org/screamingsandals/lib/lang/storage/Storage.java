package org.screamingsandals.lib.lang.storage;

import lombok.Data;
import org.screamingsandals.lib.config.ConfigAdapter;
import org.screamingsandals.lib.lang.Language;
import org.screamingsandals.lib.lang.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Storage {
    private final ConfigAdapter configuration;
    private Storage fallbackStorage;
    private String languageCode;
    private String prefix;

    public Storage(ConfigAdapter configuration, String languageCode, Storage fallbackStorage) {
        this.configuration = configuration;
        this.languageCode = languageCode;
        this.fallbackStorage = fallbackStorage;
        this.prefix = getPrefixFromConfig();
    }

    public Storage(ConfigAdapter configuration, String languageCode) {
        this(configuration, languageCode, null);
    }

    public String getLanguageName() {
        return translate("language_name");
    }

    public List<String> translateList(String key, List<String> def, boolean prefix) {
        if (prefix) {
            return translateListWithPrefix(key, def);
        } else {
            return translateList(key, def);
        }
    }

    public List<String> translateList(String key, List<String> def) {
        if (configuration != null && configuration.get(key) != null) {
            List<String> toTranslate = configuration.getStringList(key);
            if (toTranslate != null) {
                return Utils.colorize(toTranslate);
            }
        } else if (fallbackStorage != null) {
            return fallbackStorage.translateList(key, def);
        } else if (def != null) {
            return Utils.colorize(def);
        }

        return Collections.singletonList(Utils.colorize("&c" + key));
    }

    public List<String> translateListWithPrefix(String key, List<String> def) {
        final List<String> translatedList = translateList(key, def);
        final List<String> toReturn = new ArrayList<>();
        final String prefix = getPrefix();

        for (String toTranslate : translatedList) {
            if (prefix != null && prefix.length() > 0) {
                toReturn.add(toTranslate + prefix + " ");
            }
        }
        return toReturn;
    }

    public String translate(String key, String def) {
        if (configuration != null && configuration.get(key) != null) {
            String toTranslate = configuration.getString(key);
            if (toTranslate != null) {
                return Utils.colorize(toTranslate);
            }
        } else if (fallbackStorage != null) {
            return fallbackStorage.translate(key, def);
        } else if (def != null) {
            return Utils.colorize(def);
        }

        return Utils.colorize("&c" + key);
    }


    public String translate(String key) {
        return translate(key, null);
    }

    public String translate(String key, String def, boolean prefix) {
        if (prefix) {
            return translateWithPrefix(key, def);
        } else {
            return translate(key, def);
        }
    }

    public String translate(String key, boolean prefix) {
        return translate(key, null, prefix);
    }

    public String translateWithPrefix(String key) {
        return translateWithPrefix(key, null);
    }

    public String translateWithPrefix(String key, String def) {
        String toReturn = "";
        String prefix = getPrefix();
        if (prefix != null && prefix.length() > 0) {
            toReturn += prefix + " ";
        }
        toReturn += translate(key, def);
        return toReturn;
    }

    private String getPrefixFromConfig() {
        if (Language.getInstance().getCustomPrefix() == null) {
            return Utils.colorize(configuration.getString("prefix"));
        } else {
            return Utils.colorize(Language.getInstance().getCustomPrefix());
        }
    }
}
