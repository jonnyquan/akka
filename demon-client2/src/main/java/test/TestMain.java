package test;


import akka.main.AkkaMain;

import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
public class TestMain {

    public static void main(String[] args) {
        long a = 122158496l;
        long total = 191143860;
        System.out.println(a*10/total);


       /* int count0 = 0;
        int count1 = 1;
        int count2 = 2;
        int count3 = 3;
        int count4 = 4;

        for(int i=0;i<10000;i++){
            int num = random();
            switch (num){
                case 0:
                    count0++;
                    break;
                case 1:
                    count1++;
                    break;
                case 2:
                    count2++;
                    break;
                case 3:
                    count3++;
                    break;
                case 4:
                    count4++;
                    break;
            }

        }
        System.out.println("0:"+count0+"个");
        System.out.println("1:"+count1+"个");
        System.out.println("2:"+count2+"个");
        System.out.println("3:"+count3+"个");
        System.out.println("4:"+count4+"个");*/
    }
    public static int random() {
        Random random = new Random();
        int size = 5;
        int[] scores = new int[]{1,1,1,1,4};
        int coreCount = 8;
        int[] randomScore = new int[size];

        int randomInt = random.nextInt(coreCount);
        for (int i = 0; i < size; i++) {
            int s;
            int last = 0;
            if (i > 0) {
                last = randomScore[i - 1];
            }
            s = scores[i] + last;

            if (randomInt < s && randomInt >= last) {
                return i;
            }
            randomScore[i] = s;
        }
        return 100;
    }


}
