🚀 PAD TYPES & USAGE

Get All Pads Command: /givepads
Effect: Gives the player a shulker box containing one of each pad item


1. Jump Pad
Name Format: jump <power>


Triggered by: Walking onto the pad


Effect: Applies a Jump Boost effect for a brief moment. Then, when the player jumps, it launches them visually with particles.


Example:
jump 3



2. Launch Pad
Name Format: launch <horizontalSpeed> <verticalSpeed>


Triggered by: Jumping on the pad


Effect: Launches the player forward and upward with a directional velocity


Example:
launch 1.5 1.2



3. Speed Pad
Name Format: speed <duration> <amplifier>


Triggered by: Walking on the pad


Effect: Applies a speed effect


Example:
speed 20 2
 (20 ticks = 1 seconds)



4. Equip Pad
Name Format: equip <slot>


Triggered by: Walking on the pad


Effect: Equips the item in the frame to the specified slot


Valid Slots: head, chest, legs, boots, hand


Example:
equip chest
equip hand
 (Puts the item in the player’s chestplate slot)



5. Horse Pad
Name Format: horse <speed> <jump>


Triggered by: Walking on the pad


Effect: Spawns a tamed horse and mounts the player on it


Example:

horse 0.3 0.8

6. Pig Pad
Name Format: pig <speed>


Triggered by: Walking on the pad


Effect: Spawns a saddled pig and mounts the player, gives carrot on a stick


Example:
pig 0.25

7. Strider Pad
Name Format: strider <speed>


Triggered by: Walking on the pad


Effect: Spawns a saddled strider and gives warped fungus on a stick


Example:
strider 0.2

8. Camel Pad
Name Format: camel <speed> <jump>


Triggered by: Walking on the pad


Effect: Spawns and mounts a camel


Example:
camel 0.3 0.6


9. Ice Boat Pad
Name Format: iceboat


Triggered by: Walking on the pad


Effect: Spawns a boat and mounts the player


Example:
iceboat

10. GivePotionEffect Pad

Name Format: givepotioneffect <effect> <duration|infinite> <amplifier>

Triggered by: Walking on the pad

Effect: Applies a custom potion effect to the player. Duration can be in ticks or "infinite" for permanent effects.
Example:

givepotioneffect SPEED 200 2  
givepotioneffect NIGHT_VISION infinite 1

11. RemovePotionEffect Pad

Name Format: removepotioneffect

Triggered by: Walking on the pad

Effect: Clears all active potion effects from the player.
Example:
removepotioneffect

12. KillVehicle Pad
Name Format: killvehicle

Triggered by: Walking on the pad

Effect: Instantly removes any vehicle the player is riding (boats, horses, pigs, etc.)
Example:
killvehicle

13. Clear Inventory Pad

Name Format: clearinv

Triggered by: Walking onto the pad (not by jumping)

Effect: Clears the player's entire inventory and sends a confirmation message.

Example: 
clearinv

14. Swim Launch Pad
Name Format: swimlaunch <horizontalStrength> <verticalStrength>
Triggered by: Walking onto the pad
Effect: Launches the player in the direction they are looking. The horizontal strength is determined by <horizontalStrength> and the vertical velocity by <verticalStrength>. Spawns cloud particles and sends a "High Jump!" message.
Example: swimlaunch 2.0 0.0

🔁 Cooldowns
Every pad has a short cooldown (5 ticks) per player to prevent multiple rapid activations.



💡 Tips


Ensure the item has a custom name that matches the expected command format.


Keep the area around the pad clear to ensure correct detection.


<for devs>Debug issues using server logs if a pad doesn't activate—error messages will appear for malformed inputs.

