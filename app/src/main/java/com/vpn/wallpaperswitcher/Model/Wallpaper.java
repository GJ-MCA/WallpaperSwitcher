package com.vpn.wallpaperswitcher.Model;

public class Wallpaper {

    private float id;
    private float width;
    private float height;
    private String url;
    private String photographer;
    private String photographer_url;
    private float photographer_id;
    private String avg_color;
    Src SrcObject;
    private boolean liked;
    private String alt;


    // Getter Methods

    public float getId() {
        return id;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public String getPhotographer_url() {
        return photographer_url;
    }

    public float getPhotographer_id() {
        return photographer_id;
    }

    public String getAvg_color() {
        return avg_color;
    }

    public Src getSrc() {
        return SrcObject;
    }

    public boolean getLiked() {
        return liked;
    }

    public String getAlt() {
        return alt;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public void setPhotographer_url(String photographer_url) {
        this.photographer_url = photographer_url;
    }

    public void setPhotographer_id(float photographer_id) {
        this.photographer_id = photographer_id;
    }

    public void setAvg_color(String avg_color) {
        this.avg_color = avg_color;
    }

    public void setSrc(Src srcObject) {
        this.SrcObject = srcObject;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }


    public class Src {
        private String original;
        private String large2x;
        private String large;
        private String medium;
        private String small;
        private String portrait;
        private String landscape;
        private String tiny;


        // Getter Methods

        public String getOriginal() {
            return original;
        }

        public String getLarge2x() {
            return large2x;
        }

        public String getLarge() {
            return large;
        }

        public String getMedium() {
            return medium;
        }

        public String getSmall() {
            return small;
        }

        public String getPortrait() {
            return portrait;
        }

        public String getLandscape() {
            return landscape;
        }

        public String getTiny() {
            return tiny;
        }

        // Setter Methods

        public void setOriginal(String original) {
            this.original = original;
        }

        public void setLarge2x(String large2x) {
            this.large2x = large2x;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public void setLandscape(String landscape) {
            this.landscape = landscape;
        }

        public void setTiny(String tiny) {
            this.tiny = tiny;
        }
    }
}