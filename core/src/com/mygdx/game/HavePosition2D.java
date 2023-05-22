package com.mygdx.game;

import java.awt.*;
import java.awt.geom.Point2D;

public interface HavePosition2D {
    public Point2D.Float get2DPosition();
    public void set2DPosition(Point.Float p);
    public void setX2DPosition(float x);
    public void setY2DPosition(float y);

}
