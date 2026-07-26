package com.specialweapons;

import org.bukkit.plugin.java.JavaPlugin;

public class SpecialWeapons extends JavaPlugin {

    private static SpecialWeapons instance;
    private WeaponManager weaponManager;

    @Override
    public void onEnable() {
        instance = this;
        weaponManager = new WeaponManager(this);
        getServer().getPluginManager().registerEvents(new DeathListener(this, weaponManager), this);
        getCommand("sw").setExecutor(new SpecialWeaponsCommand(weaponManager));
        getLogger().info("SpecialWeapons enabled.");
    }

    public static SpecialWeapons getInstance() { return instance; }
    public WeaponManager getWeaponManager() { return weaponManager; }
}
