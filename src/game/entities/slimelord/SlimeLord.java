package game.entities.slimelord;

import game.entities.slimefactory.SlimeFactory;

import java.util.LinkedList;

public class SlimeLord {
    int id;
    String name;
    int totalMovement;
    int remainingMovement;
    LinkedList<SlimeLordAbility> abilities;
    LinkedList<SlimeFactory> factories;
}
