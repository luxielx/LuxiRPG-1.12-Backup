package skill;

import ChucNghiep.Classes;
import Luxi.SPlayer.RPGPlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Cast implements Listener {


    @EventHandler
    public void cast(SkillCastEvent e) {
        Player p = e.getPlayer();
        Classes pcl = RPGPlayerListener.get(p).getPlayerClass();
        int id = e.getSkillID();
        if (id == 1) {
            CastSkill(p, id);
        }
        if (id == 2) {
            CastSkill(p, id);
        }

        if (id == 3) {
            CastSkill(p, id);
        }

        if (id == 4) {
            CastSkill(p, id);
        }
    }

    public void CastSkill(Player p, int id) {
        Classes pcl = RPGPlayerListener.get(p).getPlayerClass();
        if (pcl == Classes.THUNDER) {
            switch (id) {
                case (1):
                    Thunder1.r(p);
                    break;
                case (2):
                    Thunder2.r(p);
                    break;
                case (3):
                    Thunder3.r(p);
                    break;
                case (4):
                    Thunder4.r(p);
                    break;
            }

        } else if (pcl == Classes.WIND) {
            switch (id) {
                case (1):
                    Wind1.r(p);
                    break;
                case (2):
                    Wind2.r(p);
                    break;
                case (3):
                    Wind3.r(p);
                    break;
                case (4):
                    Wind4.r(p);
                    break;
            }
        } else if (pcl == Classes.WATER) {
            switch (id) {
                case (1):
                    Water1.r(p);
                    break;
                case (2):
                    Water2.r(p);
                    break;
                case (3):
                    Water3.r(p);
                    break;
                case (4):
                    Water4.r(p);
                    break;
            }
        } else if (pcl == Classes.FIRE) {
            switch (id) {
                case (1):
                    Fire1.r(p);
                    break;
                case (2):
                    Fire2.r(p);
                    break;
                case (3):
                    Fire3.r(p);
                    break;
                case (4):
                    Fire4.r(p);
                    break;
            }
        }

    }
}
