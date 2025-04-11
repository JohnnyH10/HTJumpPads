package me.JohnnyHT.runningOnRandomJumpPads.PadManager.PadType;

import me.JohnnyHT.runningOnRandomJumpPads.PadManager.PadLogic;
import me.JohnnyHT.runningOnRandomJumpPads.PadManager.PadStates;
import org.bukkit.Effect;

public class EffectPad extends PadLogic {
    public EffectPad(String name, PadStates padStates, Effect potionEffect, int time, int amplifier) {
        super(name, padStates);
    }



}
