
import java.io.*;   
import java.lang.Math;   
import java.io.InputStream;   
import java.util.*;   
   
public class SPIHT   
{   
   public static int NumBWidth;    //The number of blocks in width   
   public static int NumBHigh;     //The number of blocks in Hight   
   public static int NumBelement;  // The number of elements in a block   
      
   public static int Imagesize = 256;   //Image size   
   public static int CodeBookSize = 256;//Codebook size   
   public static int [][] OriginImage ; //host image matrix   
   public static int [][] DeImage;      //decoded image   
   public static int [][] IdxTable;     //index table   
   public static int size = 8;          //block size   
   public static String ls_filename="Lena";   
   public static String tmp_Str="";   
   public static String finally_encoded="";   
   public static int N;   
   public static int T0,R,R1,R2,R11,R12,R21,R22;   
       
   public static void main (String[] argv) throws IOException   
   {   
    //input parameters   
	BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter the name of the Image File");
	ls_filename = obj.readLine(); 
    System.out.println("Enter Image Size");
    Imagesize = Integer.parseInt(obj.readLine());
    System.out.println("Enter block size");
    size = Integer.parseInt(obj.readLine());  
    
       
    OriginImage = new int [Imagesize][Imagesize];   
    
    DeImage = new int [Imagesize][Imagesize];   
    int [][] tmp = new int [size][size];   
    int [][] tmp1 = new int [size/2][size/2];   
    int [][] tmp2 = new int [size/4][size/4];   
    IdxTable = new int [Imagesize][Imagesize];   
       
    //read images   
    ReadImage ("DWT_result"+ls_filename+".raw", Imagesize);   
    int incTemp = Imagesize/size;    
    int p=0,q=0;   
       
    //block division & SPIHT transformation   
    for (int i=0;i<incTemp;i++)   
    {   
       System.out.println("i:"+i);   
       for (int j=0;j<incTemp;j++)   
       {   
        //max coefficient   
          N = (int) (Math.log(DeImage[i*size][j*size])/Math.log(2));   
          //threshold   
          T0 = (int) (Math.pow(2,N));   
             
        //SPIHT for each block   
        for (int m=0;m<size*size;m++)         
                  tmp[m/size][m%size] = DeImage[i*size+((int)m/size)][j*size+((int)m%size)];   
           
        R = (int)((DeImage[i*size][j*size]+T0+0.5)/2);   
          R1 = (int)((DeImage[i*size][j*size]+R+0.5)/2);   
          R2 = (int)((R+T0+0.5)/2);   
                   
        //1st SPIHT_transform transformation for each block       
        finally_encoded+=SPIHT_transform(tmp);   
           
         
        R11 = (int)((DeImage[i*size][j*size]+R1+0.5)/2);   
        R12 = (int)((R1+R+0.5)/2);   
        R21 = (int)((R2+R+0.5)/2);   
        R22 = (int)((R2+T0+0.5)/2);   
           
        //
        for (int m=0;m<size*size;m++)   
        {   
            int tempvalue = DeImage[i*size+((int)m/size)][j*size+((int)m%size)];   
            //
            if (Math.abs(tempvalue)>=R)   
            {   
                if (tempvalue >=0)   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = R1;   
                else    
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = -R1;   
                finally_encoded +="1";   
            }   
            else if (((Math.abs(tempvalue)<R)) && ((Math.abs(tempvalue)>=T0)))   
            {   
                if (tempvalue >=0)   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = R2;   
                else    
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = -R2;        
                finally_encoded +="0";   
          }   
             
             
    
          if (Math.abs(tempvalue)>=R)   
            {   
              if (Math.abs(tempvalue)>=R1)   
              {     
                if (tempvalue >=0)   
                {   
                    OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = R11;   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = tempvalue - R11;   
                }   
                else    
                {   
                    OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = -R11;   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = -(Math.abs(tempvalue) - R11);   
                }   
                finally_encoded +="1";   
              }   
              else   
              {   
                if (tempvalue >=0)   
                {   
                    OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = R12;   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = tempvalue - R12;   
                }   
                else    
                {   
                    OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = -R12;   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = -(Math.abs(tempvalue) - R12);   
                }   
                finally_encoded +="0";   
              }   
                   
            }   
            else if (((Math.abs(tempvalue)<R)) && ((Math.abs(tempvalue)>=T0)))   
            {   
              if (Math.abs(tempvalue)>=R2)   
              {     
                if (tempvalue >=0)   
                {   
                    OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = R21;   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = tempvalue - R21;   
                }   
                else    
                {   
                    OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = -R21;   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = -(Math.abs(tempvalue) - R21);   
                }   
                finally_encoded +="1";   
              }   
              else   
              {   
                if (tempvalue >=0)   
                {   
                    OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = R12;   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = tempvalue - R12;   
                }   
                else    
                {   
                    OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = -R12;   
                    DeImage[i*size+((int)m/size)][j*size+((int)m%size)] = -(Math.abs(tempvalue) - R12);   
                }   
                finally_encoded +="0";   
              }   
          }   
          else   
          {   
            OriginImage[i*size+((int)m/size)][j*size+((int)m%size)] = 0;   
          }   
             
        }//for m   
           
        //2nd    
        //N=N/2;   
        //T0 = T0/2;   
        //SPIHT for each block   
        //for (int m=0;m<size*size;m++)       
              //    tmp[m/size][m%size] = DeImage[i*size+((int)m/size)][j*size+((int)m%size)];   
        //finally_encoded+=SPIHT_transform(tmp);   
           
        //copy to DeImage   
        //for (int m=0;m<size*size;m++)           
              //    IdxTable[i*size+((int)m/size)][j*size+((int)m%size)] = tmpR [m/size][m%size];     
                   
       }   
    }   
      
  //write out the haar SPIHT results   
    DataOutputStream out=new DataOutputStream(new FileOutputStream("decoded-"+ls_filename+".raw"));   
    for(int i=0;i<Imagesize;i++)   
      for(int j=0;j<Imagesize;j++)   
         out.writeByte(OriginImage[i][j]);    
    out.close();   
       
    System.out.println("Successfully finish SPIHT compression!");   
    System.out.println("The results are shown in file:"+"decoded-"+ls_filename+".raw");   
    
 }   
    
