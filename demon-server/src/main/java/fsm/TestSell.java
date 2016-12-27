package fsm;

import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 2016/12/26.
 */
public class TestSell {

    public static void main(String[] args) {

    }

    public static void count(float condition, float cut, List<Food> foods) {
        int size = foods.size();
        for (int i = 0; i < size; i++) {
            Food tail = foods.get(size - i);
            if (tail.getPrice() > condition) {
                print(new Food[]{tail}, cut);
                continue;
            }
            for (int j = 0; j < i; j++) {
                Food head = foods.get(j);
                if (tail.getPrice() + head.getPrice() > condition) {
                    print(new Food[]{head, tail}, cut);
                    break;
                }
            }
        }
    }

    public static void print(Food[] food, float cut) {
        String fstr = "";
        float total = 0;
        for (Food f : food) {
            fstr += f.getName() + "|" + f.getPrice() + " ";
            total += f.getPrice();
        }
        System.out.println("组合: " + fstr + "  总价:" + (total - cut));
    }

    static class Food {
        private String name;
        private float price;
        private Boolean repeat;

        public Food(String name, float price, Boolean repeat) {
            this.name = name;
            this.price = price;
            this.repeat = repeat;
        }

        public String getName() {
            return name;
        }

        public float getPrice() {
            return price;
        }

        public Boolean getRepeat() {
            return repeat;
        }
    }
}
