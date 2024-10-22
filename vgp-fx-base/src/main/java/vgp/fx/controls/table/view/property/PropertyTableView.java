package vgp.fx.controls.table.view.property;

import java.util.Objects;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.scene.control.TableView;

/**
 * A read-only {@code TableView} meant for viewing a bean class's properties.
 * 
 * @author (to be added)
 * @version 5.5
 * @see BeanPropertyMetadata
 * @see TableView
 * @see Property
 * @see ReadOnlyProperty
 * @since 5.5
 */
public class PropertyTableView extends TableView<BeanPropertyMetadata> {

    /**
     * Create a new {@code TableView} using the given bean class.
     * 
     * @param beanClass the class whose methods will be used to create rows for this {@code TableView}
     */
    public PropertyTableView(Class<?> beanClass) {
        super();
        Objects.requireNonNull(beanClass);
        setSkin(new PropertyTableViewSkin(this, beanClass));
    }

}
