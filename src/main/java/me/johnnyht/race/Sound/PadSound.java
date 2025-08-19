package me.johnnyht.race.Sound;

import me.johnnyht.race.HtRacePads;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PadSound {
    private static final Map<UUID, Map<String, Long>> playerSoundCooldowns = new HashMap<>();
    private static final long COOLDOWN_TICKS = 10L;
    private static final SoundCategory soundCategory = SoundCategory.AMBIENT;
    //ToDo we need to put the sounds and the sound soundCategory into the config

    public static void playSoundAtPlayer(Player player, String sound, float volume, float pitch) {
        UUID playerUUID = player.getUniqueId();
        long currentTick = HtRacePads.getInstance().getServer().getCurrentTick();

        Map<String, Long> soundCooldowns = playerSoundCooldowns.computeIfAbsent(playerUUID, k -> new HashMap<>());

        if (soundCooldowns.containsKey(sound)) {
            long lastPlayedTick = soundCooldowns.get(sound);
            if (currentTick - lastPlayedTick < COOLDOWN_TICKS) {
                return;
            }
        }

        player.playSound(player.getLocation(), sound, soundCategory, volume, pitch);
        soundCooldowns.put(sound, currentTick);
    }
}
