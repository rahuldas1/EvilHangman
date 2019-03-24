//***********************************************
//
// Hangman class
//
// This class represents a Hangman game
// It contains methods to check the user's guess,
// check for repeated guesses, add word patterns,
// etc.
//
//************************************************

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Hangman
{
    // declare instance variables
    private ArrayList<String> remWords = new ArrayList<String>();
    private ArrayList<Character> guesses = new ArrayList<Character>();
    private HashMap<String, ArrayList<String>> wordPats;
    private int len, numGuesses;
    private String output, answer, keyS;
    private boolean correctGuess;
    private char[] keyC;

    // constructor
    public Hangman(int numGuesses, int len, ArrayList<String> dictionary)
    {
        this.len = len;
        this.numGuesses = numGuesses;

        for(String w: dictionary)
        {
            if(w.length() == len)
            {
                remWords.add(w);
            }
        }

        char[] out = new char[len];
        for(int i = 0; i < len; i++)
        {
            out[i] = '_';
        }
        output = new String(out);
    }

    // check user's guess
    public boolean check(char guess)
    {
        String tem = output;
        wordPats = new HashMap<String, ArrayList<String>>();

        if(gameOver())
        {
            return win();
        }

        // make word patterns and add words to hashmap at pattern key
        for(String w : remWords)
        {
            keyC = new char[len];

            for(int i = 0; i < len; i++)
            {
                if(w.charAt(i) == guess)
                {
                    keyC[i] = guess;
                }
                else
                {
                    keyC[i] = output.charAt(i);
                }
            }
            keyS = new String(keyC);

            addPattern(keyS, w, wordPats);
        }

        // last guess situation when remWords is not empty
        if(wordPats.keySet().contains(output) && numGuesses == 1)
        {
            numGuesses--;
            guesses.add(guess);
            remWords = new ArrayList<String>(wordPats.get(output));
            answer = wordPats.get(output).get(0);
            return false;
        }

        // implied else
        // check for word pattern with largest word list
        for(String k : wordPats.keySet())
        {
            if(!(wordPats.keySet().contains(tem)))
            {
                tem = k;
            }

            if(wordPats.get(k).size() > wordPats.get(tem).size())
            {
                tem = k;
            }
        }

        // if dictionary is still populated
        if(wordPats.keySet().contains(tem))
        {
            remWords = new ArrayList<String>(wordPats.get(tem));
            correctGuess = !(output.equals(tem));

            if(!correctGuess)
            {
                numGuesses--;
            }

            output = tem;
            answer = wordPats.get(output).get(0);
            guesses.add(guess);
            return correctGuess;
        }
        else
        {
            remWords = new ArrayList<String>();
            guesses.add(guess);
            return false;
        }
    }

    // check if guess is repeated
    public boolean checkRepeat(char guess)
    {
        for(char g : guesses)
        {
            if(g == guess)
            {
                return false;
            }
        }

        return true;
    }

    // adds words to hashmap at word pattern key
    public void addPattern(String k, String w, HashMap<String, ArrayList<String>> wordPats)
    {
        if(wordPats.get(k) == null)
        {
            wordPats.put(k, new ArrayList<String>());
        }

        wordPats.get(k).add(w);
    }

    // returns whether the game is over or not
    public boolean gameOver()
    {
        return numGuesses == 0 || remWords.isEmpty() || !(output.contains("_"));
    }

    // returns current hangman board (eg: a__a_)
    public String showWord()
    {
        return output;
    }

    // returns number of guesses left
    public int getNumGuesses()
    {
        return numGuesses;
    }

    // returns list of guesses in [_,_,_] format
    public String getGuesses()
    {
        return guesses.toString();
    }

    // returns (evil) answer
    public String getAnswer()
    {
        if(gameOver())
        {
            return answer;
        }
        return null;
    }

    // returns true if player has won
    public boolean win()
    {
        if(gameOver())
        {
            return !(output.contains("_"));
        }
        return false;
    }

    // returns size of remaining word list
    public int remWordsSize()
    {
        return remWords.size();
    }

    // writes ArrayList<String> remWords to a .txt file using PrintWriter
    public void writeRemWords()
    {
        // catches FileNotFoundException
        try
        {
            PrintWriter out = new PrintWriter("remWords.txt");
            for(String w : remWords)
            {
                out.println(w);
            }
            out.close();
        }
        catch(FileNotFoundException e)
        {
            System.err.println("remWords.txt not found");
        }
    }
}
