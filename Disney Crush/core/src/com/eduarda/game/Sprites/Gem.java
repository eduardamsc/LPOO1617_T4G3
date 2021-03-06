package com.eduarda.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

/**
 * Created by eduardacunha on 01/06/2017.
 */

/**
 * The Class Gem.
 */
public class Gem {
    private static final float GRAVITY = -((((float) 3/4) * Gdx.graphics.getHeight())/14);
    private Vector3 position;
    private Vector3 velocity;
    private int symbol;

    public void setL(String l) {
        this.l = l;
    }

    public String l;

    private Rectangle bounds;

    public float WIDTH = (float) ((((float) 8/10) * Gdx.graphics.getWidth())/8);

    private Texture gem;

    java.lang.String[] textures = {"p_ariel.png", "p_belle.png", "p_cinderella.png", "p_snowWhite.png", "p_rapunzel.png"};

    /**
     * Constructor for a gem.
     *
     * @param x Collumn.
     * @param y Line.
     */
    public Gem(float x, float y) {
        this.position = new Vector3(x, y, 0);
        this.velocity = new Vector3(0, 0, 0);
        this.symbol = generateSymbol();
        this.gem = new Texture(chooseTexture());

        this.bounds = new Rectangle(position.x, position.y, WIDTH, WIDTH);
    }

    /**
     * Updates the velocity with which the gems fall into place.
     *
     * @param dt Time.
     */
    public void update(float dt) {
        this.velocity.add(0, GRAVITY, 0);
        this.velocity.scl(dt);
        this.position.add(0, this.velocity.y, 0);
        if (this.position.y < 3*WIDTH) this.position.y += 3*WIDTH;

        velocity.scl(1/dt);

        bounds.setPosition(position.x, position.y);
    }

    /**
     * Gets the position for a gem.
     *
     * @return Position of type Vector3.
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Gets the texture for a gem.
     *
     * @return Texture.
     */
    public Texture getTexture() {
        return gem;
    }

    public Rectangle getBounds() { return bounds; }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Sets the texture for a gem.
     *
     * @param gem Texture of a disney character.
     */
    public void setGem(Texture gem) {
        this.gem = gem;
    }

    /**
     * Generates a symbol from 0 to 4 randomly.
     *
     * @return Symbol of type Int.
     */
    public int generateSymbol() {
        Random r = new Random();
        int symbol = r.nextInt(5) + 0;
        int[] symbols = {0, 1, 2, 3, 4};

        return symbols[symbol];
    }

    /**
     * Chooses a texture according to the symbol.
     *
     * @return String which represents a texture.
     */
    public java.lang.String chooseTexture() {
        l = textures[this.symbol];
        return textures[this.symbol];
    }

    /**
     * Checks if gems are colliding/overlaping.
     * 
     * @param player Rectangle that defines the bounds of a gem.
     *
     * @return True if gems overlap.
     */
    public boolean collides(Rectangle player) {
        return player.overlaps(bounds);
    }
}
