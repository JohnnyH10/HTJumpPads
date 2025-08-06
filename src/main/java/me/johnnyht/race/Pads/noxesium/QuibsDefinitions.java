package me.johnnyht.race.Pads.noxesium;

import com.noxcrew.noxesium.api.qib.QibDefinition;
import com.noxcrew.noxesium.api.qib.QibEffect;
import me.superneon4ik.noxesiumutils.config.NoxesiumUtilsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

public class QuibsDefinitions {


    private static NoxesiumUtilsConfig config;

    public QuibsDefinitions(NoxesiumUtilsConfig config) {
        QuibsDefinitions.config = config;
    }

    public static void makeQuibDifinition(List<String> parts, String padItemName, String effectName, int timeTicks, String aplifier){

        if (aplifier == "infinite") {
            aplifier = "9999999999999";
        }

        int effectAplifier = Integer.parseInt(aplifier);

        var effect = new QibEffect.GivePotionEffect(
                "minecraft",
                effectName,
                timeTicks, //Time Ticks I think
                effectAplifier, //This line gets the amplifier of the effect
                true,
                true,
                true
        );

        var def = new QibDefinition(
                effect,
                null,
                effect,
                null,
                false
        );

        config.getQibDefinitions().put(padItemName, def);
    }

    public static Entity makeInteractionPadEntity(Location loc){
        Entity interaction = Bukkit.getWorld(loc.getWorld().getName()).spawnEntity(loc, EntityType.INTERACTION);
        interaction.getBoundingBox().expand(2, 0, 2); // probs wrong

        return interaction;
    }

}
