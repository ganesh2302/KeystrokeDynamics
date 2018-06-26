import java.io.File;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

class test
{
    private static final String FILENAME = "data.txt";
    private static final int NUM_INPUTS = 31,NUM_PATTERNS = 100,NUM_HIDDEN = 62,NUM_EPOCHS = 1000;
    private static final double LR_IH = 0.7,LR_HO = 0.07;
    private static double hiddenVal[] = new double[NUM_HIDDEN];
    private static double weightsIH[][] =  new double[NUM_INPUTS][NUM_HIDDEN];
    private static double weightsHO[] = new double[NUM_HIDDEN];

    private static double trainInputs[][] = new double[NUM_PATTERNS][NUM_INPUTS];
    private static int trainOutput[] = new int[NUM_PATTERNS];

    private static double errThisPat = 0.0;
    private static double outPred = 0.0;
    private static double RMSerror = 0.0;

    void algorithm()
    {
        initWeights();
        initData();
        displayResults();

    }

    private static void initWeights()
    {
        //  Initialize weights to random values.
        BufferedReader br = null;
        FileReader fr = null;
        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);
            for(int j = 0; j < NUM_HIDDEN; j++)
            {
                weightsHO[j] = Double.parseDouble(br.readLine());
                for(int i = 0; i < NUM_INPUTS; i++)
                {
                    weightsIH[i][j] = Double.parseDouble(br.readLine());
                    System.out.println("Weight = " + weightsIH[i][j]);

                } // i
            } // j

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

    }
    private static void initData()
    {
        File text = new File("test.txt");
        try {
            Scanner scnr = new Scanner(text);

            for (int i = 0; i < NUM_PATTERNS; i++) {
                for (int j = 0; j < NUM_INPUTS; j++) {
                    trainInputs[i][j] = scnr.nextDouble();
                }
                if(i<=1)
                trainOutput[i] = 1;
                else
                    trainOutput[i] = 0;
            }
        }catch(FileNotFoundException fp)
        {
            fp.printStackTrace();
        }
        return;
    }

    private static void calcNet(final int patNum)
    {
        // Calculates values for Hidden and Output nodes.
        for(int i = 0; i < NUM_HIDDEN; i++)
        {
            hiddenVal[i] = 0.0;
            for(int j = 0; j < NUM_INPUTS; j++)
            {
                hiddenVal[i] += (trainInputs[patNum][j] * weightsIH[j][i]);
            } // j
            hiddenVal[i] = Math.tanh(hiddenVal[i]);
        } // i

        outPred = 0.0;

        for(int i = 0; i < NUM_HIDDEN; i++)
        {
            outPred += hiddenVal[i] * weightsHO[i];
        }

        errThisPat = outPred - trainOutput[patNum]; // Error = "Expected" - "Actual"
        return;
    }


    private static void displayResults()
    {
        for(int i = 0; i < NUM_PATTERNS; i ++)
        {
            calcNet(i);
            System.out.println("pat = " + (i + 1) + " actual = " + trainOutput[i] + " neural model = " + outPred);
        }
        return;
    }
    public static void main(String args[])
    {
        test xs=new test();
        xs.algorithm();
    }
}