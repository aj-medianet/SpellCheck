/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellcheck;

import edu.princeton.cs.algs4.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.*;

/**
 *
 * @author andrewjoseph
 */
public class SpellCheck {
    //SET<String> dictionary = new SET<>();
    private final HashSet<String> dictionary;
    Scanner s = new Scanner(System.in);
    
    public SpellCheck(String words) {
        dictionary = new HashSet<>();
        
        In dict = new In(words);
        while (!dict.isEmpty()) {
            String word = dict.readString();
            dictionary.add(word);
        }    
    }
        
    
    public String checkWord(String myWord) {
        String unpunctuated;
        String word = myWord.toLowerCase();
        
        //remove punctuation
        unpunctuated = depunctify(word); 
        
        //if unpunctuated word is spelled correctly
        if(dictionary.contains(unpunctuated)) {
            return word;
        }
        
        //word is misspelled, get suggestions & change word  
        StdOut.println(unpunctuated + ": did you mean?");
        
        String changedWord = edits(unpunctuated);
        changedWord = word.replace(unpunctuated, changedWord);
        return changedWord; 
    }
    
    public String depunctify(String word) {
        String unpunctuated = word;
        int length = word.length();
        
        //Checking for quotes
        if (word.startsWith("\"")) {
            unpunctuated = word.replace("\"", "");
        }
        if (word.endsWith("\"")) {
            unpunctuated = word.replace("\"", "");
        }
        
        //Checking for "." or ",",etc.. at the end 
        if (word.substring(length - 1).equals(".")  || word.substring(length - 1).equals(",") ||  word.substring(length - 1).equals("!") ||  word.substring(length - 1).equals(";") || word.substring(length - 1).equals(":")) {
            unpunctuated = word.substring(0, length - 1);
        }
        
        //checking for ending punctuation
        if (length > 2 && word.substring(length - 2).equals(",\"")  || word.substring(length - 2).equals(".\"") || word.substring(length - 2).equals("?\"") || word.substring(length - 2).equals("!\"")) {
            unpunctuated = word.substring(0, length - 2);
        }
        
        return unpunctuated;
    }
    
    public int suggest(String word1, String word2, String word3) {
        StdOut.println("1. " + word1);
        StdOut.println("2. " + word2);
        StdOut.println("3. " + word3);
        StdOut.println("0. something else");
            
        int i = s.nextInt();
            
        return i;       
    }
    
    private String edits(String word) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> suggestion = new ArrayList<>();
    
        //deletes of a single letter
        for(int i=0; i < word.length(); ++i) {
            String substring = word.substring(0, i) + word.substring(i+1);
            if(!result.contains(substring))
                result.add(substring);
        }
    
        //swaps of adjacent letters
        for(int i=0; i < word.length()-1; ++i) {
            String substring = word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2);
            if(!result.contains(substring))
                result.add(substring);
        }
    
        //replacements of a letter
        for(int i=0; i < word.length(); ++i) {
            for(char c='a'; c <= 'z'; ++c) {
                String substring = word.substring(0, i) + String.valueOf(c) + word.substring(i+1);
                if(!result.contains(substring))
                    result.add(substring);
            }
        }
    
        //insertions of a letter
        for(int i=0; i <= word.length(); ++i) {
            for(char c='a'; c <= 'z'; ++c) {
                String substring = word.substring(0, i) + String.valueOf(c) + word.substring(i);
                if(!result.contains(substring))
                    result.add(substring);
            }
        }
        
        int counter = 0;
        while (counter <= 2) {
            for (int i = 0; i < result.size(); i++)  
                //if(dictionary.containsKey(result.get(i))) {
                    suggestion.add(result.get(i));
                    counter++;
                //}
            
        }
        
        int sg = suggest(suggestion.get(0), suggestion.get(1), suggestion.get(2));
        
        if (sg == 0) {
            StdOut.print("type the word: ");
            return s.next();
        } else {
            return result.get(sg-1);
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        SpellCheck sc = new SpellCheck(args[0]);
        String checkedDoc = "";
        
        // read in mydoc.txt of words
        In mydoc = new In(args[1]);
        while (!mydoc.isEmpty()) {
            String myWord = mydoc.readString();
            checkedDoc += sc.checkWord(myWord) + " ";   
        }
        
        try (PrintWriter out = new PrintWriter("checkedDoc.txt")){
            out.println(checkedDoc);
    
        }        
    }
}
