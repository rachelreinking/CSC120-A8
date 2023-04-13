/**
 * This creates a Bird that implements Contract
 * The Bird has the ability to grab, drop, examine, and use items; fly, walk and rest; shrink and grow.
 * The methods can also be undone.
 * I also made the Bird do something interesting by implementing methods to build nests and feed chicks
 * @author Rachel Reinking
 * @version 12 April 2023
 */

import java.util.ArrayList;

public class Bird implements Contract{

    public int currentSize;
    public boolean hasItem;
    public String item;
    public int xloc;
    public int yloc;
    public ArrayList<String> methodHistory;
    public boolean hasNest;

    public Bird(int initialSize) {
        this.currentSize = initialSize;
        this.hasItem = false;
        this.item = "<null>";
        this.xloc = 0;
        this.yloc = 0;
        this.methodHistory = new ArrayList<>();
        this.hasNest = false;
    }

    /**
     * Allows the bird to grab an item
     * @param item The item being picked up
     * @throws RuntimeException if bird is already holding an item
     */ 
    public void grab(String item) {
        if(this.hasItem) {
            throw new RuntimeException("Bird is already holding an item");
        }
        this.hasItem = true;
        this.item = item;
        this.methodHistory.add("grab");
    }
    
    /**
     * Allows the bird to drop its item
     * @param item The item being dropped
     * @throws RuntimeException if bird is not holding an item or is not holding the specified item
     */
    public String drop(String item) {
        if(!this.hasItem) {
            throw new RuntimeException("Bird is not holding any item");
        }
        if(!item.equals(this.item)) {
            throw new RuntimeException("Bird is not holding " + item);
        }
        this.item = "<null>";
        this.hasItem = false;
        this.methodHistory.add("drop");
        return item;
    }

    
    /**
     * Allows bird to examine the item that it is holding
     * @param item The item being examined
     * @throws RuntimeException if bird is not holding an item or is not holding the specified item
     */
    public void examine(String item) {
        if(!this.hasItem) {
            throw new RuntimeException("Bird is not holding any item");
        }
        if(!item.equals(this.item)) {
            throw new RuntimeException("Bird is not holding " + item);
        }
        System.out.println("Look at this cool" + item + "!");
        this.methodHistory.add("examine");
    }

    /**
     * Allows bird to use the item that it is holding and then drops the item
     * @param item The item being used
     * @throws RuntimeException if bird is not holding an item or is not holding the specified item
     */
    public void use(String item) {
        if(!this.hasItem) {
            throw new RuntimeException("Bird is not holding any item");
        }
        if(!item.equals(this.item)) {
            throw new RuntimeException("Bird is not holding " + item);
        }
        System.out.println("Bird is using " + item);
        this.methodHistory.add("use");
        drop(item);
        undo();
    }

    /**
     * Moves the bird one unit in the specified direction on the grid world
     * @param direction The cardinal direction the bird is walking on the grid world
     * @return T/F: whether the bird can walk in the specified direction
     * @throws RuntimeException if the new location is out of bounds or if the direction given is invalid
     */
    public boolean walk(String direction) {
        if (direction.equals("north")) {
            if (this.yloc > 9 || this.yloc < -10) {
                throw new RuntimeException("Location is out of bounds");
            }
            this.yloc += 1;   
            this.methodHistory.add("walk");
            return true;
        }
        if (direction.equals("south")) {
            if (this.yloc > 10 || this.yloc < -9) {
                throw new RuntimeException("Location is out of bounds");
            }
            this.yloc -= 1;   
            this.methodHistory.add("walk");
            return true;
        }
        if (direction.equals("east")) {
            if (this.xloc > 9 || this.xloc < -10) {
                throw new RuntimeException("Location is out of bounds");
            }
            this.xloc += 1;   
            this.methodHistory.add("walk");
            return true;
        }
        if (direction.equals("west")) {
            if (this.xloc > 10 || this.xloc < -9) {
                throw new RuntimeException("Location is out of bounds");
            }
            this.xloc -= 1;  
            this.methodHistory.add("walk"); 
            return true;
        }
        else {
            throw new RuntimeException("Invalid direction. Valid directions are: north, south, east, west.");
        }
    }

    /**
     * Moves the bird the given number of units in the directions given on the grid world
     * @param x How many units east/west the bird is flying
     * @param y How many units north/south the bird is flying
     * @return T/F: whether the bird can fly the specified number of units
     * @throws RuntimeException if the new location is out of bounds
     */
    public boolean fly(int x, int y) {
        if ((this.xloc + x) > 10 || (this.xloc + x)  < -10) {
            throw new RuntimeException("Location is out of bounds");
        }
        if ((this.yloc + y) > 10 || (this.yloc + y)  < -10) {
            throw new RuntimeException("Location is out of bounds");
        }
        this.xloc += x;
        this.yloc += y;
        this.methodHistory.add("fly");
        return true;
    }

    /**
     * Makes the bird grow two units larger
     * @return the new size of the bird
     */
    public Number shrink() {
        this.currentSize = this.currentSize - 2;
        this.methodHistory.add("shrink");
        return this.currentSize;
    }

    /**
     * Makes the bird shrink two units smaller
     * @return the new size of the bird
     */
    public Number grow() {
        this.currentSize = this.currentSize + 2;
        this.methodHistory.add("grow");
        return this.currentSize;
    }

    /**
     * Allows the bird to rest momentarily
     */
    public void rest() {
        System.out.println("Resting. . . ");
        this.methodHistory.add("rest");
    }

    /**
     * Removes the most recent method from the stack of the method history
     */
    public void undo() {
        this.methodHistory.remove(this.methodHistory.size() - 1);
    }

    /**
     * This method combines other methods to do behavior that feeds chicks that are in a nest
     * @throws RuntimeException if the Bird does not have a nests
     */
    public void feedChick() {
        if (!this.hasNest) {
            throw new RuntimeException("This Bird does not have a nest");
        }
        this.xloc = 0;
        this.yloc = 0;
        fly(3, 3);
        undo();
        grab("worm");
        undo();
        fly(-3, -3);
        undo();
        use("worm");
        undo();
        this.methodHistory.add("feedChick");
    }

    /**
     * This method combines other methods in a way that allows a bird to build a nest at the origin of the grid world
     * @throws RuntimeException if the Bird has already built a nest
     */
    public void buildNest() {
        if (this.hasNest) {
            throw new RuntimeException("This Bird has already built a nest");
        }
        this.xloc = 0;
        this.yloc = 0;
        this.hasNest = true;

        //gather materials and build
        int i = 0;
        while (i < 10) {
            fly(2, 2);
            undo();
            grab("stick");
            undo();
            fly(-2, -2);
            undo();
            use("stick");
            undo();
            i++;
        }
        this.methodHistory.add("buildNest");
    }

    public static void main(String[] args) {
        Bird myBird = new Bird(10);
        myBird.grab("stick");
        myBird.examine("stick");
        myBird.drop("stick");
        myBird.buildNest();
        myBird.feedChick();
        myBird.rest();
        myBird.grow();
        myBird.shrink();
        myBird.walk("north");
        myBird.undo();

    }
    
}
