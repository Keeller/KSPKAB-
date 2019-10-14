package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sequence implements Result {

    private ArrayList<Double> data=new ArrayList<Double>();

    public Sequence(String s,String delimiter) {
        String[] rowData;
        rowData=s.split(delimiter);
        for (String s1:rowData) {
            try {

                data.add(Double.parseDouble(s1));
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.out.println("Cant parse number "+s1);
            }
        }
    }

    public Sequence(Double[] d){
            data= (ArrayList<Double>) Arrays.asList(d);
    }

    public Sequence(List<Double> l){
            data=(ArrayList<Double>) l;
    }

    private boolean isIncreasing(){

        for(int i=0;i<data.size()-1;i++){

            if(data.get(i)>=data.get(i+1)){
                return false;
            }
        }
        return true;
    }

    private boolean isDuplicated(){
        for(Double d:data){
            if(Collections.frequency(data,d)>1)
                return true;
        }
        return false;
    }
    private boolean isImage(){

        boolean prev=false;
        boolean next=false;

        for (int i=0;i<data.size()-1;i++){

            if(data.get(i)<0)
                prev=false;
            else
                prev=true;

            if(data.get(i+1)<0)
                next=false;
            else
                next=true;

            if(!prev^next)
                return false;

        }
        return true;
    }
    @Override
    public String getResult() {
        String result="Введенная последовательность является: ";
        if(isIncreasing())
            result+="возрастающей ";
        if(isDuplicated())
            result+="содержащей повторяющиеся элементы ";
        if(isImage())
            result+="знакочередующийся";
        return result;
    }
}
