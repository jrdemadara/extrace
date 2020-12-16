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
package lozadagroupcompany;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Johnny Roger
 */
public class DateFunction {
    
    public static String getFormattedDate(){
    SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yy");
    Date date = new Date();
    return sdf1.format(date);
  }
    
    public static String getFormattedYearMonth(){
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
    Date date = new Date();
    return sdf1.format(date);
  }
    
    public static String getFormattedYear(){
    SimpleDateFormat sdf1 = new SimpleDateFormat("yy");
    Date date = new Date();
    return sdf1.format(date);
  }
    
}
