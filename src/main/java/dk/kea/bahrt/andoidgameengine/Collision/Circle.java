package dk.kea.bahrt.andoidgameengine.Collision;

//Circle class, used for circle hitbox boundaries.
public class Circle
{

    //Attributes

    public float centerX;
    public float centerY;

    public float radius;

    //Constructors
    public Circle(float radius)
    {
        this.centerX = 0.0f;
        this.centerY = 0.0f;
        this.radius  = radius;
    }

    public Circle(float centerX, float centerY, float radius)
    {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius  = radius;
    }

}
