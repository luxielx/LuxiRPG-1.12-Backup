package guild;

import Util.FItem;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum Familia {
    AKUMA(Arrays.asList(ChatColor.WHITE + "Vị thần của sức mạnh và ngọn lửa rực cháy",ChatColor.GRAY + "Tất cả đứa con của Akuma ",ChatColor.GRAY +"Nhận 20% kháng sát thương và lửa bất diệt trên đòn đánh"), ChatColor.RED),
    HISAME(Arrays.asList(ChatColor.WHITE + "Vị thần của tình yêu và ma pháp bộc phá",ChatColor.GRAY + "Tất cả đứa con của Hisame ",ChatColor.GRAY +"Nhận thêm 20% sát thương kĩ năng và 20% hồi mana"), ChatColor.LIGHT_PURPLE),
    KAMINARI(Arrays.asList(ChatColor.WHITE + "Vị thần của tốc độ và ảo ảnh",ChatColor.GRAY + "Tất cả đứa con của Kaminari",ChatColor.GRAY +"Nhận thêm 10% tốc độ di chuyển và thêm 20% sát thương nếu tấn công sau lưng"), ChatColor.GOLD),
    RAIJIN(Arrays.asList(ChatColor.WHITE + "Vị thần của Vũ Cung và tên xuyên phá",ChatColor.GRAY + "Tất cả đứa con của Raijin",ChatColor.GRAY +"Nhận khả năng 15% xuyên giáp và 15% tốc độ tấn công"), ChatColor.WHITE),
    AMATERASU(Arrays.asList(ChatColor.WHITE + "Vị thần với ánh hào quang sáng chói",ChatColor.GRAY + "Tất cả đứa con của Amaterasu",ChatColor.GRAY +"Khi áp dụng hiệu ứng cho đồng minh sẽ hồi 10% máu cho bản thân và 5% cho đồng minh"), ChatColor.GREEN);

    List<String> desc;
    ChatColor color;

    Familia(List<String> desc, ChatColor cl) {
        this.desc = desc;
        this.color = cl;
    }

    public static ItemStack famitem() {
        return new FItem(Material.STAINED_GLASS_PANE).setColorShort((15)).setName(ChatColor.RED + "Chọn Familia").toItemStack();

    }


    public static Familia getByName(String s) {
        for (Familia f : Familia.values()) {
            if (f.getName().equalsIgnoreCase(s))
                return f;
        }
        return null;
    }

    public static void openFamiliarGUI(UUID uniqueId, String name) {
        Player p = Bukkit.getPlayer(uniqueId);
        Inventory inv = Bukkit.createInventory(new FamHolder(), 9, ChatColor.RED + "Chọn Familia " + Utils.hideS(name));
        int index = 0;
        for (Familia f : Familia.values()) {
            inv.setItem(index, f.symbol());
            index++;
        }
        p.openInventory(inv);

    }

    public String getName() {
        return Utils.toCappp(this.name());
    }

    public ItemStack symbol() {
        return new FItem(Material.GOLD_SWORD).setName(color + getName()).setLore(getLore()).hideAttributes(true).toItemStack();
    }

    private List<String> getLore() {
        return desc;
    }


}

class FamHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }

}
