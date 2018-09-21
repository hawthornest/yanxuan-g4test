package com.lynhaw.g4test.enumeration;


/**
 * @Author yyhu3
 * @Date 2018-09-20 10:57
 */
public class TestEnum {

    public static void main(String[] args)
    {
        String test = "Noneed";
        CommonEnum commonEnum = CommonEnum.valueOf(test.toUpperCase());
        switch(commonEnum)
        {
            case NEED:
                System.out.println("NEED21123132");
                break;
            case NONEED:
                System.out.println("NONEED132123132");
                break;
        }
     }
}
