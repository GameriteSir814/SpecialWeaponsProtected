package com.specialweapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DeathListener implements Listener {

    private final SpecialWeapons plugin;
    private final WeaponManager weaponManager;

    // Stores saved weapons per player UUID until they respawn
    private final Map<UUID, List<ItemStack>> savedWeapons = new HashMap<>();

    public DeathListener(SpecialWeapons plugin, WeaponManager weaponManager) {
        this.plugin = plugin;
        this.weaponManager = weaponManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        List<ItemStack> toSave = new ArrayList<>();

        // Remove any special weapons from the drop list and save them
        e.getDrops().removeIf(item -> {
            if (weaponManager.isSpecialWeapon(item)) {
                toSave.add(item.clone());
                return true; // remove from drops
            }
            return false;
        });

        if (!toSave.isEmpty()) {
            savedWeapons.put(victim.getUniqueId(), toSave);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        List<ItemStack> weapons = savedWeapons.remove(p.getUniqueId());
        if (weapons == null || weapons.isEmpty()) return;

        // Give back on the tick after respawn so inventory is ready
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (ItemStack weapon : weapons) {
                if (p.getInventory().firstEmpty() != -1) {
                    p.getInventory().addItem(weapon);
                } else {
                    p.getWorld().dropItemNaturally(p.getLocation(), weapon);
                }
            }
            p.sendMessage(Component.text("Your protected item has been returned to you.", NamedTextColor.GOLD));
        }, 2L);
    }
}
