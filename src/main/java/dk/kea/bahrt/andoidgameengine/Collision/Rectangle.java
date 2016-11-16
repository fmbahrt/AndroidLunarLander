package dk.kea.bahrt.andoidgameengine.Collision;

//Rectangle class, used for rect hitbox boundaries.
public class Rectangle
{
    //Attributes

    public float rotationDegree;

    //Position in grid
    public float upperLeftX;
    public float upperLeftY;

    public float width;
    public float height;

    //Constructors
    public Rectangle(float width, float height)
    {
        this.rotationDegree = 0.0f;
        this.upperLeftX     = 0.0f;
        this.upperLeftY     = 0.0f;
        this.width          = width;
        this.height         = height;
    }

    public Rectangle(float upperLeftX, float upperLeftY, float width, float height)
    {
        this.rotationDegree = 0.0f;
        this.upperLeftX     = upperLeftX;
        this.upperLeftY     = upperLeftY;
        this.width          = width;
        this.height         = height;
    }

    public Rectangle(float upperLeftX, float upperLeftY, float width, float height, float rotationDegree)
    {
        this.rotationDegree = rotationDegree;
        this.upperLeftX     = upperLeftX;
        this.upperLeftY     = upperLeftY;
        this.width          = width;
        this.height         = height;
    }

}
