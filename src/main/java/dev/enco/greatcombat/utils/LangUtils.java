package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.versions.LangType;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * Utility class for handling item name translations using LangHelper library.
 */
@UtilityClass
public class LangUtils {
    private static boolean USE_LANG_HELPER;
    /** The TranslationManager instance */
    private Object manager;

    /** The selected LangType */
    private Object type;

    /** Reflection method for getting item names */
    private Method getMethod;

    /**
     * Initializes the translation system using LangHelper.
     */
    public void setup() {
        Locale locale = ConfigManager.getLocale();
        FileConfiguration config = ConfigManager.getMainConfig();
        USE_LANG_HELPER = config.getBoolean("use-lang-helper");
        if (!USE_LANG_HELPER) return;
        try {
            String lang = config.getString("helper-lang");
            manager = LangHelper.getInstance().getTranslateManager();
            type = LangType.valueOf(config.getString("helper-lang"));
            getMethod = manager.getClass().getMethod("getItemName", ItemStack.class, LangType.class);
            Logger.info(MessageFormat.format(locale.langSuccess(), lang));
        } catch (Exception e) {
            Logger.warn(locale.langError() + e);
            USE_LANG_HELPER = false;
        }
    }

    /**
     * Gets the translated name for an item.
     *
     * @param translation the custom translation string (can be null or empty)
     * @param item the ItemStack to translate
     * @return the translated item name, or custom translation if provided
     */
    public String getTranslation(String translation, ItemStack item) {
        if (!USE_LANG_HELPER) return translation;
        if (translation != null && !translation.isEmpty()) return translation;
        try {
            return (String) getMethod.invoke(manager, item, type);
        } catch (Exception ignored) { return translation; }
    }

    /**
     * Disables LangHelper plugin if needed.
     *
     * @param bool if true, forces shutdown LangHelper plugin
     */
    public void shutdown(boolean bool) {
        if (USE_LANG_HELPER && bool)
            Bukkit.getPluginManager().disablePlugin(LangHelper.getInstance());
    }
}
