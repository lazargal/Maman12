import javax.swing.JFrame;

public class MyShapeGui
{

    public static void main(String[] args)
    {
	JFrame myFrame = new JFrame();
	MyPanel myPanel = new MyPanel();
	
	myFrame.add(myPanel);
	myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	myFrame.setSize(400, 400);
	myFrame.setVisible(true);
	
	myPanel.fillOrgShapeArray();
	myPanel.duplicateAndOffsetShapeArray();
	myPanel.repaint();
    }

}
