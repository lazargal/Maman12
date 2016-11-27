import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public abstract class MyShape implements Cloneable 
{
    private Point m_p1 = new Point();	// the top left point of the shape to draw
    private Point m_p2 = new Point();	// the bottom right point of the shape to draw
    private Color m_myColor = new Color(0,0,0);	// the color of the shape to draw
    
    
    // an abstract function to draw the sub class shape on to an Graphics object
    public abstract void draw(Graphics g);	

    // an abstract function for all sub classes to implement the clone function 
    @Override
    public abstract MyShape clone() throws CloneNotSupportedException;
    
    // m_p1 getter
    public Point getP1()
    {
	return m_p1;
    }

    // m_p1 setter
    public void setP1(Point p1)
    {
	this.m_p1 = (Point)p1.clone();
    }

    // m_p2 getter
    public Point getP2()
    {
	return m_p2;
    }

    // m_p2 setter
    public void setP2(Point p2)
    {
	this.m_p2 = (Point)p2.clone();
    }

    // m_myColor getter
    public Color getMyColor()
    {
	return m_myColor;
    }

    // m_myColor setter
    public void setMyColor(Color myColor)
    {
	this.m_myColor = new Color(myColor.getRGB());
    }


}
