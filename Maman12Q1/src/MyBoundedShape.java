import java.awt.Color;
import java.awt.Point;

public abstract class MyBoundedShape extends MyShape
{
    private boolean m_bIsFilledShape = false;	// indication if the shape is filled with paint 

    // Class constructor
    protected MyBoundedShape(Point p1 , Point p2 , Color color , boolean bIsFilledShape)
    {
	setP1(p1);
	setP2(p2);
	setMyColor(color);
	setFilledShape(bIsFilledShape);
    }
    
    // m_bIsFilledShape getter
    public boolean isFilledShape()
    {
	return m_bIsFilledShape;
    }

    // m_bIsFilledShape setter    
    public void setFilledShape(boolean bIsFilledShape)
    {
	this.m_bIsFilledShape = bIsFilledShape;
    }

    // returns the width of the rectangle defined by P1 and P2 
    public int getWidth()
    {
	return getP2().x -getP1().x; 
    }

    // returns the height of the rectangle defined by P1 and P2    
    public int getHeight()
    {
	return getP2().y -getP1().y; 
    }
    
    // implementation of the equals function. a BoundedShape is defined equal to another if both BoundedShapes width and height are equals    
    @Override
    public boolean equals(Object otherObject)
    {
	if(otherObject == null || !(otherObject instanceof MyBoundedShape) )
		return false;
	MyBoundedShape otherBoundedShape = (MyBoundedShape)otherObject;
	
	if( getWidth() == otherBoundedShape.getWidth() && getHeight() == otherBoundedShape.getHeight())
	    return true;
	else
	    return false;		
    }
   
}
