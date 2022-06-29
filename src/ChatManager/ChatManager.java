package ChatManager;

import ChucNghiep.Classes;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Parties.Party;
import Parties.PartyUtils;
import Util.Utils;
import com.earth2me.essentials.Essentials;
import guild.GuildUtils;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by ADMIN on 06/01/2019.
 */
public class ChatManager implements Listener {
    public static int chattime = 1;
    HashMap<UUID, Long> timers = new HashMap<>();

    public static long cooldown(int level) {
        return 1000 * (chattime);
    }

//	public static boolean isMentionable(Player player) {
//		if (disableMention.contains(player.getUniqueId())) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	public static void setMentionable(Player player, boolean bl) {
//		if (bl) {
//
//			if (disableMention.contains(player.getUniqueId())) {
//				disableMention.remove(player.getUniqueId());
//			}
//		} else {
//
//			if (!disableMention.contains(player.getUniqueId())) {
//				disableMention.add(player.getUniqueId());
//			}
//		}
//	}

    public boolean cooldown(int level, Player player) {
        if (!timers.containsKey(player.getUniqueId())) {
            timers.put(player.getUniqueId(), 0l);
        }
        if (System.currentTimeMillis() - timers.get(player.getUniqueId()) < cooldown(level)) {
            player.sendMessage(ChatColor.GREEN + "Bạn cần đợi " + ChatColor.RED
                    + ((cooldown(level) - System.currentTimeMillis() + timers.get(player.getUniqueId())) / 1000 + 1)
                    + ChatColor.GREEN + " giây Để chat tiếp");
            return true;
        }

        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void chatformat(AsyncPlayerChatEvent e) {
        if (e.isCancelled())
            return;
        if (cooldown(1, e.getPlayer())) {
            e.setCancelled(true);
            return;
        }

        if (!e.getPlayer().hasPermission("rpg.bypasschat")) {
            timers.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
        e.setCancelled(true);
        Player player = e.getPlayer();
        RPGPlayer rpgPlayer = Utils.getRpgPlayer(player.getUniqueId());
        Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        // PermissionsEx.getUser(player).getPrefix().replaceAll("&", "§");
        User user = Main.getLPAPI().getUserManager().getUser(player.getUniqueId());
        ContextManager cm = Main.getLPAPI().getContextManager();
        QueryOptions queryOptions = cm.getQueryOptions(user).orElse(cm.getStaticQueryOptions());
        CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
        String playerprefix = metaData.getPrefix().replaceAll("&", "§");

        com.earth2me.essentials.User sender = ess.getUser(e.getPlayer());

        Bukkit.getScheduler().runTaskAsynchronously(Main.m, new Runnable() {

            @Override
            public void run() {
                int plevel = player.getLevel();

                String guild = "";
                if (GuildUtils.haveGuild(player.getUniqueId())) {
                    guild = ChatColor.GRAY + "[" + rpgPlayer.getGuild().getColor() + rpgPlayer.getGuild().getName() + ChatColor.GRAY + "] ";
                }

                TextComponent plevelc = new TextComponent(guild);
                Classes pcl = RPGPlayerListener.get(player).getPlayerClass();
                TextComponent plv = new TextComponent(ChatColor.GRAY + "[" + ChatColor.WHITE + pcl.getName() + ChatColor.GRAY + "/" + ChatColor.WHITE + player.getLevel() + ChatColor.GRAY + "] ");

                String message = e.getMessage();
                boolean party = false;
                String partyy = "";
                String suffix = metaData.getSuffix().replaceAll("&", "§");
                if (PartyUtils.haveParty(player.getUniqueId())) {
                    if (message.startsWith("@")) {
                        message = message.substring(1);
                        party = true;
                        suffix = ChatColor.AQUA.toString();
                    }
                    Party ptt = PartyUtils.getParty(player.getUniqueId());
                    partyy = ptt.getColor() + ptt.getName();
                }
                if (sender.isMuted()) {
                    Main.m.getServer().getConsoleSender().sendMessage(
                            ChatColor.RED + "[ChatLogger] [Muted] " + e.getPlayer().getName() + ": " + e.getMessage());
                    return;
                }

                long money = sender.getMoney().longValue();


                TextComponent prefix = new TextComponent(playerprefix + " ");

                HoverEvent playerstats = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(
                                ChatColor.GRAY + "Số tiền: " + ChatColor.GREEN + "$" + ChatColor.GRAY + money + "\n"
                                        + ChatColor.GRAY + "Tên thật: " + ChatColor.GREEN + player.getName()
                                        + "\n" + ChatColor.GRAY + "Level: " + ChatColor.GREEN + player.getLevel() + "\n" + ChatColor.GRAY + "Guild: " + guild
                                        + "\n" + ChatColor.GRAY + "Party: " + partyy)
                                .create());
                plevelc.setHoverEvent(playerstats);
                TextComponent name = new TextComponent(e.getPlayer().getName());
                name.setColor(ChatColor.RESET);
                name.setBold(false);
                if (sender.getNickname() != null) {
                    name = new TextComponent(sender.getNickname());
                }
                name.setHoverEvent(playerstats);

                TextComponent end = new TextComponent(" >> ");
                end.setColor(ChatColor.GOLD);
                String[] split = message.split(" ");


                HoverEvent msgh = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ChatColor.RED + "Bấm vào để xem thông tin").create());
                plevelc.addExtra(plv);
                plevelc.addExtra(prefix);
                plevelc.addExtra(name);
                plevelc.addExtra(end);
                plevelc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/profile" + " " + e.getPlayer().getName()));

                for (String z : split) {
                    TextComponent cm = null;
                    if (z.equalsIgnoreCase("[item]") && player.hasPermission("rpg.showitem")
                            && player.getInventory().getItemInMainHand() != null) {
                        ItemStack is = player.getInventory().getItemInMainHand();
                        String itemJson = convertItemStackToJsonRegular(is);
                        BaseComponent[] hoverEventComponents = new BaseComponent[]{new TextComponent(itemJson)};
                        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                            cm = new TextComponent(" " + is.getItemMeta().getDisplayName() + " ");
                        } else {
                            cm = new TextComponent(" " + is.getType().toString() + " ");
                        }
                        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents);
                        cm.setHoverEvent(event);
                    } else if (z.equalsIgnoreCase("[money]") && player.hasPermission("rpg.showitem")
                            && player.getInventory().getItemInMainHand() != null) {
                        cm = new TextComponent(" " + ChatColor.GREEN + "" + ChatColor.BOLD + "$" + money + " ");
                        cm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(ChatColor.BLUE + "Số Tiền Của " + ChatColor.RED + player.getName()).create()));
                    } else if (z.equalsIgnoreCase("[level]") && player.hasPermission("rpg.showitem")
                            && player.getInventory().getItemInMainHand() != null) {
                        cm = new TextComponent(" " + ChatColor.BLUE + "Lv" + ChatColor.BOLD + "" + player.getLevel() + " ");
                        cm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(ChatColor.GREEN + "Cấp độ Của " + ChatColor.RED + player.getName()).create()));
                    } else {
                        cm = new TextComponent(suffix + z + " ");
                        cm.setHoverEvent(msgh);
                    }
                    plevelc.addExtra(cm);

                }


                TextComponent staff = new TextComponent(ChatColor.RED + " ❖");
                if (e.getPlayer().hasPermission("rpg.staff")) {
                    staff.setBold(true);
                    staff.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(ChatColor.AQUA + "Đây là Staff").create()));

                }

                for (Player pl : Main.m.getServer().getOnlinePlayers()) {
                    com.earth2me.essentials.User receiver = ess.getUser(pl);
                    if (party) {
                        if (PartyUtils.isSameParty(player.getUniqueId(), pl.getUniqueId())) {
                            pl.spigot().sendMessage(plevelc);
                        }
                    } else {
                        if (receiver.isIgnoredPlayer(sender) && !receiver.isIgnoreExempt())
                            continue;
                        if (message.contains(pl.getName())) {
                            plevelc.setColor(ChatColor.RED);
                            plevelc.setBold(true);
                        }
                        if (e.getPlayer().hasPermission("rpg.staff")) {
                            pl.spigot().sendMessage(plevelc, staff);
                        } else if (!e.getPlayer().hasPermission("rpg.staff") || e.getPlayer().isOp() == false) {
                            pl.spigot().sendMessage(plevelc);

                        }
                    }
                }

                Main.m.getServer().getConsoleSender().sendMessage(
                        ChatColor.GREEN + "[ChatLogger] " + e.getPlayer().getName() + ": " + e.getMessage());

            }
        });

    }

    public String convertItemStackToJsonRegular(ItemStack is) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound compound = new NBTTagCompound();
        compound = nmsItemStack.save(compound);

        return compound.toString();
    }
}
