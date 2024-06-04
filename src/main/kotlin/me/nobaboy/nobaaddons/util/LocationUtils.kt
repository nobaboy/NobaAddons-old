package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.NobaAddons.Companion.mc
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import kotlin.math.max
import kotlin.math.min

/**
 * Taken and modified from SkyHanni under GNU Lesser General Public License 2.1
 * https://github.com/hannibal002/SkyHanni/blob/stable/LICENSE
 */
object LocationUtils {
    fun canSee(source: NobaVec, target: NobaVec) =
        mc.theWorld.rayTraceBlocks(source.toVec3(), target.toVec3(), false, true, true) == null

    fun playerLocation() = mc.thePlayer.getNobaVec()

    fun NobaVec.distanceToPlayer() = distance(playerLocation())
    fun NobaVec.distanceSqToPlayer() = distanceSq(playerLocation())

    fun NobaVec.distanceToPlayerIgnoreY() = distanceIgnoreY(playerLocation())
    fun NobaVec.distanceToPlayerSqIgnoreY() = distanceSqIgnoreY(playerLocation())

    fun Entity.distanceToPlayer() = getNobaVec().distanceToPlayer()

    fun Entity.distanceTo(location: NobaVec) = getNobaVec().distance(location)
    fun Entity.distanceTo(other: Entity) = getNobaVec().distance(other.getNobaVec())

    fun Entity.distanceToIgnoreY(location: NobaVec) = getNobaVec().distanceIgnoreY(location)

    fun playerEyeLocation(): NobaVec {
        val player = mc.thePlayer
        val vec = player.getNobaVec()
        return vec.add(y = player.getEyeHeight().toDouble())
    }

    fun AxisAlignedBB.isInside(vec: NobaVec) = isVecInside(vec.toVec3())
    fun AxisAlignedBB.isPlayerInside() = isInside(playerLocation())

    fun NobaVec.canBeSeen(radius: Double = 150.0): Boolean {
        val a = playerEyeLocation()
        val b = this
        val noBlocks = canSee(a, b)
        val notTooFar = a.distance(b) < radius
        val inFov = true // TODO add Frustum "Frustum().isBoundingBoxInFrustum(entity.entityBoundingBox)"
        return noBlocks && notTooFar && inFov
    }

    fun NobaVec.canBeSeen(yOffsetRange: IntRange, radius: Double = 150.0): Boolean =
        yOffsetRange.any { offset ->
            this.add(y = offset).canBeSeen(radius)
        }

    fun AxisAlignedBB.minBox() = NobaVec(minX, minY, minZ)
    fun AxisAlignedBB.maxBox() = NobaVec(maxX, maxY, maxZ)

    fun AxisAlignedBB.rayIntersects(origin: NobaVec, direction: NobaVec): Boolean {
        // Reference for Algorithm https://tavianator.com/2011/ray_box.html
        val rayDirectionInverse = direction.inverse()
        val t1 = (this.minBox() - origin) * rayDirectionInverse
        val t2 = (this.maxBox() - origin) * rayDirectionInverse

        val tmin = max(t1.minOfEach(t2).max(), Double.NEGATIVE_INFINITY)
        val tmax = min(t1.maxOfEach(t2).min(), Double.POSITIVE_INFINITY)
        return tmax >= tmin && tmax >= 0.0
    }

    fun AxisAlignedBB.union(aabbs: List<AxisAlignedBB>?): AxisAlignedBB? {
        if (aabbs.isNullOrEmpty()) {
            return null
        }

        var minX = this.minX
        var minY = this.minY
        var minZ = this.minZ
        var maxX = this.maxX
        var maxY = this.maxY
        var maxZ = this.maxZ

        aabbs.forEach { aabb ->
            if (aabb.minX < minX) minX = aabb.minX
            if (aabb.minY < minY) minY = aabb.minY
            if (aabb.minZ < minZ) minZ = aabb.minZ
            if (aabb.maxX > maxX) maxX = aabb.maxX
            if (aabb.maxY > maxY) maxY = aabb.maxY
            if (aabb.maxZ > maxZ) maxZ = aabb.maxZ
        }

        return AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    fun AxisAlignedBB.dimensions() = maxBox() - minBox()

    fun AxisAlignedBB.center() = dimensions() * 0.5 + minBox()

    fun AxisAlignedBB.topCenter() = center().add(y = (maxY - minY) / 2)

    fun AxisAlignedBB.clampTo(other: AxisAlignedBB): AxisAlignedBB {
        val minX = max(this.minX, other.minX)
        val minY = max(this.minY, other.minY)
        val minZ = max(this.minZ, other.minZ)
        val maxX = min(this.maxX, other.maxX)
        val maxY = min(this.maxY, other.maxY)
        val maxZ = min(this.maxZ, other.maxZ)
        return AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
    }
}