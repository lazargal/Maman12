import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyLine extends MyShape 
{
    // class constructor
    MyLine( Point p1 , Point p2 , Color color)
    {
	super();
	setP1(p1);
	setP2(p2);
	setMyColor(color);
    }

    // implementation of the abstract draw function 
    @Override
    public void draw(Graphics g)
    {
	Color tempColor = g.getColor();
	g.setColor(getMyColor());
	g.drawLine(getP1().x , getP1().y , getP2().x , getP2().y);
	g.setColor(tempColor);	
    }
    
    // implementation of the equals function. a Line is defined equal to another if both line sizes are equals 
    @Override
    public boolean equals(Object otherObject)
    {
	if(otherObject == null || !(otherObject instanceof MyLine) )
		return false;
	MyLine otherLine = (MyLine)otherObject;
	double dMySize = Math.sqrt( (getP2().x - getP1().x)*(getP2().x - getP1().x) + (getP2().y - getP1().y)*(getP2().y - getP1().y) );
	double dOtherLineSize = Math.sqrt( (otherLine.getP2().x - otherLine.getP1().x)*(otherLine.getP2().x - otherLine.getP1().x) + 
						(otherLine.getP2().y - otherLine.getP1().y)*(otherLine.getP2().y - otherLine.getP1().y) );
	
	if( Math.abs(dOtherLineSize - dMySize) < 0.0001)
	    return true;
	else
	    return false;		
    }
    
    // implementation of the clone function
    @Override
    public MyLine clone() throws CloneNotSupportedException
    {
	return new MyLine(getP1() , getP2(), getMyColor());
    }
}
