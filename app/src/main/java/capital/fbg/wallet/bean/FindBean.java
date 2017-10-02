package capital.fbg.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/1.
 * 功能描述：
 * 版本：@version
 */

public class FindBean implements Serializable{
    private ArrayList<Banner> banner;
    private ArrayList<Ico> ico;

    public ArrayList<Banner> getBanner() {
        return banner;
    }

    public void setBanner(ArrayList<Banner> banner) {
        this.banner = banner;
    }

    public ArrayList<Ico> getIco() {
        return ico;
    }

    public void setIco(ArrayList<Ico> ico) {
        this.ico = ico;
    }

    public static class Banner implements Serializable{
        private int id;
        private Detail detail;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
            this.detail = detail;
        }
    }

    public static class Detail implements Serializable{
        private String title;
        private String img;
        private String desc;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class Ico implements Serializable{
        private int id;
        private String name;
        private String img;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
