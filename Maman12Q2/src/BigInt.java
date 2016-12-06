import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;
import javax.xml.ws.FaultAction;

public class BigInt implements Comparable<BigInt>
{
    private ArrayList<Byte> m_byteArrayList = new ArrayList<Byte>();	// an array of digits that forms the number (most significant digit is at 0 position) 
    private boolean m_bIsNegative = false;	// an indication if the number is negative or not
    private boolean m_bIsZero = false;		// an indication if the number is 0 
    
    // class constructor that forms a number from a string sent  
    BigInt(String strNumber) throws IllegalArgumentException
    {
	strNumber.trim();
	if(strNumber.isEmpty())
	    throw new IllegalArgumentException("strNumber may not be empty");
	if(strNumber.charAt(0) == '+' )
	{
	    setIsNegative( false);
	    strNumber = strNumber.substring(1);
	}
	else if(strNumber.charAt(0) == '-' )
	{
	    setIsNegative(true);
	    strNumber = strNumber.substring(1);
	}
	
	boolean bIsNonZero = false;
	for(int i = 0 ; i < strNumber.length() ; i++)
	{
	    Byte bTemp = new Byte((byte) (strNumber.charAt(i)-'0'));
	    if(bTemp < 0 || bTemp > 9)
		throw new IllegalArgumentException("strNumber may not be empty");
	    
	    if(bTemp != 0)
		bIsNonZero = true;
	    
	    getByteArrayList().add(bTemp);
	}
	
	if(!bIsNonZero)
	{	// special treatment for 0 number
	    getByteArrayList().clear();
	    getByteArrayList().add((byte) 0);
	    setZeroNum(true);
	    setIsNegative(false);
	}
	else
	{	// special treatment if the number starts with 0. (000321)
	    Byte bTemp = getByteArrayList().get(0);
	    while(bTemp == 0)
	    {
		getByteArrayList().remove(0);
		bTemp = getByteArrayList().get(0);
	    }
	}
    }
    
    // copy constructor
    BigInt( BigInt otherNumber)
    {
	this(otherNumber.toString());
    }
    
    // m_byteArrayList getter for inner class usage
    private ArrayList<Byte> getByteArrayList()
    {
	return m_byteArrayList;		
    }

    // m_byteArrayList setter for inner class usage
    private void setByteArrayList(ArrayList<Byte> newByteArrayList)
    {
	m_byteArrayList = newByteArrayList;
    }
    
    // m_bIsNegative setter for inner class usage
    private void setIsNegative(boolean bIsNegative)
    {
	m_bIsNegative = bIsNegative;	
    }

    // m_bIsZero setter for inner class usage
    private void setZeroNum(boolean bIsZero)
    {
	m_bIsZero = bIsZero;
    }
    
    // m_bIsNegative getter
    public boolean isNegativeNumber()
    {
	return m_bIsNegative;
    }
    
    // m_bIsZero getter
    public boolean isZeroNumber()
    {
	return m_bIsZero;
    }
    
