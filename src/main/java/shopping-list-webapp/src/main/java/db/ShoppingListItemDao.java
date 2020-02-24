package db;

import model.ShoppingListItem;

import java.util.List;

public interface ShoppingListItemDao {

    List<ShoppingListItem> getAllItems();

    ShoppingListItem getItem(long id);

    ShoppingListItem getItemByIndex(int i);

    boolean addItem(ShoppingListItem newItem);

    boolean removeItem(ShoppingListItem item);
}
