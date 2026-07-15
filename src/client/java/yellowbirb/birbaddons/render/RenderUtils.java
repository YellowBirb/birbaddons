package yellowbirb.birbaddons.render;

import net.minecraft.client.player.LocalPlayer;
import yellowbirb.birbaddons.render.shapes.CircleXZ;
import yellowbirb.birbaddons.render.shapes.InterCircleStrip;

import static java.lang.Math.*;

public class RenderUtils {
    public static void drawBoxyRing(double alpha, int deltaY, LocalPlayer player) {

        float angleMargin = 2.5F;
        float heightMargin = 2.5F;

        int boxyRingInnerColorR = 255;
        int boxyRingInnerColorG = 0;
        int boxyRingInnerColorB = 0;
        int boxyRingInnerColorA = 255;
        boolean boxyRingInnerVisibleThroughWalls = true;

        int boxyRingOuterCornerColorR = 0;
        int boxyRingOuterCornerColorG = 255;
        int boxyRingOuterCornerColorB = 0;
        int boxyRingOuterCornerColorA = 255;
        boolean boxyRingOuterCornerVisibleThroughWalls = false;

        int boxyRingOuterPlaneColorR = 0;
        int boxyRingOuterPlaneColorG = 255;
        int boxyRingOuterPlaneColorB = 0;
        int boxyRingOuterPlaneColorA = 50;
        boolean boxyRingOuterPlaneVisibleThroughWalls = false;

        double margin_rad = Math.toRadians(angleMargin);

        float radius1_1 = (float) (Math.abs(Math.tan(alpha + margin_rad) * (deltaY + heightMargin)));
        float radius1_2 = (float) (Math.abs(Math.tan(alpha + margin_rad) * (deltaY - heightMargin)));

        float radius2 = (float) (Math.abs(Math.tan(alpha) * deltaY));

        float radius3_1 = (float) (Math.abs(Math.tan(alpha - margin_rad) * (deltaY + heightMargin)));
        float radius3_2 = (float) (Math.abs(Math.tan(alpha - margin_rad) * (deltaY - heightMargin)));

        float playerX = (float) player.getX();
        float playerY = (float) player.getY();
        float playerZ = (float) player.getZ();

        float segmentLength = 0.5f;
        // resampling segmentAmount to draw clean InterCircleStrips
        int segmentAmount = max(8, (int) round((PI * max(radius1_1, max(radius1_2, max(radius2, max(radius3_1, radius3_2)))) * 2) / segmentLength));
        float lowY = playerY + deltaY - heightMargin;
        float highY = playerY + deltaY + heightMargin;

        RenderManager.add(new CircleXZ(radius1_1, segmentAmount, playerX, highY, playerZ, boxyRingOuterCornerColorR, boxyRingOuterCornerColorG, boxyRingOuterCornerColorB, boxyRingOuterCornerColorA, boxyRingOuterCornerVisibleThroughWalls));
        RenderManager.add(new CircleXZ(radius1_2, segmentAmount, playerX, lowY , playerZ, boxyRingOuterCornerColorR, boxyRingOuterCornerColorG, boxyRingOuterCornerColorB, boxyRingOuterCornerColorA, boxyRingOuterCornerVisibleThroughWalls));

        RenderManager.add(new CircleXZ(radius2, segmentAmount, playerX, playerY + deltaY, playerZ, boxyRingInnerColorR, boxyRingInnerColorG, boxyRingInnerColorB, boxyRingInnerColorA, boxyRingInnerVisibleThroughWalls));

        RenderManager.add(new CircleXZ(radius3_1, segmentAmount, playerX, highY, playerZ, boxyRingOuterCornerColorR, boxyRingOuterCornerColorG, boxyRingOuterCornerColorB, boxyRingOuterCornerColorA, boxyRingOuterCornerVisibleThroughWalls));
        RenderManager.add(new CircleXZ(radius3_2, segmentAmount, playerX, lowY , playerZ, boxyRingOuterCornerColorR, boxyRingOuterCornerColorG, boxyRingOuterCornerColorB, boxyRingOuterCornerColorA, boxyRingOuterCornerVisibleThroughWalls));

        RenderManager.add(new InterCircleStrip(radius1_1, radius1_2, segmentAmount, playerX, highY, lowY , playerZ, boxyRingOuterPlaneColorR, boxyRingOuterPlaneColorG, boxyRingOuterPlaneColorB, boxyRingOuterPlaneColorA, boxyRingOuterPlaneVisibleThroughWalls));
        RenderManager.add(new InterCircleStrip(radius3_1, radius3_2, segmentAmount, playerX, highY, lowY , playerZ, boxyRingOuterPlaneColorR, boxyRingOuterPlaneColorG, boxyRingOuterPlaneColorB, boxyRingOuterPlaneColorA, boxyRingOuterPlaneVisibleThroughWalls));
        RenderManager.add(new InterCircleStrip(radius1_1, radius3_1, segmentAmount, playerX, highY, highY, playerZ, boxyRingOuterPlaneColorR, boxyRingOuterPlaneColorG, boxyRingOuterPlaneColorB, boxyRingOuterPlaneColorA, boxyRingOuterPlaneVisibleThroughWalls));
        RenderManager.add(new InterCircleStrip(radius1_2, radius3_2, segmentAmount, playerX, lowY , lowY , playerZ, boxyRingOuterPlaneColorR, boxyRingOuterPlaneColorG, boxyRingOuterPlaneColorB, boxyRingOuterPlaneColorA, boxyRingOuterPlaneVisibleThroughWalls));
    }
}
