package ru.database.prog;

import ru.database.repository.CityRepositoryImpl;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static void printMenu() {
        System.out.println("\nВыберите требуемое действие и введите соответствующую цифру:\n" +
                "1) Список городов\n" +
                "2) Отсортированный список городов по наименованию в алфавитном порядке\n" +
                "3) Отсортированный список городов по федеральному округу и наименованию\n" +
                "4) Поиск города с наибольшим количеством жителей\n" +
                "5) Поиск количества городов в разрезе регионов\n" +
                "6) Выход\n");
    }

    public static void main(String[] args) {
        CityRepositoryImpl service = new CityRepositoryImpl();
        service.deleteTable();
        service.insertDataFromFile();
        printMenu();

        Scanner scanner = new Scanner(System.in);
        do {
            try {
                int action = scanner.nextInt();

                switch (action) {
                    case 1:
                        service.selectAllData().forEach(System.out::println);
                        printMenu();
                        break;
                    case 2:
                        service.sortByName().stream().sorted(Comparator.comparing(x -> x.getName().toLowerCase()))
                                .collect(Collectors.toList()).forEach(System.out::println);
                        printMenu();
                        break;
                    case 3:
                        service.sortByDistrictAndName().forEach(System.out::println);
                        printMenu();
                        break;
                    case 4:
                        Object[] obj = service.getMaxPopulation();
                        System.out.printf("[%d] = %d (%s)\n", obj[0], obj[1], obj[2]);
                        printMenu();
                        break;
                    case 5:
                        service.numberOfCitiesInRegion().forEach((x) -> System.out.printf("%s - %d\n", x.getRegion(), x.getCount()));
                        printMenu();
                        break;
                    case 6:
                        scanner.close();
                        System.out.println("Вы вышли из меню ");
                        return;
                    default:
                        System.out.println("Нужно вводить цифру от 1 до 6");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Нужно вводить цифру от 1 до 6. Попробуйте заново");
                break;
            }
        } while (scanner.hasNext());
    }
}