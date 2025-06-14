import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    static String player1 = "Гравець1";
    static String player2 = "Гравець2";
    static int size = 3;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadConfig();
        while (true) {
            System.out.println("1.Грати 2.Налаштування 3.Статистика 4.Вихід");
            String ch = sc.nextLine();
            if (ch.equals("1")) play();
            else if (ch.equals("2")) settings();
            else if (ch.equals("3")) stats();
            else if (ch.equals("4")) break;
        }
    }

    static void loadConfig() {
        try {
            BufferedReader r = new BufferedReader(new FileReader("config.txt"));
            player1 = r.readLine().split("=")[1];
            player2 = r.readLine().split("=")[1];
            size = Integer.parseInt(r.readLine().split("=")[1]);
            r.close();
        } catch (Exception e) {
            saveConfig();
        }
    }

    static void saveConfig() {
        try {
            PrintWriter w = new PrintWriter("config.txt");
            w.println("p1=" + player1);
            w.println("p2=" + player2);
            w.println("size=" + size);
            w.close();
        } catch (Exception e) {}
    }

    static void settings() {
        System.out.print("Ім'я 1: ");
        player1 = sc.nextLine();
        System.out.print("Ім'я 2: ");
        player2 = sc.nextLine();
        System.out.print("Розмір (3-9): ");
        try {
            int s = Integer.parseInt(sc.nextLine());
            if (s >= 3 && s <= 9) size = s;
        } catch (Exception e) {}
        saveConfig();
    }

    static void play() {
        char[][] b = new char[size][size];
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) b[i][j] = ' ';
        boolean turn = true;
        int m = 0;
        while (true) {
            print(b);
            System.out.println("Хід: " + (turn ? player1 : player2));
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            sc.nextLine();
            if (x < 0 || y < 0 || x >= size || y >= size || b[x][y] != ' ') continue;
            b[x][y] = turn ? 'X' : 'O';
            m++;
            if (win(b, turn ? 'X' : 'O')) {
                print(b);
                System.out.println("Переможець: " + (turn ? player1 : player2));
                saveStat(turn ? player1 : player2, b);
                break;
            }
            if (m == size * size) {
                print(b);
                System.out.println("Нічия");
                saveStat("Нічия", b);
                break;
            }
            turn = !turn;
        }
    }

    static void print(char[][] b) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(b[i][j]);
                if (j < size - 1) System.out.print("|");
            }
            System.out.println();
        }
    }

    static boolean win(char[][] b, char s) {
        for (int i = 0; i < size; i++) {
            boolean r = true, c = true;
            for (int j = 0; j < size; j++) {
                if (b[i][j] != s) r = false;
                if (b[j][i] != s) c = false;
            }
            if (r || c) return true;
        }
        boolean d1 = true, d2 = true;
        for (int i = 0; i < size; i++) {
            if (b[i][i] != s) d1 = false;
            if (b[i][size - i - 1] != s) d2 = false;
        }
        return d1 || d2;
    }

    static void saveStat(String winner, char[][] b) {
        try {
            FileWriter f = new FileWriter("stats.txt", true);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            f.write("Дата: " + now.format(fmt) + ", Переміг: " + winner + ", Розмір: " + size + "\n");
            f.close();
        } catch (Exception e) {}
    }

    static void stats() {
        try {
            BufferedReader r = new BufferedReader(new FileReader("stats.txt"));
            String l;
            while ((l = r.readLine()) != null) System.out.println(l);
            r.close();
        } catch (Exception e) {
            System.out.println("Немає записів.");
        }
    }
}
