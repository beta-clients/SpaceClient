package net.minecraft.src.Space.event.events;
import net.minecraft.src.Space.event.Event;

public class EventMouseClick extends Event{
    public int blockX;
    public int blockY;
    public int blockZ;
    public EventMouseClick(int blockX, int blockY, int blockZ){
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }
};