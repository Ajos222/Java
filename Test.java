import java.util.Scanner;
import java.util.Random;


public class Test {
  static Scanner scanner = new Scanner(System.in);
  static Random random=new Random();
  static int vec;
  static int cislo= random.nextInt(11);
  

    public static void main(String[] args) {
        System.out.print("cislo: ");
        int vec = scanner.nextInt();
        
        if(vec==cislo){
            System.out.println("vyhral si");
        }
        else{
          System.out.println("trhi si");
        }
        scanner.close();
}
}