package yellowbirb.birbaddons.render;

import yellowbirb.birbaddons.render.shapes.CircleXZ;
import yellowbirb.birbaddons.render.shapes.CompoundShape;
import yellowbirb.birbaddons.render.shapes.InterCircleStrip;

import static java.lang.Math.*;

public class RenderUtils {

    public static CompoundShape createBoxyRing(String featureID, String ID,
                                               double alpha, int deltaY, float playerX, float playerY, float playerZ,
                                               float angleMargin, float heightMargin,
                                               int boxyRingInnerColor, boolean boxyRingInnerVisibleThroughWalls,
                                               int boxyRingOuterCornerColor, boolean boxyRingOuterCornerVisibleThroughWalls,
                                               int boxyRingOuterPlaneColor, boolean boxyRingOuterPlaneVisibleThroughWalls) {

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

        return new CompoundShape(featureID, ID,
                new CircleXZ(radius1_1, segmentAmount, playerX, highY, playerZ, boxyRingOuterCornerColor, boxyRingOuterCornerVisibleThroughWalls),
                new CircleXZ(radius1_2, segmentAmount, playerX, lowY , playerZ, boxyRingOuterCornerColor, boxyRingOuterCornerVisibleThroughWalls),

                new CircleXZ(radius2, segmentAmount, playerX, playerY + deltaY, playerZ, boxyRingInnerColor, 2, boxyRingInnerVisibleThroughWalls),

                new CircleXZ(radius3_1, segmentAmount, playerX, highY, playerZ, boxyRingOuterCornerColor, boxyRingOuterCornerVisibleThroughWalls),
                new CircleXZ(radius3_2, segmentAmount, playerX, lowY , playerZ, boxyRingOuterCornerColor, boxyRingOuterCornerVisibleThroughWalls),

                new InterCircleStrip(radius1_1, radius1_2, segmentAmount, playerX, highY, lowY , playerZ, boxyRingOuterPlaneColor, boxyRingOuterPlaneVisibleThroughWalls),
                new InterCircleStrip(radius3_1, radius3_2, segmentAmount, playerX, highY, lowY , playerZ, boxyRingOuterPlaneColor, boxyRingOuterPlaneVisibleThroughWalls),
                new InterCircleStrip(radius1_1, radius3_1, segmentAmount, playerX, highY, highY, playerZ, boxyRingOuterPlaneColor, boxyRingOuterPlaneVisibleThroughWalls),
                new InterCircleStrip(radius1_2, radius3_2, segmentAmount, playerX, lowY , lowY , playerZ, boxyRingOuterPlaneColor, boxyRingOuterPlaneVisibleThroughWalls)
        );
    }
}