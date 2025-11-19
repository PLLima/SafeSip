package com.example.safesip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Conseil {

    private static final Random random = new Random();


    private static List<String> advicesLow() {
        List<String> list = new ArrayList<>();
        list.add("Drink slowly and enjoy your night 💛");
        list.add("Don’t forget to stay hydrated.");
        list.add("Take a short break between drinks.");
        list.add("Eat something small to help your body.");
        return list;
    }

    private static List<String> advicesMedium() {
        List<String> list = new ArrayList<>();
        list.add("Drink a full glass of water.");
        list.add("Slow down and take a small break.");
        list.add("Stay close to your friends.");
        list.add("Try switching to non-alcoholic drinks.");
        return list;
    }

    private static List<String> advicesHigh() {
        List<String> list = new ArrayList<>();
        list.add("You should stop drinking now.");
        list.add("Drink water slowly to avoid nausea.");
        list.add("Sit down and relax for a while.");
        list.add("Eat something soft like bread or crackers.");
        return list;
    }

    private static List<String> advicesSevere() {
        List<String> list = new ArrayList<>();
        list.add("Stop drinking immediately.");
        list.add("Drink small sips of water.");
        list.add("Stay seated and avoid walking.");
        list.add("Let someone stay with you; don’t be alone.");
        return list;
    }

    private static List<String> advicesCritical() {
        List<String> list = new ArrayList<>();
        list.add("DANGER");
        list.add("Stay seated and breathe slowly.");
        list.add("Call a sober friend to stay with you.");
        list.add("Drink water very slowly.");
        list.add("Seek medical help, this level is dangerous.");
        return list;
    }


    // ---------------------------
    //      WARNINGS PAR NIVEAU
    // ---------------------------

    private static String warning(double bac) {
        if (bac < 0.6) return "";
        if (bac < 1.2) return "Your reflexes are slowing down — avoid driving.";
        if (bac < 2.0) return "High risk of falls and impaired judgment.";
        if (bac < 3.0) return "Strong risk of alcohol poisoning — do not stay alone.";
        return "⚠️ CRITICAL: Risk of coma. Call someone immediately.";
    }


    // ---------------------------
    // RANDOM → pick inside a list
    // ---------------------------

    private static String pickRandom(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }


    // ---------------------------
    // PUBLIC METHOD → get message
    // ---------------------------

    public static String getAllAdvices(double bac) {

        List<String> selectedAdvices;

        if (bac < 0.3) selectedAdvices = advicesLow();
        else if (bac < 0.6) selectedAdvices = advicesMedium();
        else if (bac < 1.2) selectedAdvices = advicesHigh();
        else if (bac < 2.0) selectedAdvices = advicesSevere();
        else selectedAdvices = advicesCritical();

        return listToText(selectedAdvices);
    }

    private static String listToText(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append("• ").append(item).append("\n");
        }
        return sb.toString();
    }

}