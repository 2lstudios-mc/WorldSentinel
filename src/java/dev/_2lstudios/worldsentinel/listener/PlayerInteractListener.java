// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Entity;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import org.bukkit.entity.EntityType;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import dev._2lstudios.worldsentinel.events.RegionDenyEvent;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.HashSet;
import org.bukkit.Material;
import java.util.Collection;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import dev._2lstudios.worldsentinel.region.RegionPlayerManager;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;

class PlayerInteractListener implements Listener {
    private final PluginManager pluginManager;
    private final RegionManager regionManager;
    private final RegionPlayerManager regionPlayerManager;
    private final MainConfiguration mainConfiguration;
    private final Collection<Material> interactableBlocks;
    private final Collection<Material> entityItems;
    private final Collection<Material> potions;

    PlayerInteractListener(final PluginManager pluginManager, final RegionManager regionManager,
            final RegionPlayerManager regionPlayerManager, final MainConfiguration mainConfiguration) {
        this.interactableBlocks = new HashSet<Material>();
        this.entityItems = new HashSet<Material>();
        this.potions = new HashSet<Material>();
        this.pluginManager = pluginManager;
        this.regionManager = regionManager;
        this.regionPlayerManager = regionPlayerManager;
        this.mainConfiguration = mainConfiguration;
        Material[] values;
        for (int length = (values = Material.values()).length, i = 0; i < length; ++i) {
            final Material material = values[i];
            final String materialString = material.toString();
            if (materialString.endsWith("CHEST") || materialString.endsWith("FURNACE")
                    || materialString.endsWith("DROPPER") || materialString.endsWith("HOPPER")
                    || materialString.endsWith("TABLE") || materialString.endsWith("ANVIL")
                    || materialString.endsWith("GATE") || materialString.endsWith("DOOR")
                    || materialString.endsWith("BUTTON") || materialString.endsWith("LEVER")
                    || materialString.endsWith("PRESSURE_PLATE") || materialString.contains("REDSTONE_REPEATER")
                    || materialString.contains("REDSTONE_COMPARATOR") || materialString.contains("DIODE")
                    || materialString.contains("SHULKER_BOX") || materialString.endsWith("BEACON")) {
                this.interactableBlocks.add(material);
            } else if (material == Material.MINECART || material.name().toLowerCase().contains("boat")
                    || material == Material.ARMOR_STAND || material.name().toLowerCase().contains("monster_egg")) {
                this.entityItems.add(material);
            } else if (materialString.contains("POTION")) {
                this.potions.add(material);
            }
        }
    }

    private boolean setCancelled(final PlayerInteractEvent event, final Region region) {
        final RegionDenyEvent regionDenyEvent = new RegionDenyEvent((Event) event, region);
        this.pluginManager.callEvent((Event) regionDenyEvent);
        final boolean cancelled = !regionDenyEvent.isCancelled();
        if (cancelled) {
            event.setCancelled(cancelled);
        }
        return cancelled;
    }

    private boolean checkPlayerInteraction(final Material itemType, final Region playerRegion) {
        if (this.potions.contains(itemType) && playerRegion != null) {
            final boolean potionsAllowed = playerRegion.getFlags().getBoolean("potions");
            if (!potionsAllowed) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBlockInteraction(final Block block, final String playerName, final Material itemType,
            final Region blockRegion) {
        if (block != null && this.entityItems.contains(itemType)) {
            if (blockRegion != null) {
                final RegionFlags flags = blockRegion.getFlags();
                final Collection<String> owners = flags.getCollection("owners");
                final Collection<String> members = flags.getCollection("members");
                final boolean interacting = flags.getBoolean("interacting");
                final boolean placing = flags.getBoolean("placing");
                if (!interacting || (!placing && !owners.contains(playerName) && !members.contains(playerName))) {
                    return true;
                }
            }
            final int minecartLimit = this.mainConfiguration.getMinecartLimit();
            final int boatLimit = this.mainConfiguration.getBoatLimit();
            final int armorStandLimit = this.mainConfiguration.getArmorStandLimit();
            int minecarts = 0;
            int boats = 0;
            int armorStands = 0;
            Entity[] entities;
            for (int length = (entities = block.getLocation().getChunk().getEntities()).length,
                    i = 0; i < length; ++i) {
                final Entity entity1 = entities[i];
                final EntityType entityType1 = entity1.getType();
                if (entityType1 == EntityType.MINECART) {
                    ++minecarts;
                } else if (entityType1 == EntityType.BOAT) {
                    ++boats;
                } else if (entityType1 == EntityType.ARMOR_STAND) {
                    ++armorStands;
                }
            }
            if ((itemType == Material.MINECART && minecarts > minecartLimit)
                    || (itemType == Material.getMaterial("BOAT") && boats > boatLimit)
                    || (itemType == Material.ARMOR_STAND && armorStands > armorStandLimit)) {
                return true;
            }
        }
        return false;
    }

    private Region getBlockRegion(final Block block) {
        return this.regionManager
                .getRegionInside((block == null) ? null : block.getLocation().clone().add(0.5, -0.5, 0.5));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        final Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK && action != Action.PHYSICAL) {
            return;
        }
        final String playerName = player.getName();
        final Block block = event.getClickedBlock();
        final Region blockRegion = this.getBlockRegion(block);
        if (block != null && blockRegion != null) {
            final Material blockType = block.getType();
            if (this.interactableBlocks.contains(blockType)) {
                final RegionFlags flags = blockRegion.getFlags();
                final boolean interacting = flags.getBoolean("interacting");
                if (!interacting && !flags.getCollection("owners").contains(playerName)
                        && !flags.getCollection("members").contains(playerName)
                        && this.setCancelled(event, blockRegion)) {
                    return;
                }
            }
        }
        final ItemStack item = event.getItem();
        if (item == null || (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        final Region playerRegion = this.regionPlayerManager.getPlayer(player).getRegion();
        if (this.checkPlayerInteraction(item.getType(), playerRegion) && this.setCancelled(event, playerRegion)) {
            return;
        }
        if (this.checkBlockInteraction(block, playerName, item.getType(), blockRegion)
                && this.setCancelled(event, blockRegion)) {
            return;
        }
    }
}
