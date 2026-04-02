package dev.enco.greatcombat.core.restrictions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.managers.IInteractionManager;
import dev.enco.greatcombat.api.managers.IPreventionManager;
import dev.enco.greatcombat.api.models.InteractionHandler;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.utils.Time;
import it.unimi.dsi.fastutil.objects.*;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InteractionManager implements IInteractionManager {
    private final JavaPlugin plugin;
    private final ICooldownManager cooldownManager;
    private final IPreventionManager preventionManager;
    private final ConfigManager configManager;
    private final Reference2ObjectMap<Class<? extends Event>, List<InteractionHandler<?>>> handlerMap = new Reference2ObjectOpenHashMap<>();
    private final Object2ObjectMap<String, InteractionHandler<?>> nameMap = new Object2ObjectOpenHashMap<>();
    private final ReferenceSet<Class<? extends Event>> registeredListeners = new ReferenceOpenHashSet<>();

    @Override
    public <T extends Event> void registerMapping(Class<T> eventClass, InteractionHandler<T> handler) {
        //1.16 кал
        List<InteractionHandler<?>> handlers = handlerMap.get(eventClass);
        if (handlers == null) {
            handlers = new ArrayList<>();
            handlerMap.put(eventClass, handlers);
        }
        handlers.add(handler);
        nameMap.put(handler.name(), handler);
        if (!registeredListeners.contains(eventClass)) {
            registerDynamic(eventClass);
            registeredListeners.add(eventClass);
        }
    }

    @Override
    public <T extends Event> InteractionHandler<T> newHandler(String name, Predicate<T> predicate, Function<T, Player> playerExt, Function<T, ItemStack> itemExt) {
        return new InteractionHandler<T>() {
            @Override public String name() { return name; }
            @Override public Predicate<T> predicate() { return predicate; }
            @Override public Function<T, Player> playerExtractor() { return playerExt; }
            @Override public Function<T, ItemStack> itemExtractor() { return itemExt; }
        };
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void registerDynamic(Class<T> eventClass) {
        Bukkit.getPluginManager().registerEvent(
                eventClass,
                new Listener() {},
                EventPriority.HIGHEST,
                (listener, event) -> {
                    if (eventClass == event.getClass()) {
                        processEvent((T) event);
                    }
                },
                plugin
        );
    }

    private <T extends Event> void processEvent(T event) {
        getPredicatedHandlers(event)
                .forEach(h -> {
                    Player player = h.playerExtractor().apply(event);
                    ItemStack itemStack = h.itemExtractor().apply(event);
                    if (player == null || itemStack == null) return;
                    boolean cancelled = runCoreChecks(player, itemStack, h);
                    if (cancelled && event instanceof Cancellable c) c.setCancelled(true);
                });
    }

    private boolean runCoreChecks(Player player, ItemStack is, InteractionHandler<?> handler) {
        WrappedItem wrapped = WrappedItem.wrap(is);
        var cooldownItem = cooldownManager.getCooldownItem(wrapped);
        if (cooldownItem != null && cooldownItem.handlers().contains(handler.name())) {
            if (player.hasPermission("greatcombat.cooldowns.bypass")) return false;
            if (cooldownManager.hasCooldown(player.getUniqueId(), cooldownItem)) {
                int time = cooldownManager.getCooldownTime(player.getUniqueId(), cooldownItem);
                configManager.getMessages().onItemCooldown().execute(player, Time.format(time), cooldownItem.translation());
                return true;
            }
            cooldownManager.putCooldown(player.getUniqueId(), player, cooldownItem);
        }
        var preventionItem = preventionManager.getPreventableItem(wrapped);
        if (preventionItem != null && preventionItem.handlers().contains(handler.name())) {
            if (player.hasPermission("greatcombat.prevention.bypass")) return false;
            configManager.getMessages().onInteract().execute(player, preventionItem.translation());
            return true;
        }
        return false;
    }

    @Override
    public <T extends Event> Stream<InteractionHandler<T>> getHandlers(T event) {
        return cast(handlerMap.get(event.getClass()));
    }

    @Override
    public <T extends Event> Stream<InteractionHandler<T>> getPredicatedHandlers(T event) {
        return getHandlers(event).filter(h -> h.predicate().test(event));
    }

    @Override
    public <T extends Event> void handle(T event, Consumer<T> consumer) {
        getPredicatedHandlers(event)
                .forEach(h -> consumer.accept(event));
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> Stream<InteractionHandler<T>> cast(List<InteractionHandler<?>> uncasted) {
        return uncasted.stream()
                .map(handler -> (InteractionHandler<T>) handler);
    }

    public void registerDefaults() {
        registerMapping(PlayerItemConsumeEvent.class, newHandler(
                "CONSUME", e -> true, PlayerEvent::getPlayer, PlayerItemConsumeEvent::getItem));

        registerMapping(PlayerInteractEvent.class, newHandler(
                "RIGHT_CLICK_AIR", e -> e.getAction() == Action.RIGHT_CLICK_AIR, PlayerEvent::getPlayer, PlayerInteractEvent::getItem));

        registerMapping(PlayerInteractEvent.class, newHandler(
                "RIGHT_CLICK_BLOCK", e -> e.getAction() == Action.RIGHT_CLICK_BLOCK, PlayerEvent::getPlayer, PlayerInteractEvent::getItem));

        registerMapping(PlayerInteractEvent.class, newHandler(
                "LEFT_CLICK_AIR", e -> e.getAction() == Action.LEFT_CLICK_AIR, PlayerEvent::getPlayer, PlayerInteractEvent::getItem));

        registerMapping(PlayerInteractEvent.class, newHandler(
                "LEFT_CLICK_BLOCK", e -> e.getAction() == Action.LEFT_CLICK_BLOCK, PlayerEvent::getPlayer, PlayerInteractEvent::getItem));

        registerMapping(BlockBreakEvent.class, newHandler(
                "BLOCK_BREAK", e -> true, BlockBreakEvent::getPlayer, e -> e.getPlayer().getInventory().getItemInMainHand()));

        registerMapping(EntityResurrectEvent.class, newHandler(
                "RESURRECT_MAINHAND", e -> e.getEntity() instanceof Player,
                e -> (Player) e.getEntity(), e -> ((Player) e.getEntity()).getInventory().getItemInMainHand()));

        registerMapping(EntityResurrectEvent.class, newHandler(
                "RESURRECT_OFFHAND", e -> e.getEntity() instanceof Player,
                e -> (Player) e.getEntity(), e -> ((Player) e.getEntity()).getInventory().getItemInOffHand()));

        registerMapping(EntityShootBowEvent.class, newHandler(
                "BOW_SHOOT", e -> e.getEntity() instanceof Player,
                e -> (Player) e.getEntity(), EntityShootBowEvent::getBow));

        registerMapping(ProjectileLaunchEvent.class, newHandler(
                "PROJECTILE_LAUNCH", e -> e.getEntity().getShooter() instanceof Player,
                e -> (Player) e.getEntity().getShooter(),
                e -> ((Player) e.getEntity().getShooter()).getInventory().getItemInMainHand()));
    }
}