    // performs a plus operation (summation) of the current number with the sent otherNumber and returns a new BigNumer instance
    public BigInt plus(BigInt otherNumber)
    {
	BigInt resultInt;
	
	if(isZeroNumber())
	    return new BigInt(otherNumber);
	
	if(otherNumber.isZeroNumber())
	    return new BigInt(this);
	
	resultInt = new BigInt(this);
	
	// both numbers are positive
	if(!otherNumber.isNegativeNumber() && !isNegativeNumber())
	    resultInt.setByteArrayList(sumDigits(this.getByteArrayList(), otherNumber.getByteArrayList()));
	else if(otherNumber.isNegativeNumber() && isNegativeNumber())
	{	// both numbers are negative
	    resultInt.setByteArrayList(sumDigits(this.m_byteArrayList, otherNumber.m_byteArrayList));
	    resultInt.setIsNegative(true);
	}
	else
	{	// one number is positive and the other is negative
	    boolean bThisOrgNegative = isNegativeNumber();
	    boolean bOtherOrgNegative = otherNumber.isNegativeNumber();
	    
	    setIsNegative(false);
	    otherNumber.setIsNegative(false);
	    int nCompareRes = compareTo(otherNumber);
	    
	    switch (nCompareRes) 
	    {
	    case -1:
		resultInt.setByteArrayList(subtructDigits(otherNumber.getByteArrayList(), getByteArrayList()));
		break;
	    case 0:
		resultInt = new BigInt("0");
		break;
	    case 1:
		resultInt.setByteArrayList(subtructDigits(getByteArrayList() , otherNumber.getByteArrayList()));
		break;
	    default:
		resultInt = new BigInt("0");
		break;
	    }
	    
	    setIsNegative(bThisOrgNegative);
	    otherNumber.setIsNegative(bOtherOrgNegative);
	    
	    if(checkIsZeroNumber(resultInt.getByteArrayList()))
	    {
		ArrayList<Byte> tempByteArray = new ArrayList<Byte>();
		tempByteArray.add((byte)0);
		resultInt.setByteArrayList(tempByteArray);
		resultInt.setIsNegative(false);
		resultInt.setZeroNum(true);		
	    }
	    else
	    {
		assert bThisOrgNegative == bOtherOrgNegative : "plus: both numbers may not be of the same sign here";
		assert nCompareRes!=0 : "plus: nCompareRes == 0";
		
		if(bThisOrgNegative)
		{
		    if(nCompareRes == 1)
			resultInt.setIsNegative(true);
		    else
			resultInt.setIsNegative(false);
		}
		else
		{
		    if(nCompareRes == 1)
			resultInt.setIsNegative(false);
		    else
			resultInt.setIsNegative(true);
		}
	    }
	}
	return resultInt;    
    }
    
    //performs a minus (subtraction) operation of the sent otherNumber from the current number and returns a new BigNumer instance
    public BigInt minus(BigInt otherNumber)
    {
	BigInt resultInt;
	
	if(isZeroNumber())
	{	// 0 - X = -X
	    resultInt = new BigInt(otherNumber);
	    resultInt.setIsNegative(true);
	    return resultInt;
	}
	
	// X - 0 = X 
	if(otherNumber.isZeroNumber())
	    return new BigInt(this);
	
	resultInt = new BigInt(this);
	
	// both numbers are with the same sign
	if( (!otherNumber.isNegativeNumber() && !isNegativeNumber()) || (otherNumber.isNegativeNumber() && isNegativeNumber()) )
	{
	    boolean bThisOrgNegative = isNegativeNumber();
	    boolean bOtherOrgNegative = otherNumber.isNegativeNumber();
	    
	    setIsNegative(false);
	    otherNumber.setIsNegative(false);
	    int nCompareRes = compareTo(otherNumber);
	    
	    switch (nCompareRes) 
	    {
	    case -1:
		resultInt.setByteArrayList(subtructDigits(otherNumber.getByteArrayList(), getByteArrayList()));
		break;
	    case 0:
		resultInt = new BigInt("0");
		break;
	    case 1:
		resultInt.setByteArrayList(subtructDigits(getByteArrayList() , otherNumber.getByteArrayList()));
		break;
	    default:
		resultInt = new BigInt("0");
		break;
	    }
	    
	    setIsNegative(bThisOrgNegative);
	    otherNumber.setIsNegative(bOtherOrgNegative);
	    
	    if(checkIsZeroNumber(resultInt.getByteArrayList()))
	    {
		ArrayList<Byte> tempByteArray = new ArrayList<Byte>();
		tempByteArray.add((byte)0);
		resultInt.setByteArrayList(tempByteArray);
		resultInt.setIsNegative(false);
		resultInt.setZeroNum(true);		
	    }
	    else
	    {
		assert nCompareRes!=0 : "minus: nCompareRes == 0";
		
		if(bThisOrgNegative)
		{
		    if(nCompareRes == 1)
			resultInt.setIsNegative(true);
		    else
			resultInt.setIsNegative(false);
		}
		else
		{
		    if(nCompareRes == 1)
			resultInt.setIsNegative(false);
		    else
			resultInt.setIsNegative(true);
		}
	    }
	}
	else if(otherNumber.isNegativeNumber() )	
	{	// this number is positive and the otherNumber is negative
	    resultInt.setByteArrayList(sumDigits(this.m_byteArrayList, otherNumber.m_byteArrayList));
	}
	else	// this number is negative and the otherNumber is positive
	{	
	    resultInt.setByteArrayList(sumDigits(this.m_byteArrayList, otherNumber.m_byteArrayList));
	    resultInt.setIsNegative(true);
	}
	return resultInt;   
    }
    
    // multiply the current BigInt with the sent otherNumber and returns a new BigNumer instance
    public BigInt multiply(BigInt otherNumber)
    {
	BigInt resultInt;
	
	if(isZeroNumber() || otherNumber.isZeroNumber())
	{	// 0 * X = 0 || X * 0 = 0 
	    return new BigInt("0");
	}
	
	resultInt = new BigInt("1");
	resultInt.setByteArrayList(multiplyDigits(getByteArrayList(), otherNumber.getByteArrayList()));
	if(isNegativeNumber() != otherNumber.isNegativeNumber())
	    resultInt.setIsNegative(true);
	
	return resultInt;
    }
    
    // divide the current BigInt with the sent otherNumber and returns a new BigNumer instance
    public BigInt divide(BigInt otherNumber)
    {
	BigInt resultInt;
	
	if(isZeroNumber() )
	{	 
	    return new BigInt("0");
	}
	
	if(otherNumber.isZeroNumber())
	    throw new ArithmeticException("Division by 0 is not allowed");
	
	resultInt = new BigInt("1");
	return resultInt;
    }
    
    
    //sums two ArrayList<Byte> and return an ArrayList<Byte> with the sum of the digits
    private ArrayList<Byte> sumDigits(ArrayList<Byte> firstDigitArray , ArrayList<Byte> secondDigitArray )
    {
	ArrayList<Byte> resultArray = new ArrayList<Byte>();
	int nFirstArrayIndex = firstDigitArray.size()-1;
	int nSecondArrayIndex = secondDigitArray.size()-1;
	Byte bCurrFirstDigit = new Byte((byte)0);
	Byte bCurrSecondDigit = new Byte((byte)0);
	Byte bCurrSum = new Byte((byte)0);
	boolean bCarry = false;
	
	while(nFirstArrayIndex >= 0 && nSecondArrayIndex >= 0)
	{
	    bCurrFirstDigit = firstDigitArray.get(nFirstArrayIndex);
	    bCurrSecondDigit = secondDigitArray.get(nSecondArrayIndex);
	    bCurrSum = (byte)(bCurrFirstDigit + bCurrSecondDigit);
	    
	    if(bCarry)
		bCurrSum++;
	    bCarry = false;
		    
	    if(bCurrSum >= 10)
	    {
		bCarry = true;
		bCurrSum = (byte)(bCurrSum -10);
	    }
	    resultArray.add(0 , bCurrSum);
	    nFirstArrayIndex--;
	    nSecondArrayIndex--;
	}
	
	while(nFirstArrayIndex >= 0)
	{
	    bCurrFirstDigit = firstDigitArray.get(nFirstArrayIndex);
	    if(bCarry)
	    {
		bCurrFirstDigit++;
		bCarry = false;
	    }
	    
	    if(bCurrFirstDigit >= 10)
	    {
		bCarry = true;
		bCurrFirstDigit = (byte)(bCurrFirstDigit -10);
	    }
	    
	    resultArray.add(0,bCurrFirstDigit);
	    nFirstArrayIndex--;
	}
	
	while(nSecondArrayIndex >= 0)
	{
	    bCurrSecondDigit = secondDigitArray.get(nSecondArrayIndex);
	    if(bCarry)
	    {
		bCurrSecondDigit++;
		bCarry = false;
	    }
	    
	    if(bCurrSecondDigit >= 10)
	    {
		bCarry = true;
		bCurrSecondDigit = (byte)(bCurrSecondDigit -10);
	    }
	    
	    resultArray.add(0,bCurrSecondDigit);
	    nSecondArrayIndex--;
	}
	
	if(bCarry)
	{
	    resultArray.add(0,(byte)1);
	}
	
	return resultArray;
    }
    
    
    //subtracts the scondDigitArray from the firstDigitArray and return an ArrayList<Byte> with the results
    // Assumes the firstDigitArray is holding a number that is bigger then the secondDigitArray
    private ArrayList<Byte> subtructDigits(ArrayList<Byte> firstDigitArray , ArrayList<Byte> secondDigitArray )
    {
	ArrayList<Byte> resultArray = new ArrayList<Byte>();
	
	if(secondDigitArray.size() > firstDigitArray.size())
	{
	    assert false : "subtructDigits: secondDigitArray.size() > firstDigitArray.size()";
	    return resultArray;
	}
	
	int nFirstArrayIndex = firstDigitArray.size()-1;
	int nSecondArrayIndex = secondDigitArray.size()-1;
	Byte bCurrFirstDigit = new Byte((byte)0);
	Byte bCurrSecondDigit = new Byte((byte)0);
	Byte bCurrSubtraction = new Byte((byte)0);
	boolean bCarry = false;
	
	while(nFirstArrayIndex >= 0 && nSecondArrayIndex >= 0)
	{
	    bCurrFirstDigit = firstDigitArray.get(nFirstArrayIndex);
	    bCurrSecondDigit = secondDigitArray.get(nSecondArrayIndex);
	    
	    if(bCarry)
		bCurrFirstDigit--;
	    
	    bCurrSubtraction = (byte)(bCurrFirstDigit - bCurrSecondDigit);
	    bCarry = false;
	    if(bCurrSubtraction < 0 )
	    {
		assert nFirstArrayIndex > 0 : "subtructDigits nFirstArrayIndex <= 0 && bCurrSubtraction < 0 nFirstArrayIndex = " + nFirstArrayIndex+" bCurrSubtraction = " +bCurrSubtraction;
		bCarry = true;
		bCurrSubtraction = (byte)(10 +bCurrFirstDigit -bCurrSecondDigit);
	    }
	    resultArray.add(0 , bCurrSubtraction);
	    nFirstArrayIndex--;
	    nSecondArrayIndex--;
	}
	
	while(nFirstArrayIndex >= 0)
	{
	    bCurrFirstDigit = firstDigitArray.get(nFirstArrayIndex);
	    if(bCarry)
	    {
		bCurrFirstDigit--;
		bCarry = false;
	    }
	    
	    if(bCurrFirstDigit < 0)
	    {
		bCarry = true;
		bCurrFirstDigit = (byte)(bCurrFirstDigit +10);
	    }
	    
	    resultArray.add(0,bCurrFirstDigit);
	    nFirstArrayIndex--;
	}
	
	assert !bCarry : "subtructDigits finished subtruction with a carry";
	
	while(resultArray.size() > 0)
	{
	    Byte bTemp = resultArray.get(0);
	    if(bTemp != 0)
		break;
	    resultArray.remove(0);
	}
	
	return resultArray;
    }
    
    // multiply the firstDigitArray with the secondDigitArray. both arrays sizes should not be 0 
    private ArrayList<Byte> multiplyDigits(ArrayList<Byte> firstDigitArray , ArrayList<Byte> secondDigitArray )
    {
	ArrayList<Byte> resultArray = new ArrayList<Byte>();
	ArrayList<Byte> tempMultiArray = new ArrayList<Byte>();
	ArrayList<Byte> smallArray = firstDigitArray;
	ArrayList<Byte> largeArray = secondDigitArray;
	
	assert firstDigitArray.size() > 0 : "multiplyDigits: firstDigitArray.size() > 0";
	assert secondDigitArray.size() > 0 : "multiplyDigits: secondDigitArray.size() > 0";
	
	if(secondDigitArray.size() < firstDigitArray.size())
	{
	    smallArray = secondDigitArray;
	    largeArray = firstDigitArray;
	}
	
	for(int i = smallArray.size()-1 ; i >= 0 ;i-- )
	{
	    tempMultiArray.clear();
	    for(int j = 0 ; j < smallArray.size()-1 -i ; j++)
	    {
		tempMultiArray.add(0,(byte)0 );
	    }
	    multiplyDigitByArray(smallArray.get(i) , largeArray , tempMultiArray);
	    
	    resultArray = sumDigits(resultArray, tempMultiArray);	    
	}
	return resultArray;
    }
    
