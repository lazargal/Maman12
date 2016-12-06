import java.util.Scanner;

public class BigIntGui
{

    public static void main(String[] args)
    {
	Scanner myScanner = new Scanner(System.in);
	BigInt bigInt1;
	BigInt bigInt2;
	boolean bExit = false;
		
	while(!bExit)
	{
	    System.out.println("Enter first number");
	    bigInt1 = getBigInt(myScanner);
	    
	    System.out.println("Enter second number");
	    bigInt2 = getBigInt(myScanner);
	    
	    printSum(bigInt1 , bigInt2);
	    printSubtruction(bigInt1 , bigInt2);
	    printMultiplaction(bigInt1 , bigInt2);
	    printDivision(bigInt1 , bigInt2);
	    
	    
	    System.out.println("would you like to enter new numbers? (1 = yes)");
	    int nTemp = myScanner.nextInt();
	    if(nTemp != 1)
		bExit = true;
	    myScanner.nextLine();
	    
	}
	
	System.out.println("program terminated");
    }
    
    private static BigInt getBigInt(Scanner myScanner) 
    {
	BigInt resoultInt = new BigInt("0");
	String strTemp = myScanner.nextLine();
	boolean bValidNum = false;
	
	while(!bValidNum)
	{
	    try
	    {
        	resoultInt = new BigInt(strTemp);
        	bValidNum = true;
	    }
	    catch(IllegalArgumentException exption)
	    {
		System.out.println("Invalid number. please eneter a valid number");
		strTemp = myScanner.nextLine();
	    }
    	}
	return resoultInt;
    }
    
    private static void printSum(BigInt bigInt1 , BigInt bigInt2  )
    {
	BigInt tempRes = bigInt1.plus(bigInt2);
	StringBuilder strTemp = new StringBuilder();
	strTemp.append(bigInt1 + " + ");
	if(bigInt2.isNegativeNumber())
	    strTemp.append("( " + bigInt2 + " )");
	else
	    strTemp.append(bigInt2);
	
	strTemp.append(" = " + tempRes);
	System.out.println(strTemp);
    }
    
    private static void printSubtruction(BigInt bigInt1 , BigInt bigInt2  )
    {
	BigInt tempRes = bigInt1.minus(bigInt2);
	StringBuilder strTemp = new StringBuilder();
	strTemp.append(bigInt1 + " - ");
	if(bigInt2.isNegativeNumber())
	    strTemp.append("( " + bigInt2 + " )");
	else
	    strTemp.append(bigInt2);
	
	strTemp.append(" = " + tempRes);
	System.out.println(strTemp);
    }
    
    private static void printMultiplaction(BigInt bigInt1 , BigInt bigInt2  )
    {
	BigInt tempRes = bigInt1.multiply(bigInt2);
	StringBuilder strTemp = new StringBuilder();
	strTemp.append(bigInt1 + " * ");
	if(bigInt2.isNegativeNumber())
	    strTemp.append("( " + bigInt2 + " )");
	else
	    strTemp.append(bigInt2);
	
	strTemp.append(" = " + tempRes);
	System.out.println(strTemp);
    }
    
    private static void printDivision(BigInt bigInt1 , BigInt bigInt2  )
    {
	StringBuilder strTemp = new StringBuilder();
	try
	{
	    BigInt tempRes = bigInt1.divide(bigInt2);
	    strTemp.append(bigInt1 + " * ");
	    if(bigInt2.isNegativeNumber())
		strTemp.append("( " + bigInt2 + " )");
	    else
		strTemp.append(bigInt2);
	    strTemp.append(" = " + tempRes);
	}
	catch(ArithmeticException arithmeticEx)
	{
	    strTemp.append(arithmeticEx.getMessage());
	}
	
	
	System.out.println(strTemp);
    }
}
