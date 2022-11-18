package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        ArrayList<Citizen> people = new ArrayList<>();

        people.add(new Citizen("Illya", "Pashchenko", CitizenType.VOTER, true));
        people.add(new Citizen("Vasyl", "Pupkin", CitizenType.VOTER, true));
        people.add(new Citizen("Grigoriy", "Vasilchenko", CitizenType.VOTER, true));
        people.add(new Citizen("Fedor", "Soloviy", CitizenType.VOTER, true));
        people.add(new Citizen("Anastasia", "Vulchanska", CitizenType.VOTER, true));
        people.add(new Citizen("Margarita", "Vilna", CitizenType.VOTER, true));
        people.add(new Citizen("Denis", "Vilnyj", CitizenType.VOTER, false));

        people.add(new Citizen("Lev", "Petrechenko", CitizenType.CANDIDATE, true));
        people.add(new Citizen("Slava", "Ukrainko", CitizenType.CANDIDATE, true));
        people.add(new Citizen("Yuriy", "Sokolenko", CitizenType.CANDIDATE, true));

        VK vk = new VK();

        vk.selectAndAddCandidates(people);
        BR.sendRegistrationListToVK(vk);

        Set<UUID> candidatesId = vk.getCandidates().keySet();

        for (Citizen citizen : people) {
            citizen.setVk(vk);
        }

        for (Citizen citizen : people) {
            citizen.vote(randomCandidate(candidatesId));
        }

//        people.get(2).vote(randomCandidate(candidatesId));

        printStandings(vk, people);
    }

    public static void printStandings(VK vk, ArrayList<Citizen> people){
        for (UUID id : vk.candidates.keySet()) {
            for (Citizen citizen : people) {
                if (citizen.getId() == id) System.out.println(citizen.getFirsName() + " " + citizen.getSurname() + ": " + vk.candidates.get(id));
            }
        }
    }

    public static UUID randomCandidate(Set<UUID> set){
        int size = set.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(UUID id : set)
        {
            if (i == item)
                return id;
            i++;
        }
        return null;
    }
}
