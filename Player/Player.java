package Player;

public class Player {
    private int money;
    private int health;
    public Player(int money, int health){
        this.money = money;
        this.health = health;
    }
    public int getMoney(){
        return money;
    }
    public void setMoney(int amount){ // set the money of player
        this.money = Math.max(0, amount); // make sure the money not negative
        // amount < 0 => money = 0
        // amount >= 0 => money = amount
    } 
    public int getHealth(){
        return health;
    }
    public void setHealth(int newHealth){ // set health of player
        this.health = Math.max(0, newHealth); // // make sure the health not negative
    }

    public void addMoney(int amount){
        money += amount;
    }

    public void spendMoney(int amount){
        if (money >= amount){
            money -= amount; 
        }
        else{
            System.out.println("You are not enough money"); // update UI for this notification
        }
    }
    public void takeDamage(int dmg){
        health = Math.max(0, health-dmg);
    }
    public void healHealth(int heal){
        health = Math.min(100, health +heal);
    }
}