 //SPIHT   
 public static String SPIHT_transform(int [][]tmp)   
 {   
         
      tmp_Str="";   
      //tmperary value to determine its children's values   
      int ok = 0;   
      String qq = "";   
         
      for(int i=0;i<2;i++)   
      {   
         for (int j=0;j<2;j++)   
         {    
                if ((tmp[i][j] > 0) && (tmp[i][j] > T0))    
                {   
                    tmp_Str += "10";   
                    ok = 10;   
                }   
                else if ((tmp[i][j] < 0) && (Math.abs(tmp[i][j]) > T0))    
                {   
                  tmp_Str += "11";   
                  ok = 11;   
                  //IdxTable[i][j] = 1;   
                }   
                else    
                {   
                  tmp_Str += "0";   
                  ok = 0;   
                  //IdxTable[i][j] = 0;   
                }   
                   
                if (ok == 0)   
                {   
                   qq = checkChild(tmp, i,j,N,T0,1); //第一回合   
                   tmp_Str +=qq;   
                }      
                   
                if ((i!=0) && (j!=0) && (ok>0))    
                {   
                    child(tmp, i,j,N,T0,1);   
                }   
                else if ((i!=0) && (j!=0) && (ok==0) && (qq.equals("1"))) //雖然自己本身為0，但是其子孫有>T0的   
                {   
                    child(tmp, i,j,N,T0,1);   
                }   
                
         }//end for j   
      }//end for i    
      return tmp_Str;   
         
 }   
    
 
 public static String checkChild(int [][]tmp, int i, int j, int N, int T, int counter)   
 {   
        int testtest = 0;   
        String test = "";   
           
        //first & child checking    
        //check if they are greater than T    
        if ((Math.abs(tmp[2*i][j*2]) < T) && (Math.abs(tmp[2*i+1][j*2]) < T)    
            && (Math.abs(tmp[2*i][j*2+1]) < T) && (Math.abs(tmp[2*i+1][j*2+1]) < T))   
            {   
                if (counter ==1)   
                {   
                    i = 2*i;   
                    j = 2*j;   
                    for (int r=0;r<4;r++)   
                    {   
                       for (int s=0;s<4;s++)   
                       {   
                         if (Math.abs(tmp[2*i+r][j*2+s]) > T)   
                         {   
                            testtest ++;   
                            //IdxTable[2*i+r][j*2+s] = 1;   
                         }   
                         //else IdxTable[2*i+r][j*2+s] = 0;   
                       }   
                    }   
                       
                if ((testtest ==0) || (counter == 2))   
                   test = "0";   
                else test = "1";      
                }//end if counter ==1   
            }   
            else test = "1";   
       
          return test;   
 }   
    
    
 
