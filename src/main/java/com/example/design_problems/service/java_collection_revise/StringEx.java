package com.example.design_problems.service.java_collection_revise;

import java.util.HashSet;
import java.util.Set;

public class StringEx {
}
class Solution {
    public boolean checkIfPangram(String sentence) {
        // Set<Character> alphabets= Stream.of({'a', 'b', 'c', 'e', 'f', 'g', 'h', 'i'})
        int totalChar= 0;
        Set<Character> chars= new HashSet();
        for(int i=0; i< sentence.length() ; i++){
            Character c= sentence.charAt(i);
            if(chars.contains(c)){
                continue;
            }else{
                chars.add(c);
                totalChar++;
            }
        }
        if(totalChar==26){
            return true;
        }else{
            return false;
        }

    }
}
