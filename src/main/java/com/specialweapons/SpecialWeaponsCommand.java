package com.specialweapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpecialWeaponsCommand implements CommandExecutor {

    private final WeaponManager weaponManager;

    public SpecialWeaponsCommand(WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) { sender.sendMessage("Players only."); return true; }
        if (!p.isOp()) { p.sendMessage(Component.text("Only operators can do this.", NamedTextColor.RED)); return true; }

        if (args.length < 1) {
            p.sendMessage(Component.text("Usage: /sw <add|remove>", NamedTextColor.RED));
            return true;
        }

        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand.getType().isAir()) {
            p.sendMessage(Component.text("Hold the item you want to mark first.", NamedTextColor.RED));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add" -> {
                if (weaponManager.markItem(hand)) {
                    p.sendMessage(Component.text("This item will now be kept safe on death.", NamedTextColor.GREEN));
                } else {
                    p.sendMessage(Component.text("Couldn't mark that item.", NamedTextColor.RED));
                }
            }
            case "remove" -> {
                if (weaponManager.isSpecialWeapon(hand)) {
                    weaponManager.unmarkItem(hand);
                    p.sendMessage(Component.text("This item is no longer protected on death.", NamedTextColor.YELLOW));
                } else {
                    p.sendMessage(Component.text("This item isn't marked as protected.", NamedTextColor.RED));
                }
            }
            default -> p.sendMessage(Component.text("Usage: /sw <add|remove>", NamedTextColor.RED));
        }
        return true;
    }
}
