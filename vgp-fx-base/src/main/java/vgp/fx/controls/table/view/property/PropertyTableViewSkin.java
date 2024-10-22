package vgp.fx.controls.table.view.property;

import java.util.Arrays;
import java.util.Objects;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.skin.TableViewSkin;
import javafx.util.Callback;
import vgp.dispose.Disposable;
import vgp.fx.application.ApplicationBase;

/**
 * A {@link TableViewSkin} meant only for a {@link PropertyTableView}.
 * 
 * @author (to be added)
 * @version 6.11
 * @since 5.5
 */
final class PropertyTableViewSkin extends TableViewSkin<BeanPropertyMetadata> implements Disposable {

    private final PropertyMap propertyMap;

    /**
     * Constructs a new {@code PropertyTableViewSkin} with the specified
     * {@code PropertyTableView} and bean class.
     * 
     * @param control   the {@code TableView} to bind to this skin
     * @param beanClass the class whose methods will be used to create rows for this
     *                  skin's {@code TableView}
     */
    public PropertyTableViewSkin(final PropertyTableView control, final Class<?> beanClass) {
        super(Objects.requireNonNull(control));
        propertyMap = new PropertyMap(beanClass);

        control.setItems(propertyMap.getMetadataList());

        control.setEditable(false);
        control.setTableMenuButtonVisible(false);
        if (control.getSortPolicy() == null) {
            control.setSortPolicy(ApplicationBase.defaultTableViewSortPolicy());
        }

        control.setPrefSize(700, 400);

        final TableColumn<BeanPropertyMetadata, String> column0;
        final TableColumn<BeanPropertyMetadata, Class<?>> column1;
        final TableColumn<BeanPropertyMetadata, Boolean> column2;
        final TableColumn<BeanPropertyMetadata, Boolean> column3;

        column0 = new TableColumn<>("Property Name");
        column1 = new TableColumn<>("Property Type");
        column2 = new TableColumn<>("Is Property Readable?");
        column3 = new TableColumn<>("Is Property Writable?");

        column0.setEditable(false);
        column1.setEditable(false);
        column2.setEditable(false);
        column3.setEditable(false);

        column0.setCellFactory(ApplicationBase.defaultTableColumnCellFactory());
        column1.setCellFactory(ApplicationBase.defaultTableColumnCellFactory());
        column2.setCellFactory(ApplicationBase.defaultTableColumnCellFactory());
        column3.setCellFactory(ApplicationBase.defaultTableColumnCellFactory());

        column0.setCellValueFactory(
                new Callback<CellDataFeatures<BeanPropertyMetadata, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            CellDataFeatures<BeanPropertyMetadata, String> cellDataFeatures) {
                        if (cellDataFeatures == null) {
                            return null;
                        }
                        final BeanPropertyMetadata beanPropertyMetadata = cellDataFeatures.getValue();
                        if (beanPropertyMetadata == null) {
                            return null;
                        }
                        if (beanPropertyMetadata.isDisposed()) {
                            return null;
                        }
                        return new ObservableValueBase<String>() {
                            @Override
                            public String getValue() {
                                return beanPropertyMetadata.getPropertyName();
                            }
                        };
                    }
                });
        column1.setCellValueFactory(
                new Callback<CellDataFeatures<BeanPropertyMetadata, Class<?>>, ObservableValue<Class<?>>>() {
                    @Override
                    public ObservableValue<Class<?>> call(
                            CellDataFeatures<BeanPropertyMetadata, Class<?>> cellDataFeatures) {
                        if (cellDataFeatures == null) {
                            return null;
                        }
                        final BeanPropertyMetadata beanPropertyMetadata = cellDataFeatures.getValue();
                        if (beanPropertyMetadata == null) {
                            return null;
                        }
                        if (beanPropertyMetadata.isDisposed()) {
                            return null;
                        }
                        return new ObservableValueBase<Class<?>>() {
                            @Override
                            public Class<?> getValue() {
                                return beanPropertyMetadata.getPropertyType();
                            }
                        };
                    }
                });
        column2.setCellValueFactory(
                new Callback<CellDataFeatures<BeanPropertyMetadata, Boolean>, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(
                            CellDataFeatures<BeanPropertyMetadata, Boolean> cellDataFeatures) {
                        if (cellDataFeatures == null) {
                            return null;
                        }
                        final BeanPropertyMetadata beanPropertyMetadata = cellDataFeatures.getValue();
                        if (beanPropertyMetadata == null) {
                            return null;
                        }
                        if (beanPropertyMetadata.isDisposed()) {
                            return null;
                        }
                        return new ObservableValueBase<Boolean>() {
                            @Override
                            public Boolean getValue() {
                                return beanPropertyMetadata.isReadable();
                            }
                        };
                    }
                });
        column3.setCellValueFactory(
                new Callback<CellDataFeatures<BeanPropertyMetadata, Boolean>, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(
                            CellDataFeatures<BeanPropertyMetadata, Boolean> cellDataFeatures) {
                        if (cellDataFeatures == null) {
                            return null;
                        }
                        final BeanPropertyMetadata beanPropertyMetadata = cellDataFeatures.getValue();
                        if (beanPropertyMetadata == null) {
                            return null;
                        }
                        if (beanPropertyMetadata.isDisposed()) {
                            return null;
                        }
                        return new ObservableValueBase<Boolean>() {
                            @Override
                            public Boolean getValue() {
                                return beanPropertyMetadata.isWritable();
                            }
                        };
                    }
                });

        control.getColumns().clear();
        control.getColumns().add(column0);
        control.getColumns().add(column1);
        control.getColumns().add(column2);
        control.getColumns().add(column3);
    }

    @Override
    public boolean isDisposed() {
        return getSkinnable() == null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * When overriding, always call the super-implementation
     * <em>first.</em>
     */
    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        System.out.println("PropertyTableViewSkin.layoutChildren: " + Arrays.asList(x, y, w, h));
        System.out.println();
        super.layoutChildren(x, y, w, h);
    }

    /**
     * {@inheritDoc}
     * <p>
     * When overriding, always call the super-implementation <em>last.</em> That
     * way, {@code getSkinnable()} is {@code null} after the call to that
     * super-implementation.
     * <p>
     * Also, in order to preserve this method's idempotence (as specified by @link
     * Disposable#dispose()}), always start each override of this method with...
     * 
     * <pre>
     * if (isDisposed()) {
     *     return; // This skin has already been disposed
     * }
     * </pre>
     * 
     * ...and end each override of this method with...
     * 
     * <pre>
     * super.dispose(); // getSkinnable() is null after this line
     * </pre>
     */
    @Override
    public void dispose() {
        if (isDisposed()) {
            return; // This skin has already been disposed
        }

        getSkinnable().setItems(null);
        propertyMap.dispose();

        for (TableColumn<BeanPropertyMetadata, ?> tableColumn : getSkinnable().getColumns()) {
            tableColumn.setCellFactory(ApplicationBase.defaultTableColumnCellFactory());
            tableColumn.setCellValueFactory(null);
        }
        getSkinnable().getColumns().clear();

        super.dispose(); // getSkinnable() is null after this line
    }

}
