package me.nobaboy.nobaaddons.util

import me.nobaboy.nobaaddons.util.NumberUtils.round
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.network.play.server.S2APacketParticles
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.Rotations
import net.minecraft.util.Vec3
import kotlin.math.*

/**
 * Taken and modified from SkyHanni under GNU Lesser General Public License 2.1
 * https://github.com/hannibal002/SkyHanni/blob/stable/LICENSE
 */
data class NobaVec(
    val x: Double,
    val y: Double,
    val z: Double
) {
    constructor() : this(0.0, 0.0, 0.0)
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())

    fun toBlockPos(): BlockPos = BlockPos(x, y, z)
    fun toVec3(): Vec3 = Vec3(x, y, z)

    fun distanceIgnoreY(other: NobaVec): Double = sqrt(distanceSqIgnoreY(other))
    fun distanceSqIgnoreY(other: NobaVec): Double {
        val dx = other.x - x
        val dz = other.z - z
        return (dx * dx + dz * dz)
    }

    fun distance(other: NobaVec): Double = sqrt(distanceSq(other))
    fun distance(x: Double, y: Double, z: Double): Double = distance(NobaVec(x, y, z))

    fun distanceSq(x: Double, y: Double, z: Double): Double = distanceSq(NobaVec(x, y, z))
    fun distanceSq(other: NobaVec): Double {
        val dx = other.x - x
        val dy = other.y - y
        val dz = other.z - z
        return (dx * dx + dy * dy + dz * dz)
    }

    operator fun plus(other: NobaVec) = NobaVec(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: NobaVec) = NobaVec(x - other.x, y - other.y, z - other.z)

    operator fun times(other: NobaVec) = NobaVec(x * other.x, y * other.y, z * other.z)
    operator fun times(scalar: Double) = NobaVec(x * scalar, y * scalar, z * scalar)
    operator fun times(scalar: Int) = NobaVec(x * scalar, y * scalar, z * scalar)

    operator fun div(other: NobaVec) = NobaVec(x / other.x, y / other.y, z / other.z)
    operator fun div(scalar: Double) = NobaVec(x / scalar, y / scalar, z / scalar)

    fun add(x: Int = 0, y: Int = 0, z: Int = 0): NobaVec = NobaVec(this.x + x, this.y + y, this.z + z)
    fun add(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): NobaVec =
        NobaVec(this.x + x, this.y + y, this.z + z)

    override fun toString() = "NobaVec{x=$x, y=$y, z=$z}"

    fun dot(other: NobaVec): Double = (x * other.x) + (y * other.y) + (z * other.z)

    fun cosAngle(other: NobaVec) = this.normalize().dot(other.normalize())
    fun radianAngle(other: NobaVec) = acos(this.cosAngle(other))
    fun degreeAngle(other: NobaVec) = Math.toDegrees(this.radianAngle(other))

    fun normalize() = length().let { NobaVec(x / it, y / it, z / it) }
    fun inverse() = NobaVec(1.0 / x, 1.0 / y, 1.0 / z)

    fun min() = min(x, min(y, z))
    fun max() = max(x, max(y, z))

    fun minOfEach(other: NobaVec) = NobaVec(min(x, other.x), min(y, other.y), min(z, other.z))
    fun maxOfEach(other: NobaVec) = NobaVec(max(x, other.x), max(y, other.y), max(z, other.z))

    fun formatWithAccuracy(accuracy: Int, splitChar: String = " "): String {
        return if (accuracy == 0) {
            val x = round(x).toInt()
            val y = round(y).toInt()
            val z = round(z).toInt()
            "$x$splitChar$y$splitChar$z"
        } else {
            val x = (round(x * accuracy) / accuracy)
            val y = (round(y * accuracy) / accuracy)
            val z = (round(z * accuracy) / accuracy)
            "$x$splitChar$y$splitChar$z"
        }
    }

    fun toCleanString(): String = "$x $y $z"

    fun lengthSquared(): Double = x * x + y * y + z * z
    fun length(): Double = sqrt(this.lengthSquared())

    fun isZero(): Boolean = x == 0.0 && y == 0.0 && z == 0.0

    fun clone(): NobaVec = NobaVec(x, y, z)

    fun toDoubleArray(): Array<Double> = arrayOf(x, y, z)
    fun toFloatArray(): Array<Float> = arrayOf(x.toFloat(), y.toFloat(), z.toFloat())

    fun equalsIgnoreY(other: NobaVec) = x == other.x && z == other.z

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        return (other as? NobaVec)?.let {
            x == it.x && y == it.y && z == it.z
        } ?: super.equals(other)
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    fun round(decimals: Int) = NobaVec(x.round(decimals), y.round(decimals), z.round(decimals))

    fun roundToBlock(): NobaVec {
        val x = (x - .499999).round(0)
        val y = (y - .499999).round(0)
        val z = (z - .499999).round(0)
        return NobaVec(x, y, z)
    }

    fun roundLocation(): NobaVec {
        val x = if (this.x < 0) x.toInt() - 1 else x.toInt()
        val y = y.toInt() - 1
        val z = if (this.z < 0) z.toInt() - 1 else z.toInt()
        return NobaVec(x, y, z)
    }

    fun interpolate(other: NobaVec, factor: Double): NobaVec {
        require(factor in 0.0..1.0) { "Factor must be between 0 and 1: $factor" }

        val x = (1 - factor) * this.x + factor * other.x
        val y = (1 - factor) * this.y + factor * other.y
        val z = (1 - factor) * this.z + factor * other.z

        return NobaVec(x, y, z)
    }

    fun slope(other: NobaVec, factor: Double) = this + (other - this).scale(factor)
    fun scale(scalar: Double): NobaVec = NobaVec(scalar * x, scalar * y, scalar * z)

    fun applyTranslationToGL() {
        GlStateManager.translate(x, y, z)
    }

    fun toBoundingBoxWithOffset(offX: Double, offY: Double, offZ: Double) =
        AxisAlignedBB(x, y, z, x + offX, y + offY, z + offZ)

    fun axisAlignedTo(other: NobaVec) = AxisAlignedBB(x, y, z, other.x, other.y, other.z)
    fun raise(offset: Double): NobaVec = copy(y = y + offset)

    fun negate() = NobaVec(-x, -y, -z)
    fun rotateXY(theta: Double) = NobaVec(x * cos(theta) - y * sin(theta), x * sin(theta) + y * cos(theta), z)
    fun rotateXZ(theta: Double) = NobaVec(x * cos(theta) + z * sin(theta), y, -x * sin(theta) + z * cos(theta))
    fun rotateYZ(theta: Double) = NobaVec(x, y * cos(theta) - z * sin(theta), y * sin(theta) + z * cos(theta))

    fun nearestPointOnLine(start: NobaVec, end: NobaVec): NobaVec {
        var direction = end - start
        val toPoint = this - start

        val dotProduct = direction.lengthSquared()
        var t = 0.0
        if (dotProduct != t) t = (toPoint.dot(direction) / dotProduct).coerceIn(0.0, 1.0)

        direction *= t
        direction += start
        return direction
    }

    fun distanceToLine(startPos: NobaVec, endPos: NobaVec): Double {
        return (nearestPointOnLine(startPos, endPos) - this).lengthSquared()
    }

    fun crossProduct(other: NobaVec): NobaVec {
        val crossX = this.y * other.z - this.z * other.y
        val crossY = this.z * other.x - this.x * other.z
        val crossZ = this.x * other.y - this.y * other.x
        return NobaVec(crossX, crossY, crossZ)
    }

    fun offsetBy(other: Vec3): NobaVec = NobaVec(x + other.xCoord, y + other.yCoord, z + other.zCoord)

    companion object {
        fun fromYawPitch(yaw: Double, pitch: Double): NobaVec {
            val yawRad = (yaw + 90) * Math.PI / 180
            val pitchRad = (pitch + 90) * Math.PI / 180

            val x = sin(pitchRad) * cos(yawRad)
            val y = sin(pitchRad) * sin(yawRad)
            val z = cos(pitchRad)
            return NobaVec(x, z, y)
        }

        // Format: "x:y:z"
        fun fromString(string: String): NobaVec {
            val (x, y, z) = string.split(":").map { it.toDouble() }
            return NobaVec(x, y, z)
        }

        fun blockBelowPlayer() = LocationUtils.playerLocation().roundToBlock().add(y = -1.0)

        val expandVector = NobaVec(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026)
    }
}

// Extension functions for converting to NobaVec
fun BlockPos.toNobaVec(): NobaVec = NobaVec(x, y, z)

fun Entity.getNobaVec(): NobaVec = NobaVec(posX, posY, posZ)
fun Entity.getPrevNobaVec(): NobaVec = NobaVec(prevPosX, prevPosY, prevPosZ)
fun Entity.getMotionNobaVec(): NobaVec = NobaVec(motionX, motionY, motionZ)

fun Vec3.toNobaVec(): NobaVec = NobaVec(xCoord, yCoord, zCoord)

fun Rotations.toNobaVec(): NobaVec = NobaVec(x, y, z)

fun S2APacketParticles.toNobaVec() = NobaVec(xCoordinate, yCoordinate, zCoordinate)

fun Array<Double>.toNobaVec(): NobaVec = NobaVec(this[0], this[1], this[2])

fun RenderUtils.translate(vec: NobaVec) = GlStateManager.translate(vec.x, vec.y, vec.z)

fun AxisAlignedBB.expand(vec: NobaVec): AxisAlignedBB = this.expand(vec.x, vec.y, vec.z)
fun AxisAlignedBB.expand(amount: Double): AxisAlignedBB = this.expand(amount, amount, amount)