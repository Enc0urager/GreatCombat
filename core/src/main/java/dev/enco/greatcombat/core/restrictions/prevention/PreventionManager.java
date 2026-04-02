package dev.enco.greatcombat.core.restrictions.prevention;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IMetaManager;
import dev.enco.greatcombat.api.managers.IPreventionManager;
import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.PreventionType;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.restrictions.CheckerHandle;
import dev.enco.greatcombat.core.restrictions.WrappedItem;
import dev.enco.greatcombat.core.utils.EnumUtils;
import dev.enco.greatcombat.core.utils.ItemUtils;
import dev.enco.greatcombat.core.utils.LangUtils;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;
import dev.enco.greatcombat.core.utils.logger.Logger;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Singleton
public class PreventionManager implements IPreventionManager {
    private IPreventableItem[] preventableItems = new IPreventableItem[0];
    private final IMetaManager metaManager;

    @Inject
    public PreventionManager(IMetaManager metaManager, ConfigManager configManager) {
        this.metaManager = metaManager;
        load(configManager);
    }

    @Override
    public IPreventableItem getPreventableItem(ItemStack itemStack) {
        var wrapped = WrappedItem.wrap(itemStack);
        return getPreventableItem(wrapped);
    }

    @Override
    public IPreventableItem getPreventableItem(IWrappedItem wrapped) {
        for (IPreventableItem item : preventableItems)
            if (metaManager.isSimilar(item.wrappedItem(), wrapped, item.checkedMetas()))
                return item;
        return null;
    }

    public void load(ConfigManager configManager) {
        var section = configManager.getMainConfig().getConfigurationSection("preventable-items");
        final var locale = configManager.getLocale();
        List<PreventableItem> itemsList = new ArrayList<>();
        for (var key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);

            var handlers = new HashSet<>(itemSection.getStringList("handlers"));

            List<String> metaKeys = itemSection.getStringList("checked-meta");
            int size = metaKeys.size();
            CheckerHandle[] handles = new CheckerHandle[size];

            for (int i = 0; i < size; i++) {
                handles[i] = (CheckerHandle) metaManager.getByID(metaKeys.get(i));
            }

            var types = EnumUtils.toEnumSet(
                    itemSection.getStringList("types"),
                    PreventionType.class,
                    type -> Logger.warn(MessageFormat.format(locale.blockerDoesNotExist(), type))
            );

            var item = ItemUtils.decode(itemSection.getString("base64"));
            itemsList.add(new PreventableItem(
                    WrappedItem.withMeta(item),
                    Colorizer.colorize(LangUtils.getTranslation(itemSection.getString("translation"), item)),
                    types,
                    handlers,
                    handles,
                    itemSection.getBoolean("set-material-cooldown")
            ));
        }
        preventableItems = itemsList.toArray(new PreventableItem[0]);
    }
}
