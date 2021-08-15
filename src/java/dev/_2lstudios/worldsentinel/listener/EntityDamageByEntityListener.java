package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class EntityDamageByEntityListener implements Listener {
    private final RegionManager regionManager;
    private final MainConfiguration mainConfiguration;

    EntityDamageByEntityListener(final RegionManager regionManager, final MainConfiguration mainConfiguration) {
        this.regionManager = regionManager;
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Entity damaged = event.getEntity();
        Entity damager = event.getDamager();
        if (damager instanceof Projectile) {
            final ProjectileSource source = ((Projectile) damager).getShooter();
            if (source instanceof Entity) {
                damager = (Entity) source;
            }
        }
        final Region region = this.regionManager.getRegionInside(damager.getLocation());
        final Region region2 = this.regionManager.getRegionInside(damaged.getLocation());
        if (damaged instanceof Player && damager instanceof Player) {
            final boolean regionPvp = region == null || region.getFlags().getBoolean("pvp");
            final boolean region1Pvp = region2 == null || region2.getFlags().getBoolean("pvp");
            if (!regionPvp || !region1Pvp) {
                event.setCancelled(true);
            }
        } else if (damaged instanceof Creature) {
            if (!this.mainConfiguration.isCreaturesDamageAllowed()) {
                event.setCancelled(true);
            } else if (damager instanceof Player) {
                final boolean regionPve = region == null || region.getFlags().getBoolean("pve");
                final boolean region1Pve = region2 == null || region2.getFlags().getBoolean("pve");
                if (!regionPve || !region1Pve) {
                    event.setCancelled(true);
                }
            }
        } else if (damager instanceof Creature) {
            if (!this.mainConfiguration.isCreaturesDamageAllowed()) {
                event.setCancelled(true);
            } else if (damaged instanceof Player) {
                final boolean regionEvp = region == null || region.getFlags().getBoolean("evp");
                final boolean region1Evp = region2 == null || region2.getFlags().getBoolean("evp");
                if (!regionEvp || !region1Evp) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
