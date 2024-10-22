package vgp.fx.controls.table.view.map;

import java.util.Map;
import java.util.Objects;

import javafx.collections.ObservableMap;
import javafx.scene.control.TableView;

/**
 * A {@code TableView} meant for viewing an {@code ObservableMap}'s keys and
 * values.
 * 
 * @author (to be added)
 * @version 5.6
 * @param <K> the type of this table's map's keys
 * @param <V> the type of this table's map's values
 * @see TableView
 * @see ObservableMap
 * @see Map.Entry
 * @since 5.6
 */
public final class MapTableView<K, V> extends TableView<Map.Entry<K,V>> {

    /**
     * Create a new {@code TableView} with the given map.
     * 
     * @param backingMap the map whose keys will be used to create this table's rows
     */
    public MapTableView(final ObservableMap<K, V> backingMap) {
        super();
        Objects.requireNonNull(backingMap);
        setSkin(new MapTableViewSkin<K, V>(this, backingMap));
    }

}
