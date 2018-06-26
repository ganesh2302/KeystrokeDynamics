import java.io.*;
import java.util.Random;
import java.util.Scanner;

class train
{
    private static final String FILENAME = "data.txt";
    private static final int NUM_INPUTS = 31,NUM_PATTERNS = 400,NUM_HIDDEN = 62,NUM_EPOCHS = 1000;
    private static final double LR_IH = 0.3,LR_HO = 0.07;
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
        int patNum = 0;

        initWeights();
        initData();
        for(int j = 0; j < NUM_EPOCHS; j++)
        {
            for(int i = 0; i < NUM_PATTERNS; i++)
            {
                patNum = (i);
                calcNet(patNum);
                WeightChangesHO();
                WeightChangesIH(patNum);

            }
            RMSerror = calcOverallError();
            System.out.println("epoch = " + j + " RMS Error = " + RMSerror);

        }

        displayResults();
        WeightSave();

        return;
    }
    private static void initWeightstest()
    {
        //  Initialize weights to random values.
        for(int j = 0; j < NUM_HIDDEN; j++)
        {
            //weightsHO[j] = (new Random().nextDouble()-0.5)/2.0;
            Random r1=new Random();
            weightsHO[j] = r1.nextDouble();
            for(int i = 0; i < NUM_INPUTS; i++)
            {
                //weightsIH[i][j] = (new Random().nextDouble()-0.5)/5.0;
                Random r2=new Random();
                weightsIH[i][j] =r2.nextDouble();
                System.out.println("Weight = " + weightsIH[i][j]);

            } // i
        } // j

        return;
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
    private static void WeightSave()
    {
        //  Initialize weights to random values.
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(FILENAME);
            bw = new BufferedWriter(fw);
            for(int j = 0; j < NUM_HIDDEN; j++)
            {
                bw.write(Double.toString(weightsHO[j])+'\n');
                for(int i = 0; i < NUM_INPUTS; i++)
                {
                    bw.write(Double.toString(weightsIH[i][j])+'\n');

                } // i
            } // j
            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        }
        finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

    }
    private static void initData()
    {
        File text = new File("keystroke-dynamics.txt");
        try {
            Scanner scnr = new Scanner(text);

            for (int i = 0; i < NUM_PATTERNS; i++) {
                for (int j = 0; j < NUM_INPUTS; j++) {
                    trainInputs[i][j] = scnr.nextDouble();
                }
                trainOutput[i] = 1;
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
                hiddenVal[i]+=(trainInputs[patNum][j] * weightsIH[j][i]);
            } // j
            hiddenVal[i] =1/(1- Math.exp(-hiddenVal[i]));
        } // i

        outPred = 0.0;

        for(int i = 0; i < NUM_HIDDEN; i++)
        {
            outPred += hiddenVal[i] * weightsHO[i];
        }

        errThisPat = outPred - trainOutput[patNum];
        // Error = "Expected" - "Actual"
        return;
    }

    private static void WeightChangesHO()
    {
        errThisPat =errThisPat*(1-errThisPat); // Adjust the Hidden to Output weights.
        for(int k = 0; k < NUM_HIDDEN; k++)
        {
            double weightChange = LR_HO * errThisPat * hiddenVal[k];
            weightsHO[k] -= weightChange;

            // Regularization of the output weights.
          //  if(weightsHO[k] < -10.0){
            //    weightsHO[k] = -10.0;
            //}else if(weightsHO[k] > 10.0){
              //  weightsHO[k] = 10.0;
            //}
        }
        return;
    }

    private static void WeightChangesIH(final int patNum)
    {
        // Adjust the Input to Hidden weights.

        for(int i = 0; i < NUM_HIDDEN; i++)
        {
            for(int k = 0; k < NUM_INPUTS; k++)
            {
                double x = hiddenVal[i]*(1 -hiddenVal[i]);
                double weightChange = x * weightsHO[i] * errThisPat * LR_IH;
                weightsIH[k][i] -= weightChange;
            } // k
        } // i
        return;
    }

    private static double calcOverallError()
    {
        double errorValue = 0.0;

        for(int i = 0; i < NUM_PATTERNS; i++)
        {
            calcNet(i);
            errorValue +=Math.pow(errThisPat, 2);
        }
        errorValue=errorValue/2;
return errorValue;
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
        train xs=new train();
        xs.algorithm();
    }
}