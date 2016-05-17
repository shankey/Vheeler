package adcar.com.model.servertalkers;

/**
 * Created by aditya on 15/02/16.
 */
public class Versions {

    String name;
    Integer version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString(){
        return name + " - " + version;
    }
}
