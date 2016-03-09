package adcar.com.model;

import java.util.List;

/**
 * Created by aditya on 15/02/16.
 */
public class VersionsList {

    List<Versions> versions;

    public List<Versions> getVersions() {
        return versions;
    }

    public void setVersions(List<Versions> versions) {
        this.versions = versions;
    }

    @Override
    public String toString(){
        return versions.toString();
    }
}
