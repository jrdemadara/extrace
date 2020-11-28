/*
 * Copyright 2020 Johnny Roger.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Classes;

/**
 *
 * @author Johnny Roger
 */
public class NumberFunction {
    
    //This method put comma on numbers equals to or more than 1000 
    //For Example 1000.00 = 1,000.00
    public static String getFormattedNumber(String numberToFormat){
        if(numberToFormat != null){
            String finalNumber = "";
            String[] number = numberToFormat.split("\\.");
            char[] digit = number[0].toCharArray();
            int x = 0;
            int y=3;
            for (int i=digit.length-1; i>=0; i--){
                if (x == y) {
                    finalNumber = digit[i] + "," + finalNumber;
                    y+=3;
                } else {
                    finalNumber = digit[i] + finalNumber;
                }
                x++;
            }
            return finalNumber + "." + number[1];
        }
        
        return "0.00";
    }
    
    //This method strips the "Php" and comma in the string
    //for example Php 1,000.00 = 1000.00
    public static float stripValue(String stringToStrip){
        float strippedValue = 0;
        if(!stringToStrip.trim().equals("")){
            String word = "Php";
            if(stringToStrip.contains(word)){
                stringToStrip = stringToStrip.replace(word, "");
            }
            word = ",";
            if(stringToStrip.contains(word)){
                stringToStrip = stringToStrip.replace(word, "");
            }
            strippedValue = Float.parseFloat(stringToStrip);
        }
        return strippedValue;
    }
    
}
