package yellowbirb.birbaddons.render;

import yellowbirb.birbaddons.render.shapes.CircleXZ;
import yellowbirb.birbaddons.render.shapes.InterCircleStrip;

import static java.lang.Math.*;

public class RenderUtils {
    public static void drawBoxyRing(double alpha, int deltaY, float playerX, float playerY, float playerZ,
                                    float angleMargin, float heightMargin,
                                    int boxyRingInnerColorR, int boxyRingInnerColorG, int boxyRingInnerColorB, int boxyRingInnerColorA, boolean boxyRingInnerVisibleThroughWalls,
                                    int boxyRingOuterCornerColorR, int boxyRingOuterCornerColorG, int boxyRingOuterCornerColorB, int boxyRingOuterCornerColorA, boolean boxyRingOuterCornerVisibleThroughWalls,
                                    int boxyRingOuterPlaneColorR, int boxyRingOuterPlaneColorG, int boxyRingOuterPlaneColorB, int boxyRingOuterPlaneColorA, boolean boxyRingOuterPlaneVisibleThroughWalls) {

        double margin_rad = Math.toRadians(angleMargin);

        float radius1_1 = (float) (Math.abs(Math.tan(alpha + margin_rad) * (deltaY + heightMargin)));
        float radius1_2 = (float) (Math.abs(Math.tan(alpha + margin_rad) * (deltaY - heightMargin)));

        float radius2 = (float) (Math.abs(Math.tan(alpha) * deltaY));

        float radius3_1 = (float) (Math.abs(Math.tan(alpha - margin_rad) * (deltaY + heightMargin)));
        float radius3_2 = (float) (Math.abs(Math.tan(alpha - margin_rad) * (deltaY - heightMargin)));

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
