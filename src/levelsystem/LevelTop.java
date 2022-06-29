package levelsystem;

import Luxi.SPlayer.RPGPlayer;
import Luxiel.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class LevelTop {
    public static ArrayList<RPGPlayer> list = new ArrayList<>();
    public static void updateArrayList(){
        list.clear();
        File f =new File(Main.m.getDataFolder() + "/PlayerData/");
        for(File z : f.listFiles()){
            RPGPlayer p = new RPGPlayer(UUID.fromString( z.getName().replaceAll(".yml", "")).toString());
            p.loadFromFile();
            list.add(p);
        }
        sortTop();
        return;
    }
    public static void sortTop(){
        list.sort(new Comparator<RPGPlayer>() {
            @Override
            public int compare(RPGPlayer o1, RPGPlayer o2) {
                return Integer.valueOf(o2.getLevel()).compareTo(o1.getLevel());
            }
        });
    }
    public static ArrayList<RPGPlayer> getTopList(){
        return list;
    }
    public static int getPlace(UUID p){
        RPGPlayer pz = new RPGPlayer(p.toString());
        for(RPGPlayer pzz : list){
            if(pzz.uuid.toString().equalsIgnoreCase(pz.uuid.toString())){
                return list.indexOf(pzz);

            }
        }
        return 0;
    }
    public static ArrayList<RPGPlayer> getTop(int i , int z){
        ArrayList<RPGPlayer> l = new ArrayList<>();
        while(i+1<=z){
            if(list.size() <= i) break;
            l.add(list.get(i));
            i++;

        }
        return l;
    }
}
