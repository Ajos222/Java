import java.util.Scanner;
import java.util.Random;

public class Experimenty {

  static Scanner scanner = new Scanner(System.in);
  static Random random = new Random();
  static int num;

  static void pokus() {
    int vec = scanner.nextInt();
    if (vec == num) {
      System.out.println("Ano!");
    } else {
      System.out.println("Nie!");
    }
  }

  public static void main(String[] args) {
    num = random.nextInt(11); 
    System.out.print("Uhádni číslo (0-10): ");
    pokus();
    pokus();
    pokus();
    scanner.close();
  }
}
