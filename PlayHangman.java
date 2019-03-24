//***********************************************
//
// PlayHangman class
//
// This class creates a Hangman object and plays
// the game
//
//************************************************

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;

public class PlayHangman
{
    public static void main(String[] args) throws FileNotFoundException
    {
        // declare instance variables
        Scanner sc = new Scanner(System.in);
        ArrayList<String> dictionary = new ArrayList<String>();
        int len, numGuesses;
        char guess, yorn;
        boolean runningTotal = false;

        // read dictionary.txt or catch FileNotFoundException
        try
        {
            Scanner readFile = new Scanner(new File("dictionary.txt"));
            while(readFile.hasNextLine())
            {
                dictionary.add(readFile.nextLine());
            }
        }
        catch(FileNotFoundException e)
        {
            System.err.println("dictionary.txt not found");
            System.exit(0);
        }

        // start game
        while(true)
        {
            System.out.println("Welcome to (Evil) Hangman!");
            // get word length and check validity
            while(true)
            {
                System.out.println("Please enter the word length");
                System.out.print("> ");
                len = sc.nextInt();

                if(len <= 29 && len > 1 && len != 26 && len != 27)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid word length.");
                }
            }

            // get number of guesses and check validity
            System.out.println();
            while(true)
            {
                System.out.println("Please enter the number of guesses");
                System.out.print("> ");
                numGuesses = sc.nextInt();

                if(numGuesses > 0)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid number of guesses.");
                }
            }
            System.out.println();

            // running total for testing purposes
            System.out.println("Would you like to have a running total of the no. of remaining words? (y or n)");
            System.out.print("> ");
            yorn = sc.next().charAt(0);
            if(yorn == 'y')
            {
                runningTotal = true;
            }

            // create Hangman object
            Hangman evil = new Hangman(numGuesses, len, dictionary);
            System.out.println(evil.showWord());

            // user guesses
            while(!evil.gameOver())
            {
                // get guess and check repeats
                while(true)
                {
                    System.out.println("Enter your guess");
                    System.out.print("> ");
                    guess = sc.next().charAt(0);
                    if(evil.checkRepeat(guess))
                    {
                        break;
                    }
                    else
                    {
                        System.out.println("You cannot guess the same letter twice.");
                    }
                }

                if(evil.check(guess))
                {
                    System.out.println("Correct!");
                }
                else
                {
                    System.out.println("Incorrect guess!");
                }

                System.out.println();
                System.out.println(evil.showWord());
                System.out.println("Guesses left: " + evil.getNumGuesses());
                System.out.println("Guessed letters: " + evil.getGuesses());
                if(runningTotal)
                {
                    System.out.println("# of words remaining: " + evil.remWordsSize());
                }
            }

            // win/lose output
            if(evil.win())
            {
                System.out.println("Congratulations! You won!");
            }
            else
            {
                System.out.println("You lost!");
            }
            System.out.println("The answer was: " + evil.getAnswer());

            // write remaining words to .txt for testing purposes
            System.out.println();
            System.out.println("Would you like the remaining words to be written to a text file? (y or n)");
            System.out.print("> ");
            yorn = sc.next().charAt(0);
            if(yorn == 'y')
            {
                evil.writeRemWords();
            }

            System.out.println();
            System.out.println("Would you like to play again? (y or n)");
            System.out.print("> ");
            yorn = sc.next().charAt(0);
            if(yorn == 'n')
            {
                break;
            }
            System.out.println();
        }
    }
}
