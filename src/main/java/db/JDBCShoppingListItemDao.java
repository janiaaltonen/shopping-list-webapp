package db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ShoppingListItem;

public class JDBCShoppingListItemDao implements ShoppingListItemDao {



    private List<ShoppingListItem> items;
    private long defaultId;

    public JDBCShoppingListItemDao(){
        this.items = new ArrayList<>();
        this.defaultId = 0;
    }

    public long getDefaultId() {
        return defaultId;
    }

    protected Connection getConnection() throws SQLException {
        String url = System.getenv("URL_DB");
        String dbUser = System.getenv("USER_DB").split(":")[0];
        String dbPw = System.getenv("USER_DB").split(":")[1];
        MysqlDataSource ds = new MysqlDataSource();
        ds.setUrl(url);
        return ds.getConnection(dbUser, dbPw);
    }

    private static void closeResources(AutoCloseable... sqlResources) {
        for (AutoCloseable a : sqlResources) {
            if (a != null) {
                try {
                    a.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<ShoppingListItem> getAllItems() {
        items.clear();
        Connection connection = null;
        PreparedStatement pStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            String sql = "SELECT * FROM shopping_list";
            pStatement = connection.prepareStatement(sql);
            resultSet = pStatement.executeQuery();
            while(resultSet.next()) {
                String title = resultSet.getString("title");
                int titleLength = title.length();
                String titleWithCapitalStart = title.substring(0, 1).toUpperCase() + title.substring(1, titleLength);
                ShoppingListItem item = new ShoppingListItem(resultSet.getLong("id"), titleWithCapitalStart);
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            closeResources(connection, pStatement, resultSet);
        }
        return items;
    }

    /* could've been implemented like getAllItems() method with WHERE id = ? clause.
     * Especially in case, when db table includes lots of rows because it's not efficient to get them all
     * but had to try Stream API, which is new to me
     */
    @Override
    public ShoppingListItem getItem(long id) {
        List<ShoppingListItem> listItems = getAllItems();

        return listItems.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
    }

    /*
     *returns the corresponding item from the chosen index
     * other way to do this is get the full list and then with [list].get(index-1) to get matching item.
     * Idea of this method is that second item's id could be something else than 2 if items are added or removed
     */
    @Override
    public ShoppingListItem getItemByIndex(int index) {
        Connection connection = null;
        PreparedStatement pStatement = null;
        ResultSet resultSet = null;
        long id = 0;
        String titleWithCapitalStart = null;
        try {
            connection = getConnection();
            String sql = "SELECT * FROM shopping_list LIMIT ? OFFSET ?";
            pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, index);
            int offset = index -1;
            pStatement.setInt(2, offset);
            resultSet = pStatement.executeQuery();
            while(resultSet.next()) {
                id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                int titleLength = title.length();
                titleWithCapitalStart = title.substring(0, 1).toUpperCase() + title.substring(1, titleLength);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            closeResources(connection, pStatement, resultSet);
        }
        if (titleWithCapitalStart != null) {
            return new ShoppingListItem(id, titleWithCapitalStart);
        }
        return null;
    }

    @Override
    public boolean addItem(ShoppingListItem newItem) {
        Connection connection = null;
        PreparedStatement pStatement = null;
        ResultSet resultSet = null;
        boolean successful = false;
        try {
            connection = getConnection();
            int rowsAffected = 0;
            String sql = "SELECT * FROM shopping_list WHERE title = ?";
            pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, newItem.getTitle().toLowerCase());
            resultSet = pStatement.executeQuery();
            if (!resultSet.next()) {
                sql = "INSERT INTO shopping_list(title) VALUES(?)";
                pStatement = connection.prepareStatement(sql);
                pStatement.setString(1,newItem.getTitle());
                //pStmt.setString(2,newItem.getTitle().toLowerCase());
                rowsAffected = pStatement.executeUpdate();
            }
            if (rowsAffected > 0){
                successful = true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            closeResources(connection, pStatement, resultSet);
        }
        return successful;
    }

    @Override
    public boolean removeItem(ShoppingListItem item) {
        Connection connection = null;
        PreparedStatement pStatement = null;
        boolean successful = false;
        try {
            connection = getConnection();
            int rowsAffected = 0;
            String sql = "DELETE FROM shopping_list WHERE title = ?";
            pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, item.getTitle().toLowerCase());
            rowsAffected = pStatement.executeUpdate();

            if (rowsAffected > 0){
                successful = true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            closeResources(connection, pStatement);
        }
        return successful;
    }
}
