package me.johnnyht.race.Sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class PadSoundConfig {
    private final float minPitch;
    private final float maxPitch;
    private final float volume;

    public PadSoundConfig(float minPitch, float maxPitch, float volume) {
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
        this.volume = volume;
    }

    public static PadSoundConfig fromConfigSection(ConfigurationSection section) {
        float minPitch = (float) section.getDouble("minPitch", 1.0);
        float maxPitch = (float) section.getDouble("maxPitch", 1.0);
        float volume   = (float) section.getDouble("volume", 1.0);

        return new PadSoundConfig(minPitch, maxPitch, volume);
    }

    public float getRandomPitch() {
        return minPitch + (float) Math.random() * (maxPitch - minPitch);
    }

    public float getVolume() {
        return volume;
    }

    public void playSound(Player player, Sound sound) {
        playSound(player.getLocation(), player, sound);
    }

    public void playSound(Location location, Player player, Sound sound) {
        player.playSound(location, sound, volume, getRandomPitch());
    }
}