 public static void child(int [][]tmp, int i, int j, int N, int T, int counter)   
 {   
        int ok = 0;   
        String qq = "";   
           
        if ((tmp[2*i][j*2] > 0) && (tmp[2*i][j*2] > T))   
        {   
           tmp_Str += "10";   
           ok = 10;   
        }   
        else if ((tmp[2*i][j*2] < 0) && (Math.abs(tmp[2*i][j*2]) > T))    
        {   
             tmp_Str += "11";   
             ok = 11;   
        }   
        else    
        {   
          ok = 0;   
          tmp_Str += "0";   
        }   
           
        if ((ok == 0) && (counter == 1))   
        {   
                   qq = checkChild(tmp, i,j,N,T,2); //第一回合   
                   tmp_Str+=qq;   
        }   
           
        if ((((counter == 1) && (ok >0)) || ((counter == 1) && qq.equals("1"))))   
            child(tmp, 2*i, j*2, N, T,2);   
           
        if ((tmp[2*i][j*2+1] > 0) && (tmp[2*i][j*2+1] > T))   
        {   
           tmp_Str += "10";   
           ok = 10;   
        }   
        else if ((tmp[2*i][j*2+1] < 0) && (Math.abs(tmp[2*i][j*2+1]) > T))    
        {   
             tmp_Str += "11";   
             ok = 11;   
        }   
        else    
        {   
          ok = 0;   
          tmp_Str += "0";   
        }   
           
           
        if ((ok == 0) && (counter == 1))   
        {   
                   qq = checkChild(tmp, i,j,N,T,2); //第一回合   
                   tmp_Str+=qq;   
        }   
           
        if ((((counter == 1) && (ok >0)) || ((counter == 1) && qq.equals("1"))))   
            child(tmp, 2*i, j*2+1, N, T,2);   
           
           
           
        if ((tmp[2*i+1][j*2] > 0) && (tmp[2*i+1][j*2] > T))   
        {   
           tmp_Str += "10";   
           ok = 10;   
        }   
        else if ((tmp[2*i+1][j*2] < 0) && (Math.abs(tmp[2*i+1][j*2]) > T))    
        {   
             tmp_Str += "11";   
             ok = 11;   
        }   
        else    
        {   
            tmp_Str += "0";   
            ok = 0;   
        }   
           
           
        if ((ok == 0) && (counter == 1))   
        {   
                   qq = checkChild(tmp, i,j,N,T,2); //第一回合   
                   tmp_Str+=qq;   
        }   
           
        if ((((counter == 1) && (ok >0)) || ((counter == 1) && qq.equals("1"))))   
            child(tmp, 2*i+1, j*2, N, T,2);   
           
           
           
        if ((tmp[2*i+1][j*2+1] > 0) && (tmp[2*i+1][j*2+1] > T))   
        {   
           tmp_Str += "10";   
           ok = 10;   
        }   
        else if ((tmp[2*i+1][j*2+1] < 0) && (Math.abs(tmp[2*i+1][j*2+1]) > T))    
        {   
             tmp_Str += "11";   
             ok = 11;   
        }   
        else    
        {   
            tmp_Str += "0";   
            ok = 0;   
        }   
           
        if ((ok == 0) && (counter == 1))   
        {   
                   qq = checkChild(tmp, i,j,N,T,2);  
                   tmp_Str+=qq;   
        }   
           
        if ((((counter == 1) && (ok >0)) || ((counter == 1) && qq.equals("1"))))   
            child(tmp, 2*i+1, j*2+1, N, T,2);   
           
                   
 }   
    
  //read Image   
  public static void ReadImage (String File_name, int imgSize)    throws IOException   
  {   
    DataInputStream in=new DataInputStream(new FileInputStream("C:\\Users\\ABC\\workspace\\black-dot.png"));   
    for(int i=0;i<imgSize;i++)   
        for(int j=0;j<imgSize;j++)   
           DeImage[i][j]= (int) in.readUnsignedByte();    
           //OriginImage[i][j]= (int) in.readUnsignedByte();    
    in.close();   
  }   
    
}
