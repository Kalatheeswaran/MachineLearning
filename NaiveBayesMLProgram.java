
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NaiveBayesMLProgram {

	static int count = 0;
	int positiveCount = 0, negativeCount = 0, neutralCount = 0;
	int totalNumberOfWords = 0, totalNumberOfGames = 0;
	int dataSetStart = 1;
	int dataSetEnd = 10000;

	
	private int testDataCountStart;
	private int testDataCountEnd;

	public int fold;
	
	
	/**
	 *An ArrayList of ArrayList gameName has been created for the gameName and the particular class it belongs based on the comment 
	 *
	 */
	ArrayList<ArrayList<String>> gameName = new ArrayList<ArrayList<String>>();
	ArrayList<String> subGameName = new ArrayList<String>();
	ArrayList<String> subGameName0 = new ArrayList<String>();
	ArrayList<String> subGameName1 = new ArrayList<String>();
	ArrayList<String> subGameName2 = new ArrayList<String>();

	/**
	 * An ArrayList of ArrayList content has been created for the comments and the particular class it belongs
	 */
	ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
	ArrayList<String> contentName = new ArrayList<String>();
	ArrayList<String> contentName0 = new ArrayList<String>();
	ArrayList<String> contentName1 = new ArrayList<String>();
	ArrayList<String> contentName2 = new ArrayList<String>();
	
	public static ArrayList<Double> allAccuracy=new ArrayList<Double>();
	
	public NaiveBayesMLProgram() {
		

		/**
		 * The ArrayLists subGameName, subGameName0, subGameName1, subGameName2 are added to the ArrayList gameName
		 */
		gameName.add(subGameName);
		gameName.add(subGameName0);
		gameName.add(subGameName1);
		gameName.add(subGameName2);

		
		/**
		 * The ArrayLists contentName, contentName0, contentName1, contentName2 are added to the ArrayList content
		 */
		content.add(contentName);
		content.add(contentName0);
		content.add(contentName1);
		content.add(contentName2);
		
		//The ArrayLists subGameName is initialized to null and subGameName0, subGameName1, subGameName2 are all initialized to 1 to avoid the zero-frequency problem.
		for (int i = 0; i < 1000; i++) {
			subGameName.add(null);
			subGameName0.add("1");
			subGameName1.add("1");
			subGameName2.add("1");
		}
		//The ArrayLists contentName is initialized to null and contentName0, contentName1, contentName2 are all initialized to 1 to avoid the zero-frequency problem.
		for (int i = 0; i < 30000; i++) {
			contentName.add(null);
			contentName0.add("1");
			contentName1.add("1");
			contentName2.add("1");
		}
	}

	/**
	 * @param line
	 * @return processData
	 * Each line is scanned for commas and in the first two occurrences of a comma the game name and the class label are obtained and 
	 * are allocated to a String array ‘processData’. If the commas occur for more than two instances they are removed and the entire 
	 * comment is allocated to the ‘processData’ array. Thus the processData array holds game name, class label and the comments on the 
	 * game (with the commas removed).
	 */
	public String[] preProcessData(String line) {
		char[] charStr = line.toCharArray();
		String[] processData = {"", "", ""};
		int l = 0;
		for (int k = 0; k < charStr.length; k++) {
			if (l <= 2) {
				if (charStr[k] != ',') {
					processData[l] = processData[l] + charStr[k];
				} else {
					l++;
				}
			} else {
				if (charStr[k] != ',') {
					processData[l - 1] = processData[l - 1] + charStr[k];
				}
			}
		}
		return processData;
	}
	
	//Classifying the training dataset
	public void TrainingData() {
		BufferedReader br = null;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\dataset-cs6735.csv"));
			for (int i = dataSetStart; i <= dataSetEnd; i++) {
				line = br.readLine();
				if (!(i >= this.testDataCountStart && i <= this.testDataCountEnd)) 
				{
					String[] data = preProcessData(line);
					String game = data[0];
					String label = data[1];
					String contentStr = data[2];
					count++;
					
					if (label.equals("0")) {
						neutralCount++;
					} else if (label.equals("1")) {
						positiveCount++;
					} else if (label.equals("2")) {
						negativeCount++;
					}
					search1:
						for (int j = 0; j < subGameName.size(); j++) {
							if ((subGameName.get(j) != null) && (subGameName.get(j)).equals(game)) {
								if (label.equals("0")) {
									int number = Integer.parseInt(subGameName0.get(j));
									number++;
									subGameName0.set(j, Integer.toString(number));
								} else if (label.equals("1")) {
									int number = Integer.parseInt(subGameName1.get(j));
									number++;
									subGameName1.set(j, Integer.toString(number));
								} else if (label.equals("2")) {
									int number = Integer.parseInt(subGameName2.get(j));
									number++;
									subGameName2.set(j, Integer.toString(number));
								}
								break search1;
							}//end if#2
							else if ((subGameName.get(j) == null) || (subGameName.get(j).isEmpty())) {
								subGameName.set(j, game);
								if (label.equals("0")) {
									int number = Integer.parseInt(subGameName0.get(j));
									number++;
									subGameName0.set(j, Integer.toString(number));
									totalNumberOfGames++;
									break;
								} else if (label.equals("1")) {
									int number = Integer.parseInt(subGameName1.get(j));
									number++;
									subGameName1.set(j, Integer.toString(number));
									totalNumberOfGames++;
									break;
								} else if (label.equals("2")) {
									int number = Integer.parseInt(subGameName2.get(j));
									number++;
									subGameName2.set(j, Integer.toString(number));
									totalNumberOfGames++;
									break;
								}
							}//end else if#2
						}//end for#2

					String[] words = contentStr.split(" ");

					for (String s : words) {
						for (int j = 0; j < contentName.size(); j++) {
							if ((contentName.get(j) != null) && (contentName.get(j)).equalsIgnoreCase(s)) {

								if (label.equals("0")) {
									int number = Integer.parseInt(contentName0.get(j));
									number++;
									totalNumberOfWords++;
									contentName0.set(j, Integer.toString(number));
								}

								if (label.equals("1")) {
									int number = Integer.parseInt(contentName1.get(j));
									number++;
									totalNumberOfWords++;
									contentName1.set(j, Integer.toString(number));
								}
								if (label.equals("2")) {
									int number = Integer.parseInt(contentName2.get(j));
									number++;
									totalNumberOfWords++;
									contentName2.set(j, Integer.toString(number));
								}
								break;
							} else if ((contentName.get(j) == null) || (contentName.get(j).isEmpty())) {
								contentName.set(j, s);
								if (label.equals("0")) {
									int number = Integer.parseInt(contentName0.get(j));
									number++;
									totalNumberOfWords++;
									contentName0.set(j, Integer.toString(number));
									break;
								}
								if (label.equals("1")) {
									int number = Integer.parseInt(contentName1.get(j));
									number++;
									totalNumberOfWords++;
									contentName1.set(j, Integer.toString(number));
									break;
								}
								if (label.equals("2")) {
									int number = Integer.parseInt(contentName2.get(j));
									number++;
									totalNumberOfWords++;
									contentName2.set(j, Integer.toString(number));
									break;
								}
							}
						}//end for
					}
				}

			}//for end#1

			//System.out.println("neutralCount " + neutralCount + "positiveCount " + positiveCount + "negativeCount " + negativeCount + "totalNumberOfGames " + totalNumberOfGames + "totalNumberOfWords " + totalNumberOfWords);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//Classifying the test dataset
	public void TestData() {
		int TotalClass = positiveCount + negativeCount + neutralCount;
		Double Accuracy = 0.0, Positives = 0.0, Negatives = 0.0, Neutral = 0.0;
		BufferedReader brTestData = null;
		String lineTestData = "";

		try {
			brTestData = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\dataset-cs6735.csv"));
			for (int i = dataSetStart; i <= dataSetEnd; i++) {
				lineTestData = brTestData.readLine();
				String[] testData = preProcessData(lineTestData);
				String testGame = testData[0];
				String testLabel = testData[1];
				String testContentStr = testData[2];
				Double positiveContentProduct = 1.0, negativeContentProduct = 1.0, neutralContentProduct = 1.0;
				String[] testWords = testContentStr.split(" ");
				if (i > testDataCountStart && i <= testDataCountEnd) {
					for (String s : testWords) {
						
						for (int j = 0; j < contentName.size(); j++) {

							if ((contentName.get(j) != null) && (contentName.get(j).equalsIgnoreCase(s))) 
							{
								if ((contentName0.get(j)).equals("0")) {
									contentName0.set(j, "1");
								}
								if ((contentName1.get(j)).equals("0")) {
									contentName1.set(j, "1");
								}
								if ((contentName2.get(j)).equals("0")) {
									contentName2.set(j, "1");
								}
								
								Double posContent = Double.parseDouble(contentName1.get(j));
								Double tot = Double.parseDouble(contentName0.get(j)) + Double.parseDouble(contentName1.get(j)) + Double.parseDouble(contentName2.get(j));
								Double numPos = (posContent / (double) positiveCount) * ((double) positiveCount / TotalClass);
								Double den = tot / (double) totalNumberOfWords;
								positiveContentProduct = positiveContentProduct * (numPos / den);
								Double negContent = Double.parseDouble(contentName2.get(j));
								Double numNeg = (negContent / (double) negativeCount) * ((double) negativeCount / TotalClass);
								negativeContentProduct = negativeContentProduct * (numNeg / den);
								Double neutContent = Double.parseDouble(contentName0.get(j));
								Double numNeut = (neutContent / (double) neutralCount) * ((double) neutralCount / TotalClass);
								neutralContentProduct = neutralContentProduct * (numNeut / den);
							}
						}
					
					}

					
					for (int j = 0; j < subGameName.size(); j++) {
						if ((subGameName.get(j) != null) && (subGameName.get(j).equalsIgnoreCase(testGame))) {
							if ((subGameName0.get(j)).equals("0")) {
								subGameName0.set(j, "1");
							}
							if ((subGameName1.get(j)).equals("0")) {
								subGameName1.set(j, "1");
							}
							if ((subGameName2.get(j)).equals("0")) {
								subGameName2.set(j, "1");
							}
							
							Double posGame = Double.parseDouble(subGameName1.get(j));
							Double tot = Double.parseDouble(subGameName0.get(j)) + Double.parseDouble(subGameName1.get(j)) + Double.parseDouble(subGameName2.get(j));
							Double numPosGame = (posGame / (double) positiveCount) * ((double) positiveCount / TotalClass);
							Double den = tot / (double) totalNumberOfGames;
							positiveContentProduct = positiveContentProduct * (numPosGame / den);
							Double negGame = Double.parseDouble(subGameName2.get(j));
							Double numNegGame = (negGame / (double) negativeCount) * ((double) negativeCount / TotalClass);
							negativeContentProduct = negativeContentProduct * (numNegGame / den);
							Double neutGame = Double.parseDouble(subGameName0.get(j));
							Double numNeutGame = (neutGame / (double) neutralCount) * ((double) neutralCount / TotalClass);
							neutralContentProduct = neutralContentProduct * (numNeutGame / den);
						}
					}
					
					if (neutralContentProduct > positiveContentProduct && neutralContentProduct > negativeContentProduct) {
						if (testLabel.equals("0")) {
							Accuracy++;
							Neutral++;
						}
					} else if (positiveContentProduct > negativeContentProduct && positiveContentProduct > neutralContentProduct) {
						if (testLabel.equals("1")) {
							Accuracy++;
							Positives++;
						}
					} else if (negativeContentProduct > neutralContentProduct && negativeContentProduct > positiveContentProduct) {
						if (testLabel.equals("2")) {
							Accuracy++;
							Negatives++;
						}
					}
				}
			}
			Accuracy = (Accuracy / this.fold) * 100;
			NaiveBayesMLProgram.allAccuracy.add(Accuracy);
			//System.out.println("Accuracy : " + Accuracy + " Positives : " + Positives + " Negatives : " + Negatives + "Neutral : " + Neutral);
			System.out.println("Accuracy : " + Accuracy);
			System.out.println();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (brTestData != null) {
				try {
					brTestData.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//Data is shuffled randomly for the m times k fold cross validation
	public static void shuffleData(String fileName) throws Exception {
		File file = new File(System.getProperty("user.dir")+"\\"+fileName);
		ArrayList<String> shuffleData = new ArrayList<String>();
		Random rndGen = new Random();
		BufferedReader br2 = null;
		br2 = new BufferedReader(new FileReader(System.getProperty("user.dir") +"\\"+fileName));
		try 
		{
			for (int i = 0; i < 10000; i++) {
				shuffleData.add(i, br2.readLine());
			}
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		finally
		{     
			if (br2 != null)
			{
				try {br2.close();} 
				catch (IOException e) { e.printStackTrace();}
			}
		} 
		for (int i = 0; i < shuffleData.size(); i++) {
			int mid = shuffleData.size() / 2;
			int start = rndGen.nextInt(mid);
			int end = rndGen.nextInt(shuffleData.size() - 1);
			Collections.swap(shuffleData, start, end);
		}

		FileWriter fstream = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(fstream);

		for (int i = 0; i < shuffleData.size(); i++) {
			out.append(shuffleData.get(i));
			out.newLine();
		}
		out.close();
		fstream.close();
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the fold number 'k':");
		int fold = in.nextInt();
		System.out.println("Enter the number of times to run 'm':");
		int times = in.nextInt();
		in.close();
		double averageAccuracy=0.0,sumOfAccuracy=0.0;
		for(int j=0;j<times;j++) {
		
			try {
				System.out.println("Shuffling the dataset");
				NaiveBayesMLProgram.shuffleData("dataset-cs6735.csv");
				System.out.println("Executing Naive Bayes, Time m="+(j+1));
				System.out.println("----------------------------------------------------------------");
				for (int i = 1; i <= fold; i++) {
					NaiveBayesMLProgram nB = new NaiveBayesMLProgram();
					nB.fold = 10000 / fold;

					if (i == 1) {
						nB.testDataCountStart = 1;
						nB.testDataCountEnd = nB.fold;

					} else {
						nB.testDataCountStart = nB.fold * (i - 1);
						nB.testDataCountEnd = nB.fold * i;
					}
					System.out.println("TestDataStart: "+nB.testDataCountStart+" TestDataEnd: "+nB.testDataCountEnd);
					nB.TrainingData();
					nB.TestData();
					
					
				}
				System.out.println("----------------------------------------------------------------");
				System.out.println();
			} catch (Exception ex) {
				Logger.getLogger(NaiveBayesMLProgram.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		//find the total accuracy
		for(int t=0;t<NaiveBayesMLProgram.allAccuracy.size();t++)
		{
			sumOfAccuracy=sumOfAccuracy+NaiveBayesMLProgram.allAccuracy.get(t);
		}
		
		//find the average accuracy
		averageAccuracy=sumOfAccuracy/(double)NaiveBayesMLProgram.allAccuracy.size();
		System.out.println("Average Accuracy: "+averageAccuracy);
		System.out.println();
		double sumOfDiff=0.0;
		double []diff=new double[allAccuracy.size()];
		
		//calculating the standard deviation
		for(int j=0;j<allAccuracy.size();j++)
		{
			diff[j]=allAccuracy.get(j)-averageAccuracy;
			diff[j]=Math.pow(diff[j], 2);
		}
		
		for(double d:diff)
		{
			sumOfDiff+=d;
		}
		
		sumOfDiff=sumOfDiff/(double)allAccuracy.size();
		double standardDeviation=Math.sqrt(sumOfDiff);
		System.out.println("Standard Deviation: "+standardDeviation);
		System.out.println();
		
	}
}
