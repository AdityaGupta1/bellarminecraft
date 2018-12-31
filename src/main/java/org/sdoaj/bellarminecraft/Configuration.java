package org.sdoaj.bellarminecraft;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID)
public class Configuration {
    @Config.Name("spawn x")
    public static double spawnX;
    @Config.Name("spawn z")
    public static double spawnZ;

    public static Vec3d getSpawn() {
        return new Vec3d(spawnX, 0, spawnZ);
    }

    @Config.Name("spawn protected radius")
    public static double spawnProtectedRadius;

    public static boolean isProtected(Vec3d pos) {
        return pos.subtract(0, pos.y, 0).distanceTo(getSpawn()) <= spawnProtectedRadius;
    }

    public static boolean isProtected(Vec3i pos) {
        return isProtected(new Vec3d(pos));
    }
}
