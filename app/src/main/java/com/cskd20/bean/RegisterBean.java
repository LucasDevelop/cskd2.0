package com.cskd20.bean;

/**
 * @创建者 lucas
 * @创建时间 2017/5/25 0025 17:07
 * @描述 注册
 */

public class RegisterBean {

    /**
     * msg : 注册成功！
     * data : {"id_card_logo":null,"driving_logo":null,"city":null,"xingzhi":null,"id_card":null,"available":"1",
     * "car_series":null,"car_type":null,"password":"40da7b2c42dddbb9ad853ec0631cf3b4","userfrom":"0","integral":"0",
     * "last_time":null,"driver_no_logo":null,"logo":"","car_brand":null,"id":"2","car_type_logo":null,
     * "salt":"5927dfdd58dbc","level":"1","sex":"男","nikename":"","driver_no":null,"car_color":null,
     * "car_no_logo":null,"realname":"","token":"y0gdO9CbjsPte8kZKDWNXIvnlrxh13VB6AmYFSf4","driving":null,
     * "phone":"1513488478","driving_no":null,"addtime":"2017-05-26 15:57:17","car_no":null,"grade":"0","status":"0"}
     * status : 1
     */
    public String msg;
    public DataEntity data;
    public int        status;


    public static class DataEntity {
        /**
         * id_card_logo : null
         * driving_logo : null
         * city : null
         * xingzhi : null
         * id_card : null
         * available : 1
         * car_series : null
         * car_type : null
         * password : 40da7b2c42dddbb9ad853ec0631cf3b4
         * userfrom : 0
         * integral : 0
         * last_time : null
         * driver_no_logo : null
         * logo :
         * car_brand : null
         * id : 2
         * car_type_logo : null
         * salt : 5927dfdd58dbc
         * level : 1
         * sex : 男
         * nikename :
         * driver_no : null
         * car_color : null
         * car_no_logo : null
         * realname :
         * token : y0gdO9CbjsPte8kZKDWNXIvnlrxh13VB6AmYFSf4
         * driving : null
         * phone : 1513488478
         * driving_no : null
         * addtime : 2017-05-26 15:57:17
         * car_no : null
         * grade : 0
         * status : 0
         */
        public String id_card_logo;
        public String driving_logo;
        public String city;
        public String xingzhi;
        public String id_card;
        public String available;
        public String car_series;
        public String car_type;
        public String password;
        public String userfrom;
        public String integral;
        public String last_time;
        public String driver_no_logo;
        public String logo;
        public String car_brand;
        public String id;
        public String car_type_logo;
        public String salt;
        public String level;
        public String sex;
        public String nikename;
        public String driver_no;
        public String car_color;
        public String car_no_logo;
        public String realname;
        public String token;
        public String driving;
        public String phone;
        public String driving_no;
        public String addtime;
        public String car_no;
        public String grade;
        public String status;

        @Override
        public String toString() {
            return "DataEntity{" +
                    "id_card_logo='" + id_card_logo + '\'' +
                    ", driving_logo='" + driving_logo + '\'' +
                    ", city='" + city + '\'' +
                    ", xingzhi='" + xingzhi + '\'' +
                    ", id_card='" + id_card + '\'' +
                    ", available='" + available + '\'' +
                    ", car_series='" + car_series + '\'' +
                    ", car_type='" + car_type + '\'' +
                    ", password='" + password + '\'' +
                    ", userfrom='" + userfrom + '\'' +
                    ", integral='" + integral + '\'' +
                    ", last_time='" + last_time + '\'' +
                    ", driver_no_logo='" + driver_no_logo + '\'' +
                    ", logo='" + logo + '\'' +
                    ", car_brand='" + car_brand + '\'' +
                    ", id='" + id + '\'' +
                    ", car_type_logo='" + car_type_logo + '\'' +
                    ", salt='" + salt + '\'' +
                    ", level='" + level + '\'' +
                    ", sex='" + sex + '\'' +
                    ", nikename='" + nikename + '\'' +
                    ", driver_no='" + driver_no + '\'' +
                    ", car_color='" + car_color + '\'' +
                    ", car_no_logo='" + car_no_logo + '\'' +
                    ", realname='" + realname + '\'' +
                    ", token='" + token + '\'' +
                    ", driving='" + driving + '\'' +
                    ", phone='" + phone + '\'' +
                    ", driving_no='" + driving_no + '\'' +
                    ", addtime='" + addtime + '\'' +
                    ", car_no='" + car_no + '\'' +
                    ", grade='" + grade + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RegisterBean{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", status=" + status +
                '}';
    }
}
