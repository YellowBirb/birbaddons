package yellowbirb.birbaddons.render;

import yellowbirb.birbaddons.render.shapes.CircleXZ;
import yellowbirb.birbaddons.render.shapes.CompoundShape;
import yellowbirb.birbaddons.render.shapes.InterCircleStrip;

import static java.lang.Math.*;

public class RenderUtils {

    public static CompoundShape createBoxyRing(String featureID, String ID,
                                               float playerX, float playerY, float playerZ,
                                               double alpha, double alpha1, double alpha2,
                                               float deltaY, float deltaY1, float deltaY2,
                                               int boxyRingInnerColor, boolean boxyRingInnerVisibleThroughWalls,
                                               int boxyRingOuterCornerColor, boolean boxyRingOuterCornerVisibleThroughWalls,
                                               int boxyRingOuterPlaneColor, boolean boxyRingOuterPlaneVisibleThroughWalls) {

        float radius1_1 = (float) (Math.min(Math.abs(Math.tan(alpha1) * deltaY1), 2e3));
        float radius1_2 = (float) (Math.min(Math.abs(Math.tan(alpha1) * deltaY2), 2e3));

        float radius2 = (float) (Math.min(Math.abs(Math.tan(alpha) * deltaY), 2e3));

        float radius3_1 = (float) (Math.abs(Math.tan(alpha2) * deltaY1));
        float radius3_2 = (float) (Math.abs(Math.tan(alpha2) * deltaY2));

        float segmentLength = 0.5f;
        // resampling segmentAmount to draw clean InterCircleStrips
        int segmentAmount = (int) round(clamp((PI * max(radius1_1, radius1_2) * 2) / segmentLength, 8, 128));
        float highY = playerY + deltaY1;
        float lowY = playerY + deltaY2;

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