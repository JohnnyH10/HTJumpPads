package me.johnnyht.race.noxesium

import com.noxcrew.noxesium.paper.api.NoxesiumManager
import com.noxcrew.noxesium.paper.api.RuleFunction
import com.noxcrew.noxesium.paper.api.RuleHolder
import com.noxcrew.noxesium.paper.api.event.NoxesiumPlayerRegisteredEvent
import com.noxcrew.noxesium.paper.api.network.clientbound.ClientboundSetExtraEntityDataPacket
import com.noxcrew.noxesium.paper.api.rule.RemoteServerRule
import io.papermc.paper.event.player.PlayerTrackEntityEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityRemoveEvent
import java.util.WeakHashMap

public class EntityRuleManager(private val manager: NoxesiumManager) : Listener {

    private val entities = WeakHashMap<Entity, RuleHolder>()
    private var task: Int = -1

    /**
     * Registers this manager.
     */
    public fun register() {
        Bukkit.getPluginManager().registerEvents(this, manager.plugin)

        // Send rule updates once a tick in a batch
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(manager.plugin, {
            for ((entity, holder) in entities) {
                if (!holder.needsUpdate) continue

                for (player in Bukkit.getOnlinePlayers()) {
                    if (!player.canSee(entity)) continue
                    manager.sendPacket(player,
                        ClientboundSetExtraEntityDataPacket(
                            entity.entityId,
                            holder.rules
                                .filter { manager.entityRules.isAvailable(it.key, manager.getProtocolVersion(player) ?: -1) }
                                .ifEmpty { null }
                                ?.mapValues { (_, rule) ->
                                    { buffer ->
                                        @Suppress("UNCHECKED_CAST")
                                        (rule as RemoteServerRule<Any>).write(rule.value as Any, buffer)
                                    }
                                } ?: continue
                        )
                    )
                }

                holder.markAllUpdated()
            }
        }, 1, 1)
    }

    /**
     * Unregisters this manager.
     */
    public fun unregister() {
        HandlerList.unregisterAll(this)
        Bukkit.getScheduler().cancelTask(task)
    }

    /** Returns the given [rule] for [entity]. */
    public fun <T : Any> getEntityRule(entity: Entity, rule: RuleFunction<T>): RemoteServerRule<T>? =
        getEntityRule(entity, rule.index)

    /** Returns the given [ruleIndex] for [entity]. */
    public fun <T : Any> getEntityRule(entity: Entity, ruleIndex: Int): RemoteServerRule<T>? =
        entities.computeIfAbsent(entity) { RuleHolder() }.let { holder ->
            manager.entityRules.create(ruleIndex, holder)
        }

    /**
     * Send all entities' data when a player gets registered with Noxesium so
     * they can properly see entities that were sent to them already.
     */
    @EventHandler
    public fun onNoxesiumPlayerRegistered(e: NoxesiumPlayerRegisteredEvent) {
        val player = e.player
        val protocol = e.protocolVersion

        for ((entity, holder) in entities) {
            if (!player.canSee(entity)) continue
            manager.sendPacket(player,
                ClientboundSetExtraEntityDataPacket(
                    entity.entityId,
                    holder.rules
                        .filter { manager.entityRules.isAvailable(it.key, protocol) }
                        .ifEmpty { null }
                        ?.mapValues { (_, rule) ->
                            { buffer ->
                                @Suppress("UNCHECKED_CAST")
                                (rule as RemoteServerRule<Any>).write(rule.value as Any, buffer)
                            }
                        } ?: continue
                )
            )
        }
    }

    /**
     * When an entity starts being shown to a player we
     * send its data along as well.
     * This will also send data about needed entities
     * when the player logs in / changes worlds.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public fun onEntityShown(e: PlayerTrackEntityEvent) {
        val holder = entities[e.entity] ?: return
        val protocol = manager.getProtocolVersion(e.player) ?: return

        Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, {
            manager.sendPacket(e.player,
                ClientboundSetExtraEntityDataPacket(
                    e.entity.entityId,
                    holder.rules
                        .filter { manager.entityRules.isAvailable(it.key, protocol) }
                        .ifEmpty { null }
                        ?.mapValues { (_, rule) ->
                            { buffer ->
                                @Suppress("UNCHECKED_CAST")
                                (rule as RemoteServerRule<Any>).write(rule.value as Any, buffer)
                            }
                        } ?: return@scheduleSyncDelayedTask
                )
            )
        }, 1)
    }

    /**
     * Remove data stored for an entity when the entity
     * gets removed. This also happens indirectly because we
     * use a WeakHashMap.
     */
    @EventHandler
    public fun onEntityRemoved(e: EntityRemoveEvent) {
        entities.remove(e.entity)
    }
}