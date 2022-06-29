package Util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FItem {

    ItemStack i = null;
    Material type = Material.AIR;
    String name = "";
    List<String> lore = new LinkedList<String>();
    int amount = 1;
    Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
    Color armor_color = null;
    short colordata = 0;
    boolean glow = false;

    boolean hideenchant = false;
    boolean hideattributes = false;
    boolean unbreak = false;

    public FItem(Material type) {
        this.type = type;
    }

    public FItem(ItemStack i) {
        this.i = i;
        this.type = i.getType();
        this.amount = i.getAmount();

        if (i.hasItemMeta()) {
            ItemMeta im = i.getItemMeta();

            if (im.hasDisplayName()) {
                name = im.getDisplayName();
            }
            if (im.hasLore()) {
                lore = im.getLore();
            }
            if (im.hasEnchants()) {
                enchantments = im.getEnchants();
            }
        }
    }

    public FItem setType(Material a) {
        this.type = a;
        return this;
    }

    public FItem setName(String s) {
        this.name = s;
        return this;
    }


    public FItem setAmount(int s) {
        this.amount = s;
        return this;
    }

    public FItem addLore(String s) {
        this.lore.add(s);
        return this;
    }

    public FItem addLore(double s) {
        this.lore.add(s + "");
        return this;
    }

    public FItem removeLore(String s) {
        this.lore.remove(s);
        return this;
    }

    public FItem replaceLore(String from, String to) {
        List<String> newlore = new LinkedList<String>();
        for (String s : this.lore) {
            s = s.replaceAll(from, to);
            newlore.add(s);
        }
        this.lore = newlore;
        return this;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public FItem setLore(List<String> s) {
        this.lore = s;
        return this;
    }

    public FItem addEnchant(Enchantment s, int lv) {
        enchantments.put(s, lv);
        return this;
    }

    public FItem removeEnchant(Enchantment s) {
        enchantments.remove(s);
        return this;
    }

    public FItem setEnchant(Map<Enchantment, Integer> s) {
        this.enchantments = s;
        return this;
    }


    public FItem hideEnchant(boolean a) {
        hideenchant = a;
        return this;
    }

    public FItem hideAttributes(boolean a) {
        hideattributes = a;
        return this;
    }

    public FItem setUnbreak(boolean a) {
        unbreak = a;
        return this;
    }

    public boolean hasEnchant() {
        return !this.enchantments.isEmpty();
    }

    public FItem glow(boolean a) {
        glow = a;
        return this;
    }


    public FItem setArmorColor(Color c) {
        this.armor_color = c;
        return this;
    }

    public FItem setColorShort(int c) {
        this.colordata = (short) c;
        return this;
    }


    public ItemStack toItemStack() {
        if (i == null) {
            if (colordata != 0) {
                i = new ItemStack(type, amount, colordata);

            } else {
                i = new ItemStack(type, amount);
            }

        }

        if (armor_color != null && type.toString().startsWith("LEATHER_")) {
            LeatherArmorMeta im = (LeatherArmorMeta) i.getItemMeta();

            im.setColor(armor_color);

            i.setItemMeta(im);
        }


        ItemMeta im = i.getItemMeta();

        if (unbreak) {
            im.spigot().setUnbreakable(true);
        }

        if (hideenchant) im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (hideattributes) im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (glow) im.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        if (!name.equals("")) {
            im.setDisplayName(name);
        }
        im.setLore(lore);
        i.setItemMeta(im);

        i.addUnsafeEnchantments(enchantments);


        if (glow) {
            i.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }

        i.setAmount(amount);


        return i;
    }

    public FItem clone() {
        FItem fi = new FItem(this.type);
        fi.setAmount(this.amount);
        fi.setEnchant(this.enchantments);
        fi.setLore(this.lore);
        fi.setName(this.name);
        fi.setUnbreak(this.unbreak);
        fi.glow(this.glow);
        fi.hideAttributes(this.hideattributes);
        fi.hideEnchant(this.hideenchant);

        return fi;
    }
}
