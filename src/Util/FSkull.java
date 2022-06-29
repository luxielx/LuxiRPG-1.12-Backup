package Util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class FSkull {

    public static ItemStack byName(String skullname) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        SkullMeta sm = (SkullMeta) head.getItemMeta();

        sm.setOwningPlayer(Bukkit.getOfflinePlayer(skullname));

        head.setItemMeta(sm);

        return head;
    }

    public static ItemStack byUUID(UUID uuid) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        SkullMeta sm = (SkullMeta) head.getItemMeta();

        sm.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));

        head.setItemMeta(sm);

        return head;
    }


    public static ItemStack byValue(String skinValue) {

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        if (skinValue.isEmpty()) {
            return head;
        }

        ItemMeta headMeta = head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", skinValue));

        Field profileField = null;

        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException ex) {
        }
        profileField.setAccessible(true);
        try {
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | IllegalAccessException ex) {
        }

        head.setItemMeta(headMeta);

        return head;
    }
}

