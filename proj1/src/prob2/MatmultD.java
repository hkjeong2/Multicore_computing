package prob2;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MatmultD
{
    public static void main(String [] args)
    {
        int thread_no = Integer.valueOf(args[0]);
        AtomicInteger atomic_sum = new AtomicInteger(0); // sum of all elements in the resulting matrix
        ArrayList<Thread> mThread = new ArrayList<>();
        ArrayList<int[][]> mapList = readFile(args[1]); //get two matrix from the input file
        int[][] a = mapList.get(0);
        int[][] b = mapList.get(1);
        int[][] c = new int[a.length][b[0].length];

        System.out.println("The Program is starting . . .\n");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < thread_no; i++){
            mThread.add(new MyThread(atomic_sum, a, b, c, i, thread_no));
        }
        for (int i = 0; i < thread_no; i++){
            mThread.get(i).start();
        }
        for (int i = 0; i < thread_no; i++){
            try{
                mThread.get(i).join();
            }catch (Exception e){
                System.out.println("error");
            }
        }

        long endTime = System.currentTimeMillis();

//        printMatrix(a);
//        printMatrix(b);
//        printMatrix(c);

        System.out.println("\nProgram Execution Time: " + (endTime-startTime) + "ms" + "\n");
        System.out.println("Matrix Sum = " + atomic_sum);
    }

    static class MyThread extends Thread{
        AtomicInteger atomic_sum;
        int order;
        int pos;    //the position of matrix c to store the calculation result
        int [][] a;
        int [][] b;
        int [][] c;
        int thread_no;

        public MyThread(AtomicInteger atomic_sum, int[][] a, int[][] b, int[][] c, int order, int thread_no){
            this.atomic_sum = atomic_sum;
            pos = order;
            this.order = order;
            this.a = a;
            this.b = b;
            this.c = c;
            this.thread_no = thread_no;
        }
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            while (pos < a.length * b[0].length){
                // calculate the row and col based on the value 'pos'
                int row = pos / b[0].length;
                int col = pos % b[0].length;

                // update c[row][col] and atomic_sum
                for (int i = 0; i < a[0].length; i++)
                    c[row][col] += a[row][i] * b[i][col];
                atomic_sum.addAndGet(c[row][col]);

                pos += thread_no;   // update the position
            }

            long endTime = System.currentTimeMillis();

            System.out.printf("[thread_no]:%2d , [Time]:%4d ms\n", order, endTime-startTime);
        }
    }

    public static void printMatrix(int[][] mat) {
        System.out.println("\nMatrix["+mat.length+"]["+mat[0].length+"]");
        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%4d " , mat[i][j]);
                sum+=mat[i][j];
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum);
    }

    public static ArrayList<int[][]> readFile(String file){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            int isFirst = 1;
            int idx = 0;
            int row = 0;
            int col;
            int[][] a = null;
            int[][] b = null;
            ArrayList<int[][]> mapList = new ArrayList<>();
            while ((s = br.readLine()) != null){
                String[] token = s.split(" ");
                if (idx == 0){
                    row = Integer.parseInt(token[0]);
                    col = Integer.parseInt(token[1]);
                    if (isFirst == 1){
                        a = new int[row][col];
                    }
                    idx += 1;
                    continue;
                }
                else if (idx == row+1){
                    row = Integer.parseInt(token[0]);
                    col = Integer.parseInt(token[1]);
                    isFirst = 0;
                    b = new int[row][col];
                    idx = 1;
                    continue;
                }
                if (isFirst == 1){
                    for (int i = 0; i < token.length; i++){
                        a[idx-1][i] = Integer.parseInt(token[i]);
                    }
                }
                else{
                    for (int i = 0; i < token.length; i++){
                        b[idx-1][i] = Integer.parseInt(token[i]);
                    }
                }
                idx += 1;
            }
            mapList.add(a);
            mapList.add(b);
            br.close();
            return mapList;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}