package Luxiel;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Parties.PartyUtils;
import Util.FItem;
import Util.FSkull;
import Util.Utils;
import guild.GuildUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import stat.StatPlayer;

public class Profile implements Listener, CommandExecutor {

    public static void open(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, "§0Profile");
        for (int ct = 0; ct < 27; ct++) {
            if (inv.getItem(ct) == null) {
                inv.setItem(ct, Utils.blankItem(Material.STAINED_GLASS_PANE, 15));
            }
        }
        updateinv(inv, p);
        p.openInventory(inv);
    }

    public static void openOther(Player p, OfflinePlayer target) {
        if (target == null) {
            p.sendMessage(ChatColor.RED + target.getName() + " Không hoạt động");
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 9, "§6Profile của §3" + target.getName());
        inv.setItem(4, playerInfo(target));
        inv.setItem(8, new FItem(Material.TORCH).setName("§eMời vào tổ đội").toItemStack());
        inv.setItem(7, new FItem(Material.ENCHANTMENT_TABLE).setName("§eMời vào Guild").toItemStack());
        inv.setItem(0, new FItem(Material.ARROW).setName("§eMời giao dịch").toItemStack());
        for (int ct = 0; ct < 9; ct++) {
            if (inv.getItem(ct) == null) {
                inv.setItem(ct, Utils.blankItem(Material.STAINED_GLASS_PANE, 15));
            }
        }
        p.openInventory(inv);
    }

    private static void updateinv(Inventory inv, Player p) {
        inv.setItem(4, playerInfo(p));
        inv.setItem(11, new FItem(new ItemStack(Material.KNOWLEDGE_BOOK)).setName(ChatColor.GRAY + "Nâng cấp " + ChatColor.RED + "Sức Mạnh").addLore(ChatColor.GRAY + "Điểm " + ChatColor.RED + "Sức Mạnh " + ChatColor.GRAY + "tăng sát thương đòn đánh thường ").addLore(ChatColor.GRAY + "Và sát thương từ kĩ năng vật lý!").addLore(" ").addLore(ChatColor.GOLD + "Điểm hiện tại: " + ChatColor.RESET + RPGPlayerListener.get(p.getUniqueId().toString()).getStr()).addLore("").addLore(ChatColor.GRAY + "Chuột Trái +1, Chuột phải +5").setAmount(1).toItemStack());
        inv.setItem(12, new FItem(new ItemStack(Material.KNOWLEDGE_BOOK)).setName(ChatColor.GRAY + "Nâng cấp " + ChatColor.YELLOW + "Nhanh Nhẹn").addLore(ChatColor.GRAY + "Điểm " + ChatColor.YELLOW + "Nhanh Nhẹn " + ChatColor.GRAY + "tăng tốc độ di chuyển, tốc độ đánh, tỉ lệ chí mạng!").addLore(" ").addLore(ChatColor.GOLD + "Điểm hiện tại: " + ChatColor.RESET + RPGPlayerListener.get(p.getUniqueId().toString()).getAgi()).addLore("").addLore(ChatColor.GRAY + "Chuột Trái +1, Chuột phải +5").setAmount(1).toItemStack());
        inv.setItem(13, new FItem(new ItemStack(Material.KNOWLEDGE_BOOK)).setName(ChatColor.GRAY + "Nâng cấp " + ChatColor.AQUA + "Ma Pháp").addLore(ChatColor.GRAY + "Điểm " + ChatColor.AQUA + "Ma Pháp " + ChatColor.GRAY + "tăng tốc độ hồi mana, sát thương ma pháp!").addLore(" ").addLore(ChatColor.GOLD + "Điểm hiện tại: " + ChatColor.RESET + RPGPlayerListener.get(p.getUniqueId().toString()).getInt()).addLore("").addLore(ChatColor.GRAY + "Chuột Trái +1, Chuột phải +5").setAmount(1).toItemStack());
        inv.setItem(14, new FItem(new ItemStack(Material.KNOWLEDGE_BOOK)).setName(ChatColor.GRAY + "Nâng cấp " + ChatColor.GREEN + "Sinh Lực").addLore(ChatColor.GRAY + "Điểm " + ChatColor.GREEN + "Sinh Lực " + ChatColor.GRAY + "Máu tối đa và tốc độ hồi máu!").addLore(" ").addLore(ChatColor.GOLD + "Điểm hiện tại: " + ChatColor.RESET + RPGPlayerListener.get(p.getUniqueId().toString()).getVit()).addLore("").addLore(ChatColor.GRAY + "Chuột Trái +1, Chuột phải +5").setAmount(1).toItemStack());
        inv.setItem(15, new FItem(new ItemStack(Material.ENCHANTED_BOOK)).setName(ChatColor.GOLD + "Điểm Nâng Cấp").addLore(ChatColor.GRAY + "Dùng để nâng cấp thuộc tính").addLore(" ").addLore(ChatColor.GOLD + "Điểm hiện tại: " + ChatColor.RESET + RPGPlayerListener.get(p.getUniqueId().toString()).getAttrPoint()).setAmount(1).toItemStack());
    }

    public static ItemStack playerInfo(OfflinePlayer target) {
        RPGPlayer rpg = RPGPlayerListener.get(target.getUniqueId().toString());
        ItemStack sk;
        if (target.isOnline()) {
            Player p = target.getPlayer();
            sk = new FItem(FSkull.byUUID(target.getUniqueId()))
                    .setName("§f" + target.getName())
                    .addLore("§6Level §f" + rpg.getLevel())
                    .addLore("§aExp §f" + rpg.getExp())
                    .addLore("§cSức Mạnh: §f" + rpg.getStr() + ChatColor.GREEN + " + " + ChatColor.GRAY + "(" + (StatPlayer.sTr(p) - rpg.getStr()) + ")")
                    .addLore("§eNhanh Nhẹn: §f" + +rpg.getAgi() + ChatColor.GREEN + " + " + ChatColor.GRAY + "(" + (StatPlayer.aGi(p) - rpg.getAgi()) + ")")
                    .addLore("§bMa Pháp: §f" + rpg.getInt() + ChatColor.GREEN + " + " + ChatColor.GRAY + "(" + (StatPlayer.inTel(p) - rpg.getInt()) + ")")
                    .addLore("§aSinh Lực: §f" + +rpg.getVit() + ChatColor.GREEN + " + " + ChatColor.GRAY + "(" + (StatPlayer.vIt(p) - rpg.getVit()) + ")")
                    .addLore("§4Máu: §f" + (int) p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                    .addLore("§7Giáp: §f" + StatPlayer.giap(p))
                    .addLore("§fTổ đội: §8" + (PartyUtils.haveParty(target.getUniqueId()) ? PartyUtils.getParty(target.getUniqueId()).getColor() + PartyUtils.getPartyName(target.getUniqueId()) : "§8Chưa có"))
                    .addLore("§6Guild: §f" + (GuildUtils.haveGuild(target.getUniqueId()) ? Utils.getRpgPlayer(target.getUniqueId()).getGuild().getName() : "Chưa có"))
                    .toItemStack();
        } else {
            sk = new FItem(FSkull.byUUID(target.getUniqueId()))
                    .setName("§f" + target.getName())
                    .addLore("§6Level §f" + rpg.getLevel())
                    .addLore("§aExp §f" + rpg.getExp())
                    .addLore("§cSức Mạnh: §f" + rpg.getStr() + ChatColor.GREEN)
                    .addLore("§eNhanh Nhẹn: §f" + +rpg.getAgi() + ChatColor.GREEN)
                    .addLore("§bMa Pháp: §f" + rpg.getInt() + ChatColor.GREEN)
                    .addLore("§aSinh Lực: §f" + +rpg.getVit() + ChatColor.GREEN)
//					.addLore("§4Máu: §f" + rpg.getVit())
//					.addLore("§7Giáp: §f" + StatPlayer.giap(p))
                    .addLore("§fTổ đội: §8" + (PartyUtils.haveParty(target.getUniqueId()) ? PartyUtils.getParty(target.getUniqueId()).getColor() + PartyUtils.getPartyName(target.getUniqueId()) : "§8Chưa có"))
                    .addLore("§6Guild: §f" + (GuildUtils.haveGuild(target.getUniqueId()) ? Utils.getRpgPlayer(target.getUniqueId()).getGuild().getName() : "Chưa có"))
                    .toItemStack();
        }
        return sk;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getView().getTitle().equals("§0Profile")) {
            e.setCancelled(true);
            int slot = e.getSlot();
            Player player = (Player) e.getWhoClicked();
            RPGPlayer spl = RPGPlayerListener.get(player.getUniqueId().toString());
            ClickType a = e.getClick();
            int point = 1;
            if (a == ClickType.RIGHT) point = 5;
            if (slot == 11) {
                if (spl.getAttrPoint() >= point) {
                    spl.setAttrPoint(spl.getAttrPoint() - point);
                    spl.setStr(spl.getStr() + point);
                } else {
                    return;
                }
            } else if (slot == 12) {
                if (spl.getAttrPoint() >= point) {
                    spl.setAttrPoint(spl.getAttrPoint() - point);
                    spl.setAgi(spl.getAgi() + point);
                } else {
                    return;
                }
            } else if (slot == 13) {
                if (spl.getAttrPoint() >= point) {
                    spl.setAttrPoint(spl.getAttrPoint() - point);
                    spl.setIntel(spl.getInt() + point);
                } else {
                    return;
                }
            } else if (slot == 14) {
                if (spl.getAttrPoint() >= point) {
                    spl.setAttrPoint(spl.getAttrPoint() - point);
                    spl.setVit(spl.getVit() + point);
                } else {
                    return;
                }
            }
            updateinv(e.getInventory(), player);
        }
        if (e.getView().getTitle().contains("§6Profile")) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if (e.getSlot() == 7) {
                String name = e.getView().getTitle();
                String nocolor = ChatColor.stripColor(name);
                String[] split = nocolor.split(" ");
                Player target = Bukkit.getPlayer(split[2]);
                if (target == null) return;
                GuildUtils.invite(p.getUniqueId(), target.getUniqueId());
            }
            if (e.getSlot() == 8) {
                String name = e.getView().getTitle();
                String nocolor = ChatColor.stripColor(name);
                String[] split = nocolor.split(" ");
                Player target = Bukkit.getPlayer(split[2]);
                if (target == null) return;
                PartyUtils.invite(p.getUniqueId(), target.getUniqueId());
            }
            if (e.getSlot() == 0) {
                String name = e.getView().getTitle();
                String nocolor = ChatColor.stripColor(name);
                String[] split = nocolor.split(" ");
                Player target = Bukkit.getPlayer(split[2]);
               p.chat("/trade " + target.getName());
            }

        }
    }

    @EventHandler
    public void shiftrightclick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            if (e.getPlayer().isSneaking()) {
                if (e.getRightClicked().hasMetadata("NPC")) return;
                openOther(e.getPlayer(), (Player) e.getRightClicked());
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String a, String[] args) {
        if (a.equalsIgnoreCase("profile")) {
            if (args.length > 0) {
                openOther((Player) sender, Bukkit.getPlayer(args[0]));
            } else if (args.length < 1) {
                open((Player) sender);
            }
        }

        return true;
    }
}
