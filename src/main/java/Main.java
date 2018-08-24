import org.tarantool.*;
import org.tarantool.jdbc.SQLDriver;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static class User{
        public int id;
        public String name;
        public String sname;

        public User(int id, String name, String sname){
            this.id = id;
            this.name = name;
            this.sname = sname;
        }
    }

    public static class Order{
        public int id;
        public int user_id;
        public String description;
        public float price;

        public Order(int id, int user_id, String description, float price){
            this.id = id;
            this.user_id = user_id;
            this.description = description;
            this.price = price;
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        Statement query = conn.createStatement();
        query.executeQuery("create table if not exists users(id integer primary key," +
                "name string," +
                "sname string)");
        query.executeQuery("create table if not exists orders(id integer primary key," +
                "user_id integer," +
                "description string," +
                "price number)");
        query.executeQuery("delete from users");
        query.executeQuery("delete from orders");
    }

    private static void fillInTables(Connection conn) throws SQLException {
        List<Main.User> users = new ArrayList<User>();
        users.add(new User(11, "Valera", "Boss"));
        users.add(new User(12, "Sonya", "Visa"));
        users.add(new User(13, "Arny", "Shvart"));
        users.add(new User(14, "Billy", "Gat"));

        PreparedStatement insert_user = conn.prepareStatement("insert or replace into users values(?, ?, ?)");
        for (Main.User user: users) {
            insert_user.setInt(1, user.id);
            insert_user.setString(2, user.name);
            insert_user.setString(3, user.sname);
            ResultSet rs = insert_user.executeQuery();
            rs.close();
            System.out.println("User " + user.id + " inserted");
        }
    }

    private static void selectDemonstration(Connection conn) throws SQLException {
        Statement query = conn.createStatement();
        ResultSet results = query.executeQuery("select id, name, sname from USERS");
        while (results.next()) {
            System.out.println("id " + results.getString(1));
            System.out.println("name " + results.getString(2));
            System.out.println("sname " + results.getString(3));
        }

        PreparedStatement st = conn.prepareStatement("select 'Valera123', ?");
        st.setInt(1, 23);
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            System.out.println("Column 1 returned " + rs.getString(1));
            System.out.println("Column 2 returned " + rs.getInt(2));
        }
        rs.close();
        st.close();
    }

    private static void transaction(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
    }

    public static void main(String[] args) throws IOException, SQLException {
        SQLDriver driver = new SQLDriver();
        Connection conn = driver.connect("tarantool://localhost:3301?user=guest", null);
        try {
            createTables(conn);
        } catch (NullPointerException e){
            System.out.println("!!!Table creation is not working");
        }
        try {
            fillInTables(conn);
        } catch (NullPointerException e){
            System.out.println("!!!Insert is not working");
        }
        selectDemonstration(conn);
        System.out.println("Select is working");
        try {
            transaction(conn);
        } catch (SQLException e){
            System.out.println("!!!Transactions are not working");
        }
    }
}
