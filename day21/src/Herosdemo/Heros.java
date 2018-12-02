package Herosdemo;

public class Heros {
    private int id;
    private String name;
    private String city;
    private String sex;
    private int birth;
    private int die;
    private int wuli;

    public Heros(int id, String name, String city, String sex, int birth, int die, int wuli) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.sex = sex;
        this.birth = birth;
        this.die = die;
        this.wuli = wuli;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getSex() {
        return sex;
    }

    public int getBirth() {
        return birth;
    }

    public int getDie() {
        return die;
    }

    public int getWuli() {
        return wuli;
    }

    @Override
    public String toString() {
        return "Heros{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", sex='" + sex + '\'' +
                ", birth=" + birth +
                ", die=" + die +
                ", wuli=" + wuli +
                '}';
    }
}
