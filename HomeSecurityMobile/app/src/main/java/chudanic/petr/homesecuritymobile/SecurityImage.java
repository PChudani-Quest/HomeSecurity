package chudanic.petr.homesecuritymobile;

import java.util.Objects;

/**
 * Created by Petr on 16.12.2017.
 */

public class SecurityImage {

    private String name;
    private Long timestamp;
    private String downloadUrl;

    public String getName() {
        return name;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  SecurityImage)) {
            return false;
        }

        return Objects.equals(name, ((SecurityImage) obj).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
