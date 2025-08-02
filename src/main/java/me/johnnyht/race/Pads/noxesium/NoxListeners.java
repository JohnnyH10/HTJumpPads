package me.johnnyht.race.Pads.noxesium;


import com.noxcrew.noxesium.api.protocol.rule.EntityRuleIndices;
import com.noxcrew.noxesium.api.qib.QibDefinition;
import com.noxcrew.noxesium.api.qib.QibEffect;
import me.johnnyht.race.HtRacePads;
import me.superneon4ik.noxesiumutils.config.NoxesiumUtilsConfig;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class NoxListeners implements Listener {



    NoxesiumUtilsConfig config;

    public NoxListeners(NoxesiumUtilsConfig config) {
        this.config = config;
    }


    @EventHandler
    public void onLoadChunk(ChunkLoadEvent e){

        var entities = e.getChunk().getEntities();
        for (Entity entity : entities){
            if (entity.getType() != EntityType.ITEM_FRAME) continue;
            if (entity instanceof ItemFrame itemFrame) {
                ItemStack item = itemFrame.getItem();
                if (item.getType() != Material.AIR) {
                    ItemMeta meta = item.getItemMeta();
                    String name = meta.getDisplayName();


                    List<String> parts = Arrays.asList(name.split("\\s+"));
                    String padType = parts.get(0).toLowerCase();


                    switch (padType){
                        case "jump":
                            QuibsDefinitions.makeQuibDifinition(parts,meta.getDisplayName(),"jump_boost",10, parts.get(1));
                            Entity interaction = QuibsDefinitions.makeInteractionPadEntity(entity.getLocation().subtract(-1.5, 0, -1.5));
                            var entityRule = HtRacePads.plugin.noxesiumUtils.getEntityRuleManager().getEntityRule(interaction, EntityRuleIndices.QIB_BEHAVIOR);
                            if (entityRule == null) continue;
                            entityRule.setValue(meta.getDisplayName());
                            break;
                        case "launch":
                            QuibsDefinitions.makeQuibDifinition(parts,meta.getDisplayName(),"jump_boost",10, parts.get(1));
                            Entity interaction = QuibsDefinitions.makeInteractionPadEntity(entity.getLocation().subtract(-1.5, 0, -1.5));
                            var entityRule = HtRacePads.plugin.noxesiumUtils.getEntityRuleManager().getEntityRule(interaction, EntityRuleIndices.QIB_BEHAVIOR);
                            if (entityRule == null) continue;
                            entityRule.setValue(meta.getDisplayName());
                            break;
                        case "speed":
                            QuibsDefinitions.makeQuibDifinition(parts,meta.getDisplayName(),"speed", Integer.parseInt(parts.get(1)), parts.get(2));
                            Entity interactionSpeed = QuibsDefinitions.makeInteractionPadEntity(entity.getLocation().subtract(-1.5, 0, -1.5));
                            var speedRule = HtRacePads.plugin.noxesiumUtils.getEntityRuleManager().getEntityRule(interactionSpeed, EntityRuleIndices.QIB_BEHAVIOR);
                            if (speedRule == null) continue;
                            speedRule.setValue(meta.getDisplayName());
                            break;
                        case "givepotioneffect":
                            QuibsDefinitions.makeQuibDifinition(parts,meta.getDisplayName(), parts.get(1) ,Integer.parseInt(parts.get(2)), parts.get(3));
                            Entity interactionEffect = QuibsDefinitions.makeInteractionPadEntity(entity.getLocation().subtract(-1.5, 0, -1.5));
                            var effectRule = HtRacePads.plugin.noxesiumUtils.getEntityRuleManager().getEntityRule(interactionEffect, EntityRuleIndices.QIB_BEHAVIOR);
                            if (effectRule == null) continue;
                            effectRule.setValue(meta.getDisplayName());
                            break;
                        case "removepotioneffect":

                            break;

                    }



                }
                return;
            }
        }
    }

    @EventHandler
    public void unLoadChunk(ChunkUnloadEvent e){

    }


}
