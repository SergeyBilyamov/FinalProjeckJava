import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PhoneBook {
    private static Map<String, Set<String>> phoneBook = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final String FILE_NAME = "phonebook.ser";

    public static void main(String[] args) {
        loadPhoneBook();

        boolean running = true;
        while (running) {
            hr();
            System.out.println("Телефонная книга:\n");
            System.out.println("1. Просмотреть записи");
            System.out.println("2. Добавить запись");
            System.out.println("3. Удалить запись");
            System.out.println("4. Редактировать запись");
            System.out.println("5. Выйти");
            System.out.print("\nВыберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            clearConsole();

            switch (choice) {
                case 1:
                    displayItems();
                    break;
                case 2:
                    addItem();
                    break;
                case 3:
                    deleteItem();
                    break;
                case 4:
                    editItem();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("\nНекорректный выбор! Попробуйте снова.\n");
            }
        }
        savePhoneBook();
        System.out.println("\nРабота приложения завершена.\n");
    }

    private static void displayItems() {
        if (phoneBook.isEmpty()) {
            System.out.println("\nТелефонная книга пуста.\n");
        } else {
            List<Map.Entry<String, Set<String>>> sortedEntries = new ArrayList<>(phoneBook.entrySet());
            sortedEntries.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));
            hr();
            System.out.println("Записи в телефонной книге (отсортированы по убыванию числа телефонов):\n");
            for (Map.Entry<String, Set<String>> entry : sortedEntries) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        waitEnter();
    }

    private static void addItem() {
        hr();
        System.out.print("ДОБАВЛЕНИЕ ЗАПИСИ\n\n");
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите номер телефона: ");
        String phoneNumber = scanner.nextLine();
        phoneBook.computeIfAbsent(name, k -> new HashSet<>()).add(phoneNumber);
        System.out.println("\nЗапись добавлена: " + name + ": " + phoneNumber);
        waitEnter();
    }

    private static void deleteItem() {
        hr();
        System.out.print("УДАЛЕНИЕ ЗАПИСИ\n\n");
        System.out.print("Введите имя для удаления: ");
        String name = scanner.nextLine();
        if (phoneBook.containsKey(name)) {
            Set<String> phoneNumbers = phoneBook.remove(name);
            System.out.println("Запись удалена: " + name + ": " + phoneNumbers);
        } else {
            System.out.println("Запись с таким именем не найдена.");
        }
        waitEnter();
    }

    private static void editItem() {
        hr();
        System.out.print("РЕДАКТИРОВАНИЕ ЗАПИСИ\n\n");
        System.out.print("Введите имя для редактирования: ");
        String name = scanner.nextLine();
        if (phoneBook.containsKey(name)) {
            System.out.print("Введите новый номер телефона: ");
            String newPhoneNumber = scanner.nextLine();
            phoneBook.get(name).add(newPhoneNumber);
            System.out.println("Запись отредактирована: " + name + ": " + phoneBook.get(name));
        } else {
            System.out.println("Запись с таким именем не найдена.");
        }
        waitEnter();
    }

    private static void waitEnter() {
        System.out.print("\nНажмите ENTER для продолжения...");
        scanner.nextLine();
        clearConsole();
    }

    private static void hr() {
        System.out.println("\n--------------------------------");
    }

    public static void clearConsole() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

    private static void savePhoneBook() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {
            oos.writeObject(phoneBook);
            System.out.println("Телефонная книга сохранена в файл: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении телефонной книги: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadPhoneBook() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_NAME))) {
            phoneBook = (Map<String, Set<String>>) ois.readObject();
            System.out.println("Телефонная книга загружена из файла: " + FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке телефонной книги: " + e.getMessage());
        }
    }
}