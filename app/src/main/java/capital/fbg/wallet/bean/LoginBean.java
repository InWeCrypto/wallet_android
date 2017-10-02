package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1.
 * 功能描述：
 * 版本：@version
 */

public class LoginBean implements Serializable {

    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User implements Serializable{
        private String open_id;
        private String nickname;
        private int sex;
        private String img;

        public String getOpen_id() {
            return open_id;
        }

        public void setOpen_id(String open_id) {
            this.open_id = open_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
