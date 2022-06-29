package guild;

import Luxi.SPlayer.RPGPlayer;
import Luxiel.Main;
import guild.Guild;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class GuildTop {
    public static ArrayList<Guild> list = new ArrayList<>();
    public static void updateArrayList(){
        list.clear();
        File f =new File(Main.m.getDataFolder() + "/Guild/");
        for(File z : f.listFiles()){
            Guild g = new Guild(z.getName().replaceAll(".yml", ""));
            g.loadFromFile();
            list.add(g);
        }
        sortTop();
        return;
    }
    public static void sortTop(){
        list.sort(new Comparator<Guild>() {
            @Override
            public int compare(Guild o1, Guild o2) {
                return Integer.valueOf(o2.calcAvgLevel()).compareTo(o1.calcAvgLevel());
            }
        });
    }
    public static ArrayList<Guild> getTopList(){
        return list;
    }
    public static int getPlace(Guild g){
        for(Guild pzz : list){
            if(pzz.getName().equalsIgnoreCase(g.getName())){
                return list.indexOf(pzz);
            }
        }
        return 0;
    }
    public static ArrayList<Guild> getTop(int i , int z){
        ArrayList<Guild> l = new ArrayList<>();
        while(i+1<=z){
            if(list.size() <= i) break;
            l.add(list.get(i));
            i++;

        }
        return l;
    }
}
