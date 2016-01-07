

import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.lang.reflect.Array;


	public class temp{
		
		static int i;
		static int nRow;// this variable sets the rows of array containing the dataset.Since all the data files have same dimensions, so i have used numbers 																													
		static int nCol;// this variable sets the column of the array containing the dataset 
		static boolean boolPrint = Boolean.FALSE;
		static int L1; 
		static int K1 ; 
		static String trainingDataSet;
		static String validationDataSet;
		static String testDataSet;
		static String print;
			@SuppressWarnings("resource")

			public static void main(String[] args) throws IOException {
			// TODO Auto-generated method stub;
			ArrayList<ArrayList<Integer>> arr1 = new ArrayList<ArrayList<Integer>>();
			int[][] myMultiArray = new int[21][600];
			L1 = Integer.parseInt(args[0]); 
			K1 = Integer.parseInt(args[1]); 
			trainingDataSet = args[2];
			validationDataSet = args[3];
			testDataSet = args[4];
			print = args[5];
	
			arr1=writeTree();
			myMultiArray= getRows(arr1);
			boolean boolPrint = Boolean.FALSE;
			
			attribute a;
			int[] attributeFlag;
			attributeFlag = new int[myMultiArray.length];
			for(int i=0; i<attributeFlag.length; i++)
				attributeFlag[i]=1;
			a=BuildTree(myMultiArray, attributeFlag);
			if(print.equalsIgnoreCase("Yes")){printTree(a, 1); }
			System.out.println("");
			System.out.println(treeAccuracy(a,testDataSet));//print the pre pruning accuracy
			attribute b = pruneIgTree(K1,L1, a);
			System.out.println(treeAccuracy(b, testDataSet));// prints the post pruning accuracy
		}
			
		@SuppressWarnings("resource")
		public static ArrayList writeTree() throws NumberFormatException, IOException{
		String csvFile = trainingDataSet;
		BufferedReader br = null;
		String line = "";
		int i=0;
		String cvsSplitBy = ",";
		String[] attributeSet ;
		String[] columns= null;
		int Attributes =0;
		ArrayList<ArrayList<Integer>> arr1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> arr2 = new ArrayList<Integer>();
		br = new BufferedReader(new FileReader(csvFile));
		line = br.readLine();
		attributeSet = line.split(cvsSplitBy);
		while ((line = br.readLine()) != null)
		{
			
			attributeSet = line.split(cvsSplitBy);
			for(i=0;i<attributeSet.length;i++){
				arr2.add(Integer.parseInt(attributeSet[i]));
			}
			arr1.add(arr2) ;
			arr2= new ArrayList();
		}

		Attributes = attributeSet.length;
			return arr1;
	}

			static int count;
		
			public static ArrayList<ArrayList<Integer>> readFile(String fileName) throws IOException{
				BufferedReader in = new BufferedReader(new FileReader(fileName), 4096);
				String line = in.readLine() ;
				String[] rowData = line.split(",");
				ArrayList<ArrayList<Integer>> fileData = new ArrayList<ArrayList<Integer>>();
			
				for(int i = 0;i<rowData.length-1;i = i+1){
						}		
			
				while ((line = in.readLine())!= null){
					rowData = line.split(",");
					ArrayList<Integer> a =  new ArrayList<Integer>();
				
				for(int i = 0;i<rowData.length;i++){
					a.add(Integer.parseInt(rowData[i]));
					}
					fileData.add(a);
				}
			
				in.close();
				return fileData;
			}
		   
		//This function get the rows from the arraylist of data and store in an array ( rows - attributes, cols - values)
				
		public static int[][] getRows(ArrayList arr1){
			
			int len = arr1.size();
			//int i=0;
			
			ArrayList arr1clone = (ArrayList) arr1.clone();
			Iterator itr1 =arr1clone.iterator();
			
			int[][] myMultiArray = new int[21][600];
			int index = 0;
			while(itr1.hasNext()){
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp = (ArrayList<Integer>) itr1.next();
				for(int j=0;j<temp.size();j++)
				{
					myMultiArray[j][index] = temp.get(j);
				}
				//////System.out.println(temp);
				index = index + 1;
			}
			
			int p = myMultiArray.length;
			int q = myMultiArray[0].length;
			
			//System.out.println(p+" " + q);
		return myMultiArray;
		}
		
		// This function counts number of 0s and 1s of the class column which is used later to calculate entropy
		public static Double[][] countClass(int[][] myMultiArray){
			
			
			int row= myMultiArray.length;
			int col = myMultiArray[0].length;
			int i,j=0;
		
			// Variable which stores summation of count of Zeroes and ones over the total
			
			Double[][] Arronezero = new Double[2][2];
			Double one=0.0,zero =0.0; //we have 2 classes
			Double value1=0.0;
			Double value2=0.0;
			
			
			for( i=0;i<1;i++){
				for( j=0;j<col;j++)
				{
				
				if(myMultiArray[row-1][j] == 0)
					{zero++;}
				else if (myMultiArray[row-1][j] == 1)
					{one++;}
				
				}
			}
				Arronezero[0][0]=zero;
				Arronezero[0][1]=one;
			
				
			return Arronezero;
		}
			
			public static Double[] entropyCalc(Double[][] Arronezero){
				
				////System.out.println(Arrays.deepToString(Arronezero));
				int row= Arronezero.length;
				int col = Arronezero[0].length;
				int i,j=0;
				Double total = 0.0; // stores the total count of one and zero classes
				Double zerofraction =0.0; // array stores the count of zero class for one attribute
				Double onefraction = 0.0; // stores the count of one class for one attribute
				//Double p =0.0; //stores the sum of the fraction of zero and one classes 
				Double[] ArrEntropy = new Double[row]; // stores the entropy
				Double Entropy=0.0;
				Double one=0.0,zero =0.0; //we have 2 classes
				
				
				for(i=0;i<1;i++){
				
					zero = Arronezero[0][0];
					one = Arronezero[0][1];
					//System.out.println("z" + zero + "o" + one);
					total = zero+ one;
					zerofraction = zero/total;
					onefraction	= one/total;
					if(zerofraction==0.0){ zerofraction=1.0;}
					if(onefraction==0.0){ onefraction=1.0;}
			Entropy = -(1.0) * zerofraction * (Math.log(zerofraction)/Math.log(2)) - (1.0) *(onefraction)*(Math.log(onefraction)/Math.log(2));
					if(!Double.isNaN(Entropy)){
					ArrEntropy[i] = Entropy;}
					else{
						ArrEntropy[i] = 0.0;
					}
					////System.out.println(Arrays.toString(ArrEntropy));
					//////System.out.println("Attribute: "+ i+" zero: "+ zero+" , "+" one: "+ one + " 

					//total: "+ total + "fractions: "+ zerofraction + " , "+ onefraction + " Entropy : " 
							//+  ArrEntropy[i]);
			}	
				////System.out.println();
					
				return ArrEntropy;
				}
			
			// this function calculates the leaf entropies ( Entropy for 0s) and ( Entropy for 1s) and also uses root entropy to calculate Gain
			
			public static Double[] informationGain(int[][] myMultiArray,Double[] ArrEntropy){
				
				int i=0,j=0;
				int row= myMultiArray.length;
				int col = myMultiArray[0].length;
				Double value=0.0;
				Double total1=0.0, total2=0.0;
				Double EntropyZero = 0.0 , EntropyOne = 0.0;
				Double zerozerofrac = 0.0 , zeroonefrac=0.0 , onezerofrac=0.0,oneonefrac=0.0;
				Double[] infoGain = new Double[row];
				Double zeroZero=0.0, oneZero=0.0,zeroOne=0.0,oneOne=0.0,zeros = 0.0,ones=0.0;
				////System.out.println(" row:"+ row);
				for(i=0;i<row-1;i++){
					zeroZero=0.0;
					zeroOne=0.0;
					oneZero=0.0;
					oneOne=0.0;
					zeros=0.0;
					ones=0.0;
					for(j=0;j<col;j++){				
					
					if(myMultiArray[row-1][j]== 0 && myMultiArray[i][j]==0 ){
						
						zeroZero++;
						zeros++;
					}
					
					if(myMultiArray[row-1][j]== 1 && myMultiArray[i][j]==0 ){
						
						zeroOne++;
						zeros++;
					}
					
					if(myMultiArray[row-1][j]== 0 && myMultiArray[i][j]==1 ){
						
						oneZero++;
						ones++;
					}

					if(myMultiArray[row-1][j]== 1 && myMultiArray[i][j]==1 ){
	
						oneOne++;
						ones++;
					}
				}
					
				total1 = zeroZero+ zeroOne; 
				total2 = oneZero + oneOne;
				
				zerozerofrac = zeroZero/total1;
				zeroonefrac	= zeroOne/total1;
				onezerofrac = oneZero/total2;
				oneonefrac = oneOne/total2;
				/*//System.out.print(" iteration : "+ i);
				//System.out.print(" zeroZero " +  " " + zeroZero);
				//System.out.print(" zeroOne " +  " " + zeroOne);
				//System.out.print(" oneZero " +  " " + oneZero);
				//System.out.print(" oneOne " + " " + oneOne);
				
				//System.out.print(" zerozerofrac " +  " " + zerozerofrac);
				//System.out.print(" zeroonefrac " +  " " + zeroonefrac);
				//System.out.print(" onezerofrac " + " " + onezerofrac);
				//System.out.print(" oneonefrac " + " " + oneonefrac);
				//System.out.print(" total2 " + " " + total2);
				//System.out.print(" total1 " + " " + total1);
				//System.out.print(" zeroes " + " " + zeros);
				//System.out.print(" ones " + " " + ones);*/
				//System.out.println(zerozerofrac);
				if(zerozerofrac==0.0||Double.isNaN(zerozerofrac)){zerozerofrac=1.0;}
				if(zeroonefrac==0.0||Double.isNaN(zeroonefrac)){ zeroonefrac=1.0;}
				if(onezerofrac==0.0||Double.isNaN(onezerofrac)){ onezerofrac=1.0;}
				if(oneonefrac==0.0||Double.isNaN(oneonefrac)){ oneonefrac=1.0;}
				
				EntropyZero = -(1.0) * zerozerofrac * (Math.log(zerozerofrac)/Math.log(2)) - (1.0) * (zeroonefrac)*(Math.log(zeroonefrac)/Math.log(2));
				EntropyOne = -(1.0) * onezerofrac * (Math.log(onezerofrac)/Math.log(2)) - (1.0) * (oneonefrac)*(Math.log(oneonefrac)/Math.log(2));
				/*if(Double.isNaN(EntropyZero)){
					EntropyZero = 0.0;
					}
				if(Double.isNaN(EntropyOne)){
					EntropyOne = 0.0;
					}
				*/
				//System.out.println("zo"+zeros + " " + ones);
				value = ArrEntropy[0]-((zeros/(zeros+ones))*EntropyZero+ (ones/(zeros+ones))*EntropyOne);
				////System.out.println(" Root Entropy: "+ ArrEntropy[i]+ " E0: "+ EntropyZero + " E1: " + //EntropyOne );
				////System.out.println(" Root Entropy: "+ ArrEntropy[0]);
				////System.out.println(" Gain: "+ value);
				//value = ArrEntropy[i]-(  (EntropyZero) + ( EntropyOne));
				if(i==2){
					//System.out.println(zeroOne+"a");
				}
				if(!Double.isNaN(value)){
					infoGain[i] = value;}
					else{
						infoGain[i] = 0.0;
					}
				////System.out.println();
				////System.out.println(infoGain[i]);
				
			}
				
				return infoGain;
		}
		
			// building the tree
			public static attribute BuildTree(int[][] myMultiArray, int[] attributeF){
				int flag=0;
				for(int i=0; i<attributeF.length; i++){
					if(attributeF[i]==1)
						flag++;
				}
				
				//System.out.println("attribute" + Arrays.toString(attributeF));
				//System.out.println("multiarray length" + myMultiArray[0].length);
				if(flag>=1){
					int row= myMultiArray.length;
					int col = myMultiArray[0].length;
					int i,j=0;
					int[][] S0 = new int[row][col];
					int[][] S1 = new int[row][col];
					int[][] newS0 = new int[row][col];
					int[][] newS1 = new int[row][col];
					Double[][] Arronezero = new Double[row][row];
					Double[] ArrEntropy = new Double[row];
					Double[] infoGain1 = new Double[row];
					
					Arronezero=countClass(myMultiArray);
					ArrEntropy = entropyCalc(Arronezero);
					infoGain1=informationGain(myMultiArray,ArrEntropy);
					int bestIndex = getbestAttribute(infoGain1, attributeF); // finding the index 
					//System.out.println(Arrays.deepToString(infoGain1)+"m");
					int[] attributeF1 = attributeF.clone();
					int[] attributeF2 = attributeF.clone();
					//System.out.println("best index"+bestIndex);
					attributeF1[bestIndex] = 0;
					attributeF2[bestIndex] = 0;
					//System.out.println(Arrays.toString(infoGain1));
					
					//for(int q=0; q<attributeF.length; q++)
						////System.out.print(attributeF[q]);
					////System.out.println();
					int val = 0;
					int z=0,o=0;
					for(i=0; i<col; i++){
						if(myMultiArray[row-1][i]==0)
							z++;
						else
							o++;
					}
					
					if(z==0)
						return new attribute(1, null, null);
					else if(o==0)
						return new attribute(0, null, null);
					else{
						int[][]s0 = BuildTreewith0(myMultiArray, bestIndex);
						int[][]s1 = BuildTreewith1(myMultiArray, bestIndex);
						////System.out.println(bestIndex+".........");
						return new attribute(bestIndex+100, BuildTree(s0, attributeF1), BuildTree(s1, attributeF2));
					}
						
				}
				else
					return new attribute(-1);
				
			}
			
						
				//return new attribute(bestIndex,BuildTree(newS0),BuildTree(newS1));
				
			
			//tree building where the attribute value is 0 and class col value is 0 and 1.
			public static int[][] BuildTreewith0(int[][] myMultiArray, int index){
				
				int i,j=0;int count=0;
				int row= myMultiArray.length;
				int col = myMultiArray[0].length;
				//int[][] S0 = new int[row][col];
				int z=0;
				
				
				for(i=0; i<col; i++){
					if(myMultiArray[index][i]==0)
						z++;
				}
				int[][] s0 = new int[row][z];
				for(i=0; i<col; i++){
					if(myMultiArray[index][i]==0){
						for(int k=0; k<row;k++){
							s0[k][j]=myMultiArray[k][i];
							
						}
						
						j++;
					}
				}
				
				
				return s0;
			}
		//tree building where the attribute value is 1 and class col value is 0 and 1.	
		public static int[][] BuildTreewith1(int[][] myMultiArray, int index){
				
			int i,j=0;int count=0;
			int row= myMultiArray.length;
			int col = myMultiArray[0].length;
			//int[][] S0 = new int[row][col];
			int o=0;
			
			
			for(i=0; i<col; i++){
				if(myMultiArray[index][i]==1)
					o++;
			}
			int[][] s1 = new int[row][o];
			for(i=0; i<col; i++){
				if(myMultiArray[index][i]==1){
					for(int k=0; k<row;k++){
						s1[k][j]=myMultiArray[k][i];
			
					}
					
					j++;
				}
			}
			
			
			
			return s1;
		}
		
		
		public static double treeAccuracy(attribute root, String fileName) throws IOException{
				ArrayList<ArrayList<Integer>> fileData2 = readFile(fileName);
				int correct = 0;
				int incorrect = 0;
				attribute t = new attribute(0);
				t = root;
				double accuracy = 0;
			
			for(ArrayList<Integer> i:fileData2){
					while(t.root != 0 && t.root != 1){
						//while(t.root != -1){
						//int attributeroot = attributesNames.rootOf(t.root);
						int attributeroot = t.root-100;
						////System.out.println(t.root);
						////System.out.println(attributesNames);
						int attributeValue = i.get(attributeroot);
						if(attributeValue == 0)
						t = t.left;
					else
						t = t.right;
					}
				
					if(t.root == i.get(i.size()-1)){
						correct++;
					////System.out.println(t.root);
					////System.out.println("correct");
					}
					else{
						incorrect++;
					////System.out.println(t.root);
					////System.out.println("incorrect");
					}
					t = root;
				}
				//System.out.println("c" + correct);
				//System.out.println("i" + incorrect);
				accuracy = ((double)correct)/(correct+incorrect)*100;
				return accuracy;
			
			}
		
				public static void printTree(attribute root, int flag){
			
					if (root.left  ==  null|| root.right == null){ 
						System.out.print(": "+root.root);
				  return;
					}
					String a = "";
					for(int i = 0;i<flag;i++){
						a = a+"| ";
					}
			  
					System.out.println();
					System.out.print(a+aname[root.root-100] + " = 0");
					printTree(root.left,++flag);
			  
					flag--;
					System.out.println();
					System.out.print(a+aname[root.root-100] + " = 1");
					printTree(root.right,++flag);
				}
		
				public static void printTree1(attribute root, int level)
				{
					if(root==null)
						return;
		   
					printTree(root.right, level+1);
		    
					if(level!=0){
						for(int i=0;i<level-1;i++)
							System.out.print("|\t");
							System.out.println("|_______"+root.root);
						}
					else
							System.out.println(root.root);
							printTree(root.left, level+1);
						} 
		public static int[][] UpdateAttributes(int [][] S, int bestIndex){
		
			int i,j=0;
			int row= S.length;
			int col = S[0].length;
			int[][] newS = new int[row][col];
			
			for(i=0;i<row;i++){
				for(j=0;j<col;j++){
					
					if(i != bestIndex){
					
						newS[i][j] = S[i][j]; 
					}
				}
			}
			return newS;
	}
			
			 public static int getbestAttribute(Double[] infoGain, int[] attributeFlag){ 
				 int index=0,i=0;  
				 //System.out.println(Arrays.toString(infoGain));
				 Double maxValue = Double.MIN_VALUE;
				    for(i=0;i < (infoGain.length-1);i++){ 
				    	if(Double.isNaN(infoGain[i]))
				    		infoGain[i] = 0.0;
				      if(infoGain[i] > maxValue && attributeFlag[i]==1){ 
				         maxValue = infoGain[i];
				         index= i;
				         //////System.out.println("lll"+index);
				      } 
				    } 
				    if(index==0&&attributeFlag[index]==0){
				    	for(int j=0; j<attributeFlag.length; j++){
				    		if(attributeFlag[j]==1)
				    			return j;
				    	}
				    }
				    ////System.out.println(index);
				    return index; 
				  }
		//class for each training set attribute
			
		public static class attribute{
			int root;
			attribute right;
			attribute left;
			
			public attribute(int s){
				this.root=s;
			}
			public attribute(int s, attribute r,attribute l){
				this.root=s;
				this.left= l;
				this.right=r;
			}
			attribute copy() {
		        attribute left = null;
		        attribute right = null;
		        if (this.left !=  null) {
		            left = this.left.copy();
		        }
		        if (this.right !=  null) {
		            right = this.right.copy();
		        }
		        return new attribute(root, left, right);
		    }
		
		}
		
		
		
		public static ArrayList<ArrayList<Integer>> refreshData(ArrayList<ArrayList<Integer>> fileData, int index, int value){
			ArrayList<ArrayList<Integer>> newData = new ArrayList<ArrayList<Integer>>();
			
			for(ArrayList<Integer> x:fileData){
				if(x.get(index-100) == value){
					newData.add(x);
				}
			}
			
			return newData;
		}
		
		
		
		
		public static void replaceByLeaf(attribute q, ArrayList<ArrayList<Integer>> data, int step){
			if(count == 0){
				////System.out.println(q.root);
				//print(data);
				int w = 0;
				for(ArrayList<Integer> a: data){
					if(a.get(a.size()-1) == 0){
						w++;
					}
					else{
						w--;
					}
				}
				////System.out.println(w);
				if(w>0)
					q.root = 0;
				else
					q.root = 1;
				q.left = null;
				q.right = null;
				return;
			}
			else{
				ArrayList<ArrayList<Integer>> dataCopy = new ArrayList<ArrayList<Integer>>();
				if(q.left!= null&&q.left.root!= 0&&q.left.root!= 1&&count>0){
					count--;
					step++;
					////System.out.println("Step:" + step);
					////System.out.println(q.root+"left");
					dataCopy = refreshData(data, q.root, 0);
					replaceByLeaf(q.left, dataCopy,step);
				}
				
				if(q.right!= null&&q.right.root!= 0&&q.right.root!= 1&&count>0){
					count--;
					step++;
					////System.out.println("Step:" + step);
					////System.out.println(q.root+"right");
					dataCopy = refreshData(data, q.root, 1);
					replaceByLeaf(q.right, dataCopy,step);
				}
				else{
					return;
				}
			}
		}
		
		static ArrayList<ArrayList<Integer>> trainingData;
		
		
		
		//Function to implement Post Pruning algorithm
		public static attribute pruning(attribute root, int k, int l) throws IOException{
			trainingData = readFile(trainingDataSet);
			 attribute bestTree =  new attribute(0);
			bestTree = root.copy();
			for(int i = 1; i<= l; i++){
				attribute newRoot = root.copy();
				int m = (int)(Math.random() * (k-1) + 1);
				
				for(int j = 1; j<= m; j++){
					int n = countNonLeafattributes(newRoot);
					int p = (int)(Math.random() * (n-1) + 1);
					count = p;
					//count = 150;
					////System.out.println("count: "+count);
					replaceByLeaf(newRoot,trainingData,count);
				}
				//printTree1(newRoot,1);
				double prevAcc = treeAccuracy(root, validationDataSet);
				double newAcc = treeAccuracy(newRoot, validationDataSet);
				////System.out.println("p"+prevAcc+" n"+newAcc);
				if(newAcc>prevAcc){
					bestTree = newRoot;
					////System.out.println("hi");
				}
			}
			
			return bestTree;
		}
		
		//Function to Count Non-Leaf attributes
		public static int countNonLeafattributes(attribute t){
			if(t!=null && t.root!= 0 && t.root!= 1){
				return 1+ countNonLeafattributes(t.left)+countNonLeafattributes(t.right);
			}
			else
				return 0;
		}
		
		
		//Function to prune tree built using Information Gain
		public static attribute pruneIgTree(int k, int l, attribute a) throws IOException{
			attribute prunedIgTree = new attribute(0);
			prunedIgTree = pruning(a, k, l);
			return prunedIgTree;
		}
		
		
		static String[] aname = {"XB","XC","XD","XE","XF","XG","XH","XI","XJ","XK","XL","XM","XN","XO","XP","XQ","XR","XS","XT","XU", "","",""};
		
		/*
		 * Post Pruning Functions ends
		 */
		
		
}