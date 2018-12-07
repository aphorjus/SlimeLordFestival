package game.Battles;

import game.IntVector;

public class AttackPattern {

    public static IntVector[][] SINGLE_TARGET = {{
            new IntVector(0,0)
    }};

    public static IntVector[][] MORTAR = {{
            new IntVector(0,0),
            new IntVector(0,-1),
            new IntVector(0,1),
            new IntVector(-1,0),
            new IntVector(1,0)
    }};

    public static IntVector[][] LINE = {
            { new IntVector(0,0), new IntVector( 0, 1), new IntVector( 0, 2)},
            { new IntVector(0,0), new IntVector( 0,-1), new IntVector( 0,-2)},
            { new IntVector(0,0), new IntVector( 1, 0), new IntVector( 2, 0)},
            { new IntVector(0,0), new IntVector(-1, 0), new IntVector(-2, 0)}
    };

    public static IntVector[][] SHORT_LINE = {
            { new IntVector(0,0), new IntVector( 0, 1) },
            { new IntVector(0,0), new IntVector( 0,-1) },
            { new IntVector(0,0), new IntVector( 1, 0) },
            { new IntVector(0,0), new IntVector(-1, 0) }
    };

    public static IntVector[][] SHOTGUN = {
            { new IntVector(0,0), new IntVector( 0, 1), new IntVector( 1, 1), new IntVector(-1, 1) },
            { new IntVector(0,0), new IntVector( 0,-1), new IntVector(-1,-1), new IntVector( 1,-1) },
            { new IntVector(0,0), new IntVector( 1, 0), new IntVector( 1,-1), new IntVector( 1, 1) },
            { new IntVector(0,0), new IntVector(-1, 0), new IntVector(-1, 1), new IntVector(-1,-1) }
    };

    public static IntVector[][] SPREAD = {
            { new IntVector(0,0), new IntVector( 1, 1), new IntVector( 2, 2), new IntVector( 0, 2), new IntVector(-2, 2), new IntVector(-1, 1) },
            { new IntVector(0,0), new IntVector(-1,-1), new IntVector(-2,-2), new IntVector( 0,-2), new IntVector( 2,-2), new IntVector( 1,-1) },
            { new IntVector(0,0), new IntVector( 1,-1), new IntVector( 2,-2), new IntVector( 2, 0), new IntVector( 2, 2), new IntVector( 1, 1) },
            { new IntVector(0,0), new IntVector(-1, 1), new IntVector(-2, 2), new IntVector(-2, 0), new IntVector(-2,-2), new IntVector(-1,-1) }
    };

    public static 
}
