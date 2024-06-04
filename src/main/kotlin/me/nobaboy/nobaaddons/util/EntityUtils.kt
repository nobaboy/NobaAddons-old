package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import me.nobaboy.nobaaddons.util.LocationUtils.canBeSeen
import me.nobaboy.nobaaddons.util.LocationUtils.distanceTo
import me.nobaboy.nobaaddons.util.LocationUtils.distanceToIgnoreY
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer

/**
 * Taken and modified from SkyHanni under GNU Lesser General Public License 2.1
 * https://github.com/hannibal002/SkyHanni/blob/stable/LICENSE
 */
object EntityUtils {
    fun EntityPlayer.isRealPlayer() = uniqueID?.let { it.version() == 4 } ?: false

    fun Entity.canBeSeen(radius: Double = 150.0) = getNobaVec().add(y = 0.5).canBeSeen(radius)

    inline fun <reified R : Entity> getEntities(): Sequence<R> = getAllEntities().filterIsInstance<R>()

    fun getAllEntities(): Sequence<Entity> = mc.theWorld?.loadedEntityList?.let {
        if (mc.isCallingFromMinecraftThread) it else it.toMutableList()
    }?.asSequence()?.filterNotNull() ?: emptySequence()

    fun getEntityByID(entityId: Int) = mc.thePlayer?.entityWorld?.getEntityByID(entityId)

    fun getPlayerEntities(): MutableList<EntityOtherPlayerMP> {
        val list = mutableListOf<EntityOtherPlayerMP>()

        for (entity in mc.theWorld.playerEntities) {
            if (entity.isRealPlayer() && entity is EntityOtherPlayerMP) {
                list.add(entity)
            }
        }
        return list
    }

    inline fun <reified T : Entity> getEntitiesNear(location: NobaVec, radius: Double): Sequence<T> =
        getEntities<T>().filter { it.distanceTo(location) < radius }

    inline fun <reified T : Entity> getEntitiesNearIgnoreY(location: NobaVec, radius: Double): Sequence<T> =
        getEntities<T>().filter { it.distanceToIgnoreY(location) < radius }

    inline fun <reified T : Entity> getEntitiesNearPlayer(radius: Double): Sequence<T> =
        getEntitiesNear<T>(LocationUtils.playerLocation(), radius)
}