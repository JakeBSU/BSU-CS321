import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Test{
    private static final int ERROR_EXIT = 1;
    private static final int DELAY = 1000;  //for a progress bar.
    private enum ERROR_CAUSE{NOT_ENOUGH_ARGS, NO_ARGS, NO_FILE, CACHE_SIZE_MISMATCH}
    private static Cache<String> cacheOne, cacheTwo;
    private static boolean twoLevelCache = false;  //keep track of whether or not it's a level two cache.
    private static boolean isDone = false; //keep track of if it's done.


    public static void main(String[] args){
        if (args.length < 1){
            printErr(ERROR_CAUSE.NO_ARGS);
            System.exit(ERROR_EXIT);
        }

        //Check the simulation type.
        if (args[0].equalsIgnoreCase("1")){
            if (args.length < 3){
                printErr(ERROR_CAUSE.NOT_ENOUGH_ARGS);
                System.exit(ERROR_EXIT);
            }
            cacheOne = new Cache<>(Integer.parseInt(args[1]));
            readFile(new File(args[2]));
        } else if (args[0].equalsIgnoreCase("2")){
            if (args.length < 4){
                printErr(ERROR_CAUSE.NOT_ENOUGH_ARGS);
                System.exit(ERROR_EXIT);
            }
            if (Integer.parseInt(args[2]) <= Integer.parseInt(args[1])){
                printErr(ERROR_CAUSE.CACHE_SIZE_MISMATCH);
                System.exit(ERROR_EXIT);
            }
            twoLevelCache = true;
            cacheOne = new Cache<>(Integer.parseInt(args[1]));
            cacheTwo = new Cache<>(Integer.parseInt(args[2]));
            readFile(new File(args[3]));
        } else{
            printErr(ERROR_CAUSE.NOT_ENOUGH_ARGS);
            System.exit(ERROR_EXIT);
        }
    }

    private static void readFile(File file){
        int cacheOneMiss = 0;
        double totalNumRef = 0, levelOneNH = 0, levelTwoNH = 0;
        startAnimation();
        try{
            Scanner scan = new Scanner(file);
            String word;

            if (twoLevelCache){
                System.out.println("First level cache with " + cacheOne.getCacheLimit()
                        + " entries has been created.");
                System.out.println("Second level cache with " + cacheTwo.getCacheLimit()
                        + " entries has been created.");
            } else{
                System.out.println("First level cache with " + cacheOne.getCacheLimit()
                        + " entries has been created.");
            }
            System.out.println("------------------------------------------------------");

            while (scan.hasNextLine()){
                String line = scan.nextLine();

                @SuppressWarnings ("StringTokenizerDelimiter")
                StringTokenizer tokenizer = new StringTokenizer(line, "  \t");
                totalNumRef += tokenizer.countTokens();
                while (tokenizer.hasMoreTokens()){
                    word = tokenizer.nextToken();
                    if (twoLevelCache){
                        if (cacheOne.hit(word)){
                            levelOneNH++;
                            cacheOne.add(word);
                            cacheTwo.add(word);
                        } else
                        {
                            cacheOneMiss++;
                            if (cacheTwo.hit(word)){
                                levelTwoNH++;
                                cacheTwo.add(word);
                            }
                            cacheOne.add(word);
                        }
                        cacheOne.add(word);
                        cacheTwo.add(word);
                    } else{
                        if (cacheOne.hit(word)){
                            levelOneNH++;
                            cacheOne.add(word);
                        } else{
                            cacheOneMiss++;
                        }
                        cacheOne.add(word);
                    }
                }
            }
            scan.close(); //close scanner
            isDone = true; //be done
            
            //print stats
            System.out.println("\nTotal number of references: " + (int) totalNumRef);

            if (twoLevelCache){
                System.out.println("Total number of cache hits: " + (int) (levelOneNH + levelTwoNH)
                        + "\nThe global hit ratio: " + ((levelOneNH + levelTwoNH) / totalNumRef));
                System.out.println("\nNumber of 1st-Level cache hits: " + (int) levelOneNH + "\n1st-Level cache ratio: "
                        + (levelOneNH / totalNumRef));
                System.out.println("Number of 2nd-Level cache hits: " + (int) levelTwoNH + "\n2nd-Level cache ratio: "
                        + (levelTwoNH / cacheOneMiss));
            } else{
                System.out.println("Total number of cache hits: " + (int) levelOneNH);
                System.out.println("The global hit ratio: " + levelOneNH / totalNumRef);
            }
        } catch (FileNotFoundException e){
            System.err.println(file + ": No such file or directory.");
        }
    }

    private static void printErr(ERROR_CAUSE cause){
        String message =
                "\nUsage: <cache_system> <cache_size> [cache_two_size] <input_file>" +
                "\n\nOptions:" +
                "\n\tcache_system: the level of cache to simulate (valid options are 1 or 2)." +
                "\n\tcache_size: size of first cache. Size of second cache if cache_system is 2. \n\t" +
                "input_file: relative or absolute path to input file";

        switch (cause){
            case NO_ARGS:
                System.err.println(message);
                break;
            case NOT_ENOUGH_ARGS:
                System.err.println("ERROR: Incomplete arguments." + message);
                break;
            case NO_FILE:
                System.err.println("ERROR: No file was provided." + message);
                break;
            case CACHE_SIZE_MISMATCH:
                System.err.println("ERROR: size of 2nd-Level cache >  size of 1st-Level cache.");
                break;
            default:
                System.err.println(message);
                break;
        }
    }
    
    private static void startAnimation() {
        TimerActionListener taskPerformer = new TimerActionListener();
        new Timer(DELAY, taskPerformer).start();
    }

    private static class TimerActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            if (!isDone)
                System.out.print(".");
        }
    }
}