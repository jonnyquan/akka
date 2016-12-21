package test;


import akka.main.AkkaMain;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
public class TestMain {

    public static void main(String[] args) {
       /* try {
            Class clazz = Class.forName("test.TestActor");
            System.out.println(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        AkkaMain.initAkka();
    }


}
