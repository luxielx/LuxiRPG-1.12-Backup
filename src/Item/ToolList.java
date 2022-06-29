package Item;


import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum ToolList {
    CROSSBOW(new Material[]{Material.DIAMOND_PICKAXE}, "Nỏ"),
    SWORD(new Material[]{Material.DIAMOND_SWORD}, "Kiếm"),
    STAFF(new Material[]{Material.DIAMOND_HOE}, "Trượng"),
    SPEAR(new Material[]{Material.DIAMOND_SPADE}, "Thương"),
    DAGGER(new Material[]{Material.SHEARS}, "Dao Găm"),
    WARAXE(new Material[]{Material.DIAMOND_AXE}, "Búa Chiến"),
    BOW(new Material[]{Material.BOW}, "Cung"),

    POTION(new Material[]{Material.POTION}, "Thuốc"),
    WEAPON(new Material[]{Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SWORD, Material.DIAMOND_HOE, Material.DIAMOND_SPADE, Material.SHEARS, Material.BOW}, "Vũ Khí"),

    MELEE(new Material[]{Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.DIAMOND_SPADE, Material.SHEARS}, "Vũ Khí"),

    AGIWEAPON(new Material[]{Material.BOW, Material.SHEARS, Material.DIAMOND_PICKAXE, Material.DIAMOND_HOE}, "Vũ Khí"),


    ARMOR(new Material[]{Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLD_BOOTS,
            Material.DIAMOND_BOOTS, Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS,
            Material.GOLD_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.LEATHER_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLD_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE, Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET,
            Material.GOLD_HELMET, Material.DIAMOND_HELMET}, "Giáp"),
    CHESTPLATE(
            new Material[]{Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE,
                    Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE},
            "Giáp"),
    LEGGINGS(
            new Material[]{Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS,
                    Material.IRON_LEGGINGS, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS},
            "Quần"),
    HELMET(
            new Material[]{Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET,
                    Material.IRON_HELMET, Material.IRON_HELMET, Material.DIAMOND_HELMET},
            "Mũ"),
    BOOTS(
            new Material[]{Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS,
                    Material.IRON_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS},
            "Giày"),
    ALL(new Material[]{Material.DIAMOND_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
            Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE,
            Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLD_AXE, Material.DIAMOND_AXE,
            Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE,
            Material.GOLD_SPADE, Material.DIAMOND_SPADE,
            Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLD_BOOTS,
            Material.DIAMOND_BOOTS, Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS,
            Material.GOLD_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.LEATHER_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLD_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE, Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET,
            Material.GOLD_HELMET, Material.DIAMOND_HELMET, Material.BOW,
            Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE,
            Material.GOLD_HOE, Material.DIAMOND_HOE, Material.SHEARS
            ,}, "Tất cả");

    Material[] itemlist;
    String name;

    ToolList(Material[] itemlist, String name) {
        this.itemlist = itemlist;
        this.name = name;
    }

    public List<Material> getItemList() {
        return Arrays.asList(this.itemlist);
    }

    public String getName() {
        return this.name;
    }


    public Material getRandomTool() {
        return Arrays.asList(this.itemlist).get(ThreadLocalRandom.current().nextInt(this.itemlist.length));
    }

}
