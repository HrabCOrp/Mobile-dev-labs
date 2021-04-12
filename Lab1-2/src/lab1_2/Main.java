package lab1_2;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Частина 1

        // Дано рядок у форматі "Student1 - Group1; Student2 - Group2; ..."

        String studentsStr = "Дмитренко Олександр - ІП-84; Матвійчук Андрій - ІВ-83; Лесик Сергій - ІО-82; Ткаченко Ярослав - ІВ-83; Аверкова Анастасія - ІО-83; Соловйов Даніїл - ІО-83; Рахуба Вероніка - ІО-81; Кочерук Давид - ІВ-83; Лихацька Юлія - ІВ-82; Головенець Руслан - ІВ-83; Ющенко Андрій - ІО-82; Мінченко Володимир - ІП-83; Мартинюк Назар - ІО-82; Базова Лідія - ІВ-81; Снігурець Олег - ІВ-81; Роман Олександр - ІО-82; Дудка Максим - ІО-81; Кулініч Віталій - ІВ-81; Жуков Михайло - ІП-83; Грабко Михайло - ІВ-81; Іванов Володимир - ІО-81; Востриков Нікіта - ІО-82; Бондаренко Максим - ІВ-83; Скрипченко Володимир - ІВ-82; Кобук Назар - ІО-81; Дровнін Павло - ІВ-83; Тарасенко Юлія - ІО-82; Дрозд Світлана - ІВ-81; Фещенко Кирил - ІО-82; Крамар Віктор - ІО-83; Іванов Дмитро - ІВ-82";

        // Завдання 1
        // Заповніть словник, де:
        // - ключ – назва групи
        // - значення – відсортований масив студентів, які відносяться до відповідної групи

        HashMap<String, ArrayList<String>> studentsGroups = new HashMap<>();
        String[] students = studentsStr.split("[( - );]");

        for (int i=0; i<students.length; i+=5){
            if (studentsGroups.containsKey(students[i+3]))
                studentsGroups.get(students[i+3]).add(students[i]+" "+students[i+1]);
            else {
                ArrayList<String> stud = new ArrayList<String>();
                stud.add(students[i]+" "+students[i+1]);
                studentsGroups.put(students[i+3], stud);
            }
        }
        for (String group : studentsGroups.keySet())
            Collections.sort(studentsGroups.get(group));

        System.out.println("\nЗавдання 1");
        System.out.println(studentsGroups);
        System.out.println();


    // Завдання 2
    // Заповніть словник, де:
    // - ключ – назва групи
    // - значення – словник, де:
    //   - ключ – студент, який відносяться до відповідної групи
    //   - значення – масив з оцінками студента (заповніть масив випадковими значеннями, використовуючи функцію `randomValue(maxValue: Int) -> Int`)

    // Дано масив з максимально можливими оцінками
    int[] points = {12, 12, 12, 12, 12, 12, 12, 16};
    HashMap<String, HashMap<String, ArrayList<Integer>>> studentPoints = new HashMap<>();

    for (String group: studentsGroups.keySet()){
        if (!studentPoints.containsKey(group)){
            HashMap<String,ArrayList<Integer>> temp = new HashMap<>();
            studentPoints.put(group, temp);
        }
        for (String student : studentsGroups.get(group)) {
            ArrayList<Integer> temp = new ArrayList<>();
            studentPoints.get(group).put(student, temp);

            ArrayList<Integer> s = studentPoints.get(group).get(student);
            for (int point : points)
                s.add(randomValue(point));
        }
    }
    System.out.println("Завдання 2");
    System.out.println(studentPoints);
    System.out.println();


    // Завдання 3
    // Заповніть словник, де:
    // - ключ – назва групи
    // - значення – словник, де:
    //   - ключ – студент, який відносяться до відповідної групи
    //   - значення – сума оцінок студента

    HashMap<String, HashMap<String, Integer>> sumPoints = new HashMap<>();

    for (String group: studentPoints.keySet()){

        if (!sumPoints.containsKey(group)){
            HashMap<String, Integer> temp = new HashMap<>();
            sumPoints.put(group, temp);
        }
        for (Map.Entry<String, ArrayList<Integer>> student : studentPoints.get(group).entrySet()) {
            int sum = 0;
            for (int point : studentPoints.get(group).get(student.getKey()))
                sum += point;
            sumPoints.get(group).put(student.getKey(), sum);
        }

    }
    System.out.println("Завдання 3");
    System.out.println(sumPoints);
    System.out.println();


    // Завдання 4
    // Заповніть словник, де:
    // - ключ – назва групи
    // - значення – середня оцінка всіх студентів групи

    HashMap<String, Float> groupAvg = new HashMap<>();

    for (String group: sumPoints.keySet()){
        Set<String> students2 = sumPoints.get(group).keySet();
        int sum = 0, n = students2.size();

        for (String student : students2) {
            sum += sumPoints.get(group).get(student);
        }
        groupAvg.put(group, (float)sum/n);
    }

    System.out.println("Завдання 4");
    System.out.println(groupAvg);
    System.out.println();

    // Завдання 5
    // Заповніть словник, де:
    // - ключ – назва групи
    // - значення – масив студентів, які мають >= 60 балів

    HashMap<String, ArrayList<String>> passedPerGroup = new HashMap<>();


    for (String group : studentsGroups.keySet()){

        if (!passedPerGroup.containsKey(group)){
            ArrayList<String> temp = new ArrayList<>();
            passedPerGroup.put(group, temp);
        }
        for (String student : studentsGroups.get(group)) {
            if (sumPoints.get(group).get(student) >= 60){
                passedPerGroup.get(group).add(student);
            }
        }
    }

    System.out.println("Завдання 5");
    System.out.println(passedPerGroup);
    System.out.println();

}

    private static int randomValue(int maxValue){
        Random rand = new Random();
        switch(rand.nextInt(6)) {
            case 1:
                return (int) (maxValue * 0.7);
            case 2:
                return (int) (maxValue * 0.9);
            case 3:
            case 4:
            case 5:
                return maxValue;
            default:
                return 0;
        }
    }
}
