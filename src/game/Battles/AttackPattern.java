package game.Battles;

import game.IntVector;

import java.util.ArrayList;

public class AttackPattern {

    private IntVector[][] attackPattern;
    private boolean isDirectional;

    private final static int DOWN = 0;
    private final static int UP = 1;
    private final static int RIGHT = 2;
    private final static int LEFT = 3;
    private final static int DOWN_RIGHT = 4;
    private final static int DOWN_LEFT = 5;
    private final static int UP_RIGHT = 6;
    private final static int UP_LEFT = 7;

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

    public static IntVector[][] SQUARE = {
            { new IntVector(-1,-1), new IntVector(0,-1), new IntVector(1,-1),
              new IntVector(-1, 0), new IntVector(0, 0), new IntVector(1, 0),
              new IntVector(-1, 1), new IntVector(0, 1), new IntVector(1, 1)}
    };


    public AttackPattern( IntVector[][] attackPattern, boolean isDirectional ){
        this.attackPattern = attackPattern;
        this.isDirectional = isDirectional;
    }

    public AttackPattern(){
        this.attackPattern = AttackPattern.SINGLE_TARGET;
        this.isDirectional = false;
    }

    public void set(IntVector[][] attackPattern, boolean directional) {
        this.attackPattern = attackPattern;
        this.isDirectional = directional;
    }

    public void setPattern(IntVector[][] attackPattern){
        this.attackPattern = attackPattern;
    }

    public void setDirectional(boolean directional) {
        isDirectional = directional;
    }

    private int getDirection(int x, int y, int xIndex, int yIndex){

        if(y > yIndex){
            if(x > xIndex){
                return DOWN_RIGHT;
            }
            if(x < xIndex){
                return DOWN_LEFT;
            }
            return DOWN;
        }
        if(y < yIndex){
            if(x > xIndex){
                return UP_RIGHT;
            }
            if(x < xIndex){
                return UP_LEFT;
            }
            return UP;
        }
        if(x > xIndex){
            return RIGHT;
        }
        if(x < xIndex){
            return LEFT;
        }
        return 0;
    }

    public ArrayList<IntVector> calculatePattern(int direction1, int direction2, IntVector position){

        ArrayList<IntVector> pattern = new ArrayList<>();
        IntVector[] ap = attackPattern[direction1];

        if(direction2 == -1){

            for (int i = 0; i < ap.length; i++) {
                pattern.add(new IntVector(position.x + ap[i].x, position.y + ap[i].y));
            }
        }
        else {
            IntVector[] other_ap = attackPattern[direction2];

            for (int i = 0; i < ap.length; i++) {
                pattern.add(new IntVector(position.x + ap[i].x + other_ap[i].x,
                        position.y + ap[i].y + other_ap[i].y));
            }
        }
        return pattern;

    }

    public ArrayList<IntVector> getAttackPattern(int targetX, int targetY, int fromX, int fromY){
        ArrayList<IntVector> pattern = new ArrayList<>();
        IntVector position = new IntVector(targetX,targetY);

        if(isDirectional) {
            int direction = getDirection(targetX, targetY, fromX, fromY);

            switch (direction) {
                case (UP_LEFT): { pattern = calculatePattern(UP, LEFT, position);
                    break;
                }
                case (UP_RIGHT): { pattern = calculatePattern(UP, RIGHT, position);
                    break;
                }
                case (DOWN_LEFT): { pattern = calculatePattern(DOWN, LEFT, position);
                    break;
                }
                case (DOWN_RIGHT): { pattern = calculatePattern(DOWN, RIGHT, position);
                    break;
                }
                default: { //( UP || DOWN || LEFT || RIGHT )
                    pattern = calculatePattern(direction, -1, position);
                    break;
                }
            }
        }
        else {
            pattern = calculatePattern(0, -1, position);
        }
        return pattern;
    }

}
