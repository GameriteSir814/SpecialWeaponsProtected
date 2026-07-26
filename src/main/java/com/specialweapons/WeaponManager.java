package com.specialweapons;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class WeaponManager {

    private final SpecialWeapons plugin;
    private final NamespacedKey weaponKey;

    public WeaponManager(SpecialWeapons plugin) {
        this.plugin = plugin;
        this.weaponKey = new NamespacedKey(plugin, "special_weapon");
    }

    /** Marks the item in the player's hand as a special weapon. */
    public boolean markItem(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) return false;
        var meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(weaponKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return true;
    }

    /** Removes the special weapon mark from an item. */
    public boolean unmarkItem(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) return false;
        var meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(weaponKey);
        item.setItemMeta(meta);
        return true;
    }

    /** Returns true if this item is marked as a special weapon. */
    public boolean isSpecialWeapon(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(weaponKey, PersistentDataType.BYTE);
    }
}
