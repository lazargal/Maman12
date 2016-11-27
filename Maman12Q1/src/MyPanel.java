import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class MyPanel extends JPanel
{
    private final int NUM_OF_SHAPES = 2;	// number of shapes for each sub class of MyShape 
    private ArrayList<MyShape> m_orgShapeArray = new ArrayList<MyShape>();	// the original array of the MyShape sub class instances.
    private ArrayList<MyShape> m_dupShapeArray = new ArrayList<MyShape>();	// the duplicated array of the MyShape sub class instances.
   
    
    // fill the original shapes array
    public void fillOrgShapeArray()
    {
	Random myRand = new Random();
	int nMaxRand = 200;
	Point tempP1 = new Point(0, 0);
	Point tempP2 = new Point(0, 0);
	Color tempColor = new Color(0, 0, 0);
	
	m_orgShapeArray.clear();
	
	// add MyLine objects 
	for(int i = 0 ; i < NUM_OF_SHAPES ; i++)
	{
	    tempP1.x = myRand.nextInt(nMaxRand);
	    tempP1.y = myRand.nextInt(nMaxRand);
	    
	    tempP2.x = myRand.nextInt(nMaxRand);
	    tempP2.y = myRand.nextInt(nMaxRand);
	    
	    tempColor = new Color(myRand.nextInt(256) , myRand.nextInt(256) ,myRand.nextInt(256));
	    	    
	    m_orgShapeArray.add(new MyLine(tempP1 , tempP2 , tempColor));
	
	}
	
	// add MyOval objects
	for(int i = 0 ; i < NUM_OF_SHAPES ; i++)
	{
	    tempP1.x = myRand.nextInt(nMaxRand);
	    tempP1.y = myRand.nextInt(nMaxRand);
	    
	    tempP2.x = myRand.nextInt(nMaxRand);
	    tempP2.y = myRand.nextInt(nMaxRand);
	    
	    tempColor = new Color(myRand.nextInt(256) , myRand.nextInt(256) ,myRand.nextInt(256));
	    
	    try
	    {
		m_orgShapeArray.add(new MyOval(tempP1 , tempP2 , tempColor , (tempP1.x%2 == 0) ) );
	    } 
	    catch (IllegalArgumentException e)
	    {
		i--; 
	    }	    
	}
	
	// add MyRectangle objects
	for(int i = 0 ; i < NUM_OF_SHAPES ; i++)
	{
	    tempP1.x = myRand.nextInt(nMaxRand);
	    tempP1.y = myRand.nextInt(nMaxRand);
	    
	    tempP2.x = myRand.nextInt(nMaxRand);
	    tempP2.y = myRand.nextInt(nMaxRand);
	    
	    tempColor = new Color(myRand.nextInt(256) , myRand.nextInt(256) ,myRand.nextInt(256));
	    
	    try
	    {
		m_orgShapeArray.add(new MyRectangle(tempP1 , tempP2 , tempColor , (tempP1.x%2 == 0) ) );
	    } 
	    catch (IllegalArgumentException e)
	    {
		i--; 
	    }	    
	}
	
    }
    
    // duplicates and offset the shapes from the original shape array. MyBoundedShapes sub classes color is changed to gray
    public void duplicateAndOffsetShapeArray()
    {
	for(MyShape tempShape : m_orgShapeArray)
	{
	    try
	    {
		m_dupShapeArray.add(tempShape.clone());
		tempShape.getP1().x += 10;
		tempShape.getP1().y += 10;
		tempShape.getP2().x += 10;
		tempShape.getP2().y += 10;
		
		if(tempShape instanceof MyBoundedShape)
		{
		    tempShape.setMyColor(Color.GRAY);
		}
	    }
	    catch (CloneNotSupportedException e)
	    {
		e.printStackTrace();
	    }
	}
	
	
    }
    
    
    // paints the shapes array lists
    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	for(MyShape tempShape : m_orgShapeArray)
	{
	    tempShape.draw(g);
	}
	
	for(MyShape tempShape : m_dupShapeArray)
	{
	    tempShape.draw(g);
	}
    }

}
