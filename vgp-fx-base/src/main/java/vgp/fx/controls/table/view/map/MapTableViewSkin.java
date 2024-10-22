package vgp.fx.controls.table.view.map;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakMapChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.skin.TableViewSkin;
import javafx.util.Callback;
import vgp.fx.application.ApplicationBase;
import vgp.fx.collections.CallbackWrapperList;
import vgp.fx.collections.CustomObservableList;
import vgp.fx.controls.table.view.TableEditorViewSkin;
import vgp.fx.listenerhandler.ListenerAndHandlerBindings;

/**
 * A {@link TableViewSkin} meant only for a {@link MapTableView}.
 * <p>
 * The keys are in the first column. The values are in the second column.
 * <p>
 * Table editability, if any, must be carefully implemented.
 * 
 * @author (to be added)
 * @version 6.17
 * @param <K> the type of this skin's table's map's keys
 * @param <V> the type of this skin's table's map's values
 * @see Map
 * @see Entry
 * @since 5.6
 */
final class MapTableViewSkin<K, V> extends TableEditorViewSkin<Entry<K, V>> {

    private final Callback<Entry<K, V>, K> callback_getKeyFromEntry = Entry::getKey;

    // Must be and remain in sync with backingMap.entrySet()
    private final CustomObservableList<Entry<K, V>> entrySetAsList = new CustomObservableList<Entry<K, V>>() {
        @Override
        protected void doAdd(int index, Entry<K, V> element) {
            Objects.requireNonNull(element);
            for (Entry<K, V> entry : this) {
                if (Objects.equals(entry.getKey(), element.getKey())) {
                    throw new IllegalArgumentException("Key of entry seeking to be added is already in this list");
                }
            }
            super.doAdd(index, element);
        }
    };

    // Do not set this field to null anywhere other than here and dispose()
    private ObservableMap<K, V> backingMap = null;

    // Forwarding changes from map to list
    private final MapChangeListener<K, V> backingMap_mapChangeListener = change -> {
        verifyNotDisposed();
        Objects.requireNonNull(backingMap);

        System.out.println("MapTableViewSkin.backingMap_mapChangeListener.onChanged(" + change + ")");
        System.out.println();

        if (change != null) {
            final K key = change.getKey();
            final V valueAdded = change.getValueAdded();
            final V valueRemoved = change.getValueRemoved();
            final boolean wasAdded = change.wasAdded();
            final boolean wasRemoved = change.wasRemoved();

            if (wasAdded && wasRemoved) {
                for (int i = 0; i < entrySetAsList.size(); i++) {
                    final Entry<K, V> entry = entrySetAsList.get(i);
                    if (Objects.equals(entry.getKey(), key)) {
                        entrySetAsList.set(i, Map.entry(key, valueAdded));
                        break;
                    }
                }
            } else if (wasAdded && !wasRemoved) {
                entrySetAsList.add(Map.entry(key, valueAdded));
            } else if (!wasAdded && wasRemoved) {
                entrySetAsList.remove(Map.entry(key, valueRemoved));
            }
        }

        getSkinnable().layout();
    };

    private final WeakMapChangeListener<K, V> backingMap_weakMapChangeListener = new WeakMapChangeListener<>(
            this.backingMap_mapChangeListener);

    // Forwarding changes from list to map
    private final ListChangeListener<Entry<K, V>> entrySetAsList_listChangeListener = c -> {
        verifyNotDisposed();
        Objects.requireNonNull(backingMap);

        System.out.println("MapTableViewSkin.entrySetAsList_listChangeListener.onChanged(" + c + ")");
        System.out.println();

        if (c != null) {
            while (c.next()) {
                if (c.wasAdded() || c.wasRemoved()) {

                    final ObservableList<Entry<K, V>> entriesAdded = FXCollections
                            .observableArrayList(c.getAddedSubList());

                    final ObservableList<Entry<K, V>> entriesRemoved = FXCollections
                            .observableArrayList(c.getRemoved());

                    final List<K> keysAdded = new CallbackWrapperList<K, Entry<K, V>>(
                            entriesAdded, callback_getKeyFromEntry);

                    final List<K> keysRemoved = new CallbackWrapperList<K, Entry<K, V>>(
                            entriesRemoved, callback_getKeyFromEntry);

                    final Map<K, V> newEntries = new LinkedHashMap<>();
                    for (K key : keysAdded) {
                        for (Entry<K, V> entry : entriesAdded) {
                            if (Objects.equals(entry.getKey(), key)) {
                                final V value = entry.getValue();
                                newEntries.put(key, value);
                            }
                        }
                    }
                    backingMap.putAll(newEntries);

                    final ObservableList<K> keysRemovedButNotAdded = FXCollections.observableArrayList(keysRemoved);
                    keysRemovedButNotAdded.removeAll(keysAdded);
                    backingMap.keySet().removeAll(keysRemovedButNotAdded);
                }
            }
        }

        getSkinnable().layout();
    };

    /**
     * Constructs a new {@code MapTableViewSkin} with the specified
     * {@code MapTableView} and backing map.
     * 
     * @param control    the {@code TableView} to bind to this skin
     * @param backingMap the map whose keys will be used to create rows for the
     *                   given {@code TableView}
     */
    public MapTableViewSkin(final MapTableView<K, V> control, final ObservableMap<K, V> backingMap) {
        super(Objects.requireNonNull(control));
        this.backingMap = Objects.requireNonNull(backingMap);

        ListenerAndHandlerBindings.addListChangeListener(entrySetAsList, this.entrySetAsList_listChangeListener);
        ListenerAndHandlerBindings.addMapChangeListener(backingMap, this.backingMap_weakMapChangeListener);

        entrySetAsList.setAll(backingMap.entrySet());
        control.setItems(entrySetAsList);

        final TableColumn<Entry<K, V>, K> column0;
        final TableColumn<Entry<K, V>, V> column1;

        column0 = new TableColumn<>("Key");
        column1 = new TableColumn<>("Value");

        column0.setEditable(false);
        column1.setEditable(false);

        column0.setCellFactory(ApplicationBase.defaultTableColumnCellFactory());
        column1.setCellFactory(ApplicationBase.defaultTableColumnCellFactory());

        column0.setCellValueFactory(
                new Callback<CellDataFeatures<Entry<K, V>, K>, ObservableValue<K>>() {
                    @Override
                    public ObservableValue<K> call(
                            CellDataFeatures<Entry<K, V>, K> cellDataFeatures) {
                        if (cellDataFeatures == null) {
                            return null;
                        }
                        final Entry<K, V> entry = cellDataFeatures.getValue();
                        if (entry == null) {
                            return null;
                        }
                        return new ObservableValueBase<K>() {
                            @Override
                            public K getValue() {
                                return entry.getKey();
                            }
                        };
                    }
                });
        column1.setCellValueFactory(
                new Callback<CellDataFeatures<Entry<K, V>, V>, ObservableValue<V>>() {
                    @Override
                    public ObservableValue<V> call(
                            CellDataFeatures<Entry<K, V>, V> cellDataFeatures) {
                        if (cellDataFeatures == null) {
                            return null;
                        }
                        final Entry<K, V> entry = cellDataFeatures.getValue();
                        if (entry == null) {
                            return null;
                        }
                        return new ObservableValueBase<V>() {
                            @Override
                            public V getValue() {
                                return entry.getValue();
                            }
                        };
                    }
                });

        control.getColumns().clear();
        control.getColumns().add(column0);
        control.getColumns().add(column1);
    }

    @Override
    public boolean isDisposed() {
        return getSkinnable() == null;
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return; // This skin has already been disposed
        }

        ListenerAndHandlerBindings.removeListChangeListener(entrySetAsList, this.entrySetAsList_listChangeListener);

        if (backingMap != null) {
            ListenerAndHandlerBindings.removeMapChangeListener(backingMap, this.backingMap_weakMapChangeListener);
            backingMap = null;
        }

        getSkinnable().setItems(null);
        entrySetAsList.dispose();

        for (TableColumn<Entry<K, V>, ?> tableColumn : getSkinnable().getColumns()) {
            tableColumn.setCellFactory(ApplicationBase.defaultTableColumnCellFactory());
            tableColumn.setCellValueFactory(null);
        }
        getSkinnable().getColumns().clear();

        super.dispose(); // getSkinnable() is null after this line
    }

    /**
     * {@inheritDoc}
     * <p>
     * When overriding, always call the super-implementation
     * <em>first.</em>
     */
    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        System.out.println("MapTableViewSkin.layoutChildren: " + Arrays.asList(x, y, w, h));
        System.out.println();
        super.layoutChildren(x, y, w, h);
    }
}
