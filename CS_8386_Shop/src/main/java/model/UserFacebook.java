package model;

public class UserFacebook {
    private String id;
    private String email;
    private String name;
    private Picture picture;
    public static class Picture {
        private Data data; // Đối tượng Data chứa URL hình ảnh

        public static class Data {
            private String url; // URL của ảnh đại diện

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public UserFacebook(String id, String email, String name, Picture picture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserFacebook{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", picture=" + picture +
                '}';
    }
}
