package com.example.user.d1test;

import java.util.*;


public class testing
{
    public static void main(String[] args)
    {

//      String str = "heriot watt uni";
//      System.out.println(str);
//
//      String rev ="";
//
//      for (int i = str.length()-1; i>=0 ; i--){
//        rev = rev + str.charAt(i);
//      }
//      System.out.println(rev);
//System.out.println("");
//          String sr[] = str.split(" ");
//
//          for(int f = sr.length -1 ; f>=0 ;f--  ){
//             System.out.print(sr[f] + " ");
//          }
        HashMap<Integer,Integer> rssarrayNumberAndSqrt = new HashMap<Integer, Integer>();
        HashMap<Integer,Integer> rssarrayNumberAndSize = new HashMap<Integer, Integer>();
        for(int g = 1 ; g<4; g++){

        }
        rssarrayNumberAndSize.put(1,13);
        rssarrayNumberAndSize.put(2,14);
        rssarrayNumberAndSize.put(3,12);
        rssarrayNumberAndSize.put(4,14);
        rssarrayNumberAndSize.put(5,14);
        rssarrayNumberAndSize.put(6,13);

        rssarrayNumberAndSqrt.put(1,26);
        rssarrayNumberAndSqrt.put(2,21);
        rssarrayNumberAndSqrt.put(3,27);
        rssarrayNumberAndSqrt.put(4,29);
        rssarrayNumberAndSqrt.put(5,23);
        rssarrayNumberAndSqrt.put(6,22);



        int minloc=0;
        int maxap = 0;
        int maxSize=0;
        int minSqrt= rssarrayNumberAndSqrt.get(1);

        for ( int key : rssarrayNumberAndSize.keySet() ) {

            if (rssarrayNumberAndSize.get(key) == maxSize ){

                if (rssarrayNumberAndSqrt.get(key) <= minSqrt ){

                    minSqrt = rssarrayNumberAndSqrt.get(key);
                    minloc= key;

                }
                else  if (rssarrayNumberAndSqrt.get(minloc) <= minSqrt ){

                    minSqrt = rssarrayNumberAndSqrt.get(minloc);
                }

            }else if (rssarrayNumberAndSize.get(key) > maxSize ){
                maxSize = rssarrayNumberAndSize.get(key);
                minloc= key;
                minSqrt = rssarrayNumberAndSqrt.get(key);
            }
        }

        System.out.println("minloc = " +minloc );





    }

}

