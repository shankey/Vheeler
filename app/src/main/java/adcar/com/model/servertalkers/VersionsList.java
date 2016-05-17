package adcar.com.model.servertalkers;

import java.util.List;

import adcar.com.model.servertalkers.Versions;

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