    // multiply a single digit by the sent arrayToMultiply and add the result to the sent resultArray
    private void multiplyDigitByArray(Byte bDigit ,ArrayList<Byte> arrayToMultiply , ArrayList<Byte> resultArray)
    {
	assert arrayToMultiply != null : "multiplyDigitByArray: arrayToMultiply != null";
	assert resultArray != null : "multiplyDigitByArray: resultArray != null";
	assert arrayToMultiply.size() > 0 : "multiplyDigitByArray: arrayToMultiply.size() > 0";
	
	byte bTempMultiRes = 0;
	byte bTempCarry = 0;
	
	for(int i = arrayToMultiply.size()-1 ; i >= 0 ; i--)
	{
	    Byte bCurrArrayDigit = arrayToMultiply.get(i);
	    bTempMultiRes = (byte)(bDigit*bCurrArrayDigit + bTempCarry);
	    bTempCarry = 0;
	    if(bTempMultiRes >= 10)
	    {
		bTempCarry = (byte)(bTempMultiRes/10);
		bTempMultiRes = (byte)(bTempMultiRes%10);
	    }
	    resultArray.add(0,bTempMultiRes);
	}
	
	if(bTempCarry > 0)
	    resultArray.add(0,bTempCarry);	
    }
    
    // check if the sent digit array represent a zero number
    private boolean checkIsZeroNumber(ArrayList<Byte> digitArray)
    {
	boolean bRes = true;
	
	for(Byte bTemp : digitArray)
	{
	    if(bTemp != 0)
		return false;
	}
	return bRes;
    }
    
    
    // equals method implementation
    @Override
    public boolean equals(Object otherObject)
    {
	if(otherObject == null || !(otherObject instanceof BigInt) )
		return false;
	
	if(otherObject == this)
	    return true;
	
        BigInt otherBigInt =  (BigInt)otherObject;
        
        // both are zero
        if(otherBigInt.isZeroNumber() && isZeroNumber())
            return true;
        
        // one of the numbers is zero
        if( otherBigInt.isZeroNumber() != isZeroNumber())
            return false;
        
        // sign mismatch
        if(otherBigInt.isNegativeNumber() != isNegativeNumber())
            return false;
            
        // number of digits is different 
        if(otherBigInt.getByteArrayList().size() != getByteArrayList().size())
            return false;
        
        for(int i = 0 ; i < getByteArrayList().size() ; i++)
        {
            Byte bTemp = getByteArrayList().get(i);
            Byte bOtherTemp = otherBigInt.getByteArrayList().get(i);
            
            if(!bTemp.equals(bOtherTemp))
        	return false;
        }
        
        return true;
    }
    
    @Override
    public String toString()
    {
	StringBuilder tempStrBuilder = new StringBuilder();
	if(isZeroNumber())
	    return "0";
	
	if(isNegativeNumber())
	    tempStrBuilder.append('-');
	
	for(Byte bTemp : getByteArrayList())
	{
	    tempStrBuilder.append(bTemp);
	}
	return tempStrBuilder.toString();
    }

    // compareTo implementation
    @Override
    public int compareTo(BigInt otherBigInt)
    {
	// same number
	if(equals(otherBigInt))
	    return 0;
	// different signs
	if( isNegativeNumber() != otherBigInt.isNegativeNumber())
	{
	    if(isNegativeNumber())
		return -1;
	    else
		return 1;
	}
	// different number of digits
	if(getByteArrayList().size() != otherBigInt.getByteArrayList().size())
	{
	    if(getByteArrayList().size() < otherBigInt.getByteArrayList().size())
	    {
		if(isNegativeNumber())
		    return 1;	// -55 > -5555
		else
		    return -1;	// 55 < 5555
	    }
	    else
	    {
		if(isNegativeNumber())
		    return -1;	// -5555 < -55
		else
		    return 1;	// 5555 > 55
	    }
	}
	
	// both numbers are with the same sign and same number of digits.
	for(int i = 0 ; i < getByteArrayList().size() ; i++)
	{
	    Byte bTemp = getByteArrayList().get(i);
	    Byte bOtherTemp = otherBigInt.getByteArrayList().get(i);
	    
	    if(bTemp < bOtherTemp)
	    {
		if(isNegativeNumber())
		    return 1;	
		else
		    return -1;	
	    }
	    else if(bTemp > bOtherTemp)
	    {
		if(isNegativeNumber())
		    return -1;	
		else
		    return 1;	
	    }
	}
	
	
	// we should not get to this line of code because we checked for equals before
	return 0;	
    }

}
