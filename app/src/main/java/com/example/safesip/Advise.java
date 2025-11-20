package com.example.safesip;

import java.util.ArrayList;
import java.util.List;

public class Advise {


    private static List<String> advicesLow() {
        List<String> list = new ArrayList<>();
        list.add("⭐ WHAT YOU SHOULD KNOW:");
        list.add("You are still mostly sober but alcohol has started entering your bloodstream.");
        list.add("Effects can rise quickly depending on your body and what you ate.\n");

        list.add("🟢 WHAT YOU SHOULD DO:");
        list.add("Drink a glass of water to stay hydrated.");
        list.add("Eat something small to slow alcohol absorption.");
        list.add("Take your time between drinks.\n");

        list.add("❌ WHAT YOU SHOULD NOT DO:");
        list.add("Do not drink too fast.");
        list.add("Do not drink on an empty stomach.\n");
        return list;
    }

    private static List<String> advicesMedium() {
        List<String> list = new ArrayList<>();
        list.add("⭐ WHAT YOU SHOULD KNOW:");
        list.add("You may feel relaxed or more social.");
        list.add("Your reaction time is already slower.\n");

        list.add("🟢 WHAT YOU SHOULD DO:");
        list.add("Drink water regularly.");
        list.add("Eat something to help your body process alcohol.");
        list.add("Take a break from drinking for a while.\n");

        list.add("❌ WHAT YOU SHOULD NOT DO:");
        list.add("Do not drive — your reflexes are already impaired.");
        list.add("Do not mix alcohol with medications.");
        return list;
    }

    private static List<String> advicesHigh() {
        List<String> list = new ArrayList<>();
        list.add("⭐ WHAT YOU SHOULD KNOW:");
        list.add("You may feel euphoric, light-headed, or unsteady.");
        list.add("Coordination and judgment are significantly reduced.\n");

        list.add("🟢 WHAT YOU SHOULD DO:");
        list.add("Drink a big glass of water.");
        list.add("Stop drinking alcohol for now.");
        list.add("Sit down if you feel dizzy.");
        list.add("Eat something to stabilize yourself.\n");

        list.add("❌ WHAT YOU SHOULD NOT DO:");
        list.add("Do not drive under any circumstances.");
        list.add("Do not do physical activities — risk of injury is high.");
        list.add("Do not continue drinking quickly.");

        return list;
    }

    private static List<String> advicesSevere() {
        List<String> list = new ArrayList<>();
        list.add("⭐ WHAT YOU SHOULD KNOW:");
        list.add("You may feel nausea, blurry vision, dizziness, or confusion.");
        list.add("Your balance and judgment are heavily impaired. \n");

        list.add("🟢 WHAT YOU SHOULD DO:");
        list.add("Stop drinking immediately.");
        list.add("Drink water slowly.");
        list.add("Stay with someone you trust.");
        list.add("Sit down to avoid falls.");
        list.add("Rest in a calm and safe place.\n");

        list.add("❌ WHAT YOU SHOULD NOT DO:");
        list.add("Do not drive or operate anything.");
        list.add("Do not walk alone.");
        list.add("Do not mix alcohol with any drug or medication.");
        list.add("Do not make important decisions.");

        return list;
    }

    private static List<String> advicesCritical() {
        List<String> list = new ArrayList<>();
        list.add("⭐ WHAT YOU SHOULD KNOW:");
        list.add("This is a dangerous level: risk of alcohol poisoning.");
        list.add("You may experience confusion, vomiting, or trouble standing.\n");

        list.add("🟢 WHAT YOU SHOULD DO:");
        list.add("Stop drinking immediately.");
        list.add("Drink water slowly if you are conscious.");
        list.add("Stay with someone — do not be alone.");
        list.add("Sit or lie on your side to avoid choking if you vomit.");
        list.add("Seek medical help if you feel very unwell or faint.\n");

        list.add("❌ WHAT YOU SHOULD NOT DO:");
        list.add("Do not drive.");
        list.add("Do not walk alone.");
        list.add("Do not continue drinking.");
        list.add("Do not lie on your back if you vomit — choking risk.");
        list.add("Do not mix alcohol with medications or drugs.");

        return list;
    }



    public static String getAllAdvices(double amount) {

        List<String> selectedAdvices;

        if (amount < 0.3) selectedAdvices = advicesLow();
        else if (amount < 0.6) selectedAdvices = advicesMedium();
        else if (amount < 1.2) selectedAdvices = advicesHigh();
        else if (amount < 2.0) selectedAdvices = advicesSevere();
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