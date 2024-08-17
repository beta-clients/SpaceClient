package net.minecraft.src.Space.event;
import net.minecraft.src.Space.Client;
import java.util.ArrayList;

public class EventRegistry {
    public static void handleEvent(Event e){
        for(int i = 0; i < Client.hackList.size(); ++i){
            if(Client.hacks[i].isToggled){
                Client.hacks[i].handleEvent(e);
            }
        }
    }
};