package com.mygdx.game;

public interface GameObject extends HavePosition2D, HaveTag {
    public void collision(Collider2D myCollider, Collider2D otherCollider);
}
