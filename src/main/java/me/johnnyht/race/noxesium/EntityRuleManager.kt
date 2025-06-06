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

}