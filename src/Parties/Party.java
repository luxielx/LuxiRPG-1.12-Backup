package Parties;

import Luxiel.Main;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static Parties.Party.PartyRank.*;

public class Party {
    public static HashMap<UUID, Party> partylist = new HashMap<>();
    public UUID leader;
    public ArrayList<UUID> members;
    public String name;
    public PartyRank partyrank;

    public Party(UUID leader, String name) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader);
        this.name = name;
        this.partyrank = calcPartyRank();
    }

    public PartyRank calcPartyRank() {
        int totallevel = 0;
        for (UUID p : this.members) {
            if (!Bukkit.getPlayer(p).isOnline()) continue;
            totallevel += Bukkit.getPlayer(p).getLevel();
        }
        if (totallevel <= 10 * getMembers().size()) return D;
        if (totallevel <= 30 * getMembers().size()) return C;
        if (totallevel <= 50 * getMembers().size()) return B;
        if (totallevel <= 70 * getMembers().size()) return A;
        if (totallevel >= 70 * getMembers().size()) return S;
        return D;
    }

    public int calcAvgLevel() {
        int totallevel = 0;
        for (UUID p : this.members) {
            if (!Bukkit.getPlayer(p).isOnline()) continue;
            totallevel += Bukkit.getPlayer(p).getLevel();
        }
        return Integer.valueOf(totallevel / members.size());
    }

    public int calcTotalLevel() {
        int totallevel = 0;
        for (UUID p : this.members) {
            if (!Bukkit.getPlayer(p).isOnline()) continue;
            totallevel += Bukkit.getPlayer(p).getLevel();
        }
        return Integer.valueOf(totallevel);
    }


     public void updateParty() {

//        if (Main.dxl.getPlayerGroup(Bukkit.getPlayer(leader)) == null) {
//            Main.dxl.createGroup(Bukkit.getPlayer(leader), name);
//        }else{
//            PlayerGroup pg = Main.dxl.getPlayerGroup(Bukkit.getPlayer(leader));
//            ArrayList<UUID> remove = new ArrayList<>();
//            pg.getMembers().forEach(mm ->{
//                if(!members.contains(mm)) pg.removeMember(Bukkit.getPlayer(mm));
//            });
//            remove.forEach(en ->{
//                pg.removeMember(en);
//            });
//        }
        for (UUID z : members) {
//            Player p = Bukkit.getPlayer(z);
//            if (Main.dxl.getPlayerGroup(p) == null) {
//                if (Main.dxl.getPlayerGroup(Bukkit.getPlayer(leader)) == null) {
//                    Main.dxl.createGroup(Bukkit.getPlayer(leader), name);
//                } else {
//                    Main.dxl.getPlayerGroup(Bukkit.getPlayer(leader)).addMember(p);
//                }
//            }
            partylist.put(z, this);
        }

    }

    public void setPartyRank(PartyRank rank) {
        this.partyrank = rank;
        AnnouceAll(ChatColor.WHITE + "Tổ đội của bạn đã đạt cấp độ " + getRankColor(rank) + rank.name());
        updateParty();
    }

    public void addMember(UUID player) {
        if (this.members.size() >= 5) {
            return;
        }
        if (this.members.contains(player)) return;
        members.add(player);
        partylist.put(player, this);
        this.partyrank = calcPartyRank();
        AnnouceAll(ChatColor.WHITE + Bukkit.getOfflinePlayer(player).getName() + ChatColor.RED + " đã tham gia tổ đội!!");
        updateParty();
    }

    public void removeMember(UUID player) {
        members.remove(player);
        partylist.remove(player);
        this.partyrank = calcPartyRank();
        AnnouceAll(ChatColor.WHITE + Bukkit.getOfflinePlayer(player).getName() + ChatColor.RED + " đã rời khỏi tổ đội!!");
        updateParty();
    }

    public UUID getLeader() {
        return this.leader;
    }

    public void setLeader(UUID newleader) {
        this.leader = newleader;
        AnnouceAll(ChatColor.WHITE + Bukkit.getOfflinePlayer(newleader).getName() + ChatColor.RED + " đã trở thành đội trưởng!!");
        updateParty();
    }

    public ArrayList<UUID> getMembers() {
        return this.members;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        AnnouceAll(ChatColor.WHITE + "Tổ đội của bạn đã trở thành " + getRankColor(calcPartyRank()) + name);
        updateParty();
    }

    public void disband() {
        ArrayList<UUID> memberclone = new ArrayList<>();
        memberclone.addAll(members);
        for (UUID m : memberclone) {
            removeMember(m);
            Bukkit.getPlayer(m).sendMessage(ChatColor.RED + "Tổ đội đã giải tán!");
            Utils.sendTitleBar(Bukkit.getPlayer(m), "", ChatColor.RED + "Tổ đội đã giải tán!", 5, 20, 5);
        }
    }

    public void AnnouceAll(String message) {
        for (UUID m : members) {
            Bukkit.getPlayer(m).sendMessage(message);
        }
    }

    public void AnnouceAllTitle(String title, String subtitle) {
        for (UUID m : members) {
            Utils.sendTitleBar(Bukkit.getPlayer(m), title, subtitle);

        }
    }

    public void AnnouceAllActionBar(String message) {
        for (UUID m : members) {
            Utils.sendActionBar(Bukkit.getPlayer(m), message);
        }
    }

    public ChatColor getColor() {
        return getRankColor(calcPartyRank());
    }

    public ChatColor getRankColor(PartyRank rank) {
        switch (rank) {
            case S:
                return ChatColor.GOLD;
            case A:
                return ChatColor.RED;
            case B:
                return ChatColor.AQUA;
            case C:
                return ChatColor.GREEN;
            case D:
                return ChatColor.GRAY;
        }
        return ChatColor.GRAY;
    }

    enum PartyRank {
        D, C, B, A, S
    }


}
