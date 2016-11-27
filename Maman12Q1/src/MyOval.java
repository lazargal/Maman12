import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyOval extends MyBoundedShape 
{
    // Class constructor
    MyOval( Point p1 , Point p2 , Color color , boolean bIsFilledShape) throws IllegalArgumentException
    {
	super(p1,p2,color , bIsFilledShape);
	if(p1.x >= p2.x || p1.y >= p2.y)
	    throw new IllegalArgumentException("P1 should be the top left corner of the rectangle and P2 should be the lower right corner");
    }
 
    // implementation of the abstract draw function 
    @Override
    public void draw(Graphics g)
    {
	Color tempColor = g.getColor();
	g.setColor(getMyColor());
	if(isFilledShape())
	    g.fillOval(getP1().x, getP1().y, getWidth(), getHeight());
	else
	    g.drawOval(getP1().x, getP1().y, getWidth(), getHeight());    
	    
	g.setColor(tempColor);	
    }
    
    // 	implementation of the clone function
    @Override
    public MyOval clone() throws CloneNotSupportedException
    {
	return new MyOval(getP1() , getP2(), getMyColor() , isFilledShape());
    } 
    

}
