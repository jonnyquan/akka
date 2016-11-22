package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
public class TestMain {

    public static void main(String[] args) {
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("a", Arrays.asList(1));
        A a = new A(map.get("a"));
        a.hello();
        map.put("a", Arrays.asList(1, 2));
        a.hello();
    }

    static class A {
        private List<Integer> list;

        public A(List<Integer> list) {
            this.list = list;
        }

        public void hello() {
            System.out.println(list.size());
        }
    }
}
