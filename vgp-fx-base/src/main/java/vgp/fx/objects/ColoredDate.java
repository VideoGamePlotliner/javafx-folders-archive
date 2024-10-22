package vgp.fx.objects;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import vgp.fx.controls.table.view.ColoredDateTableView;

/**
 * Tester class used by {@link ColoredDateTableView}.
 * <p>
 * This class's {@linkplain Property writable properties} include one or more
 * {@link Color} properties and one or more {@link LocalDate} properties.
 * 
 * @author [...]
 * @version 6.2
 * @see Color
 * @see LocalDate
 * @since 2.5
 */
public class ColoredDate {

	private final ObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color", Color.TRANSPARENT);
	private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(this, "date", LocalDate.EPOCH);
	private final StringProperty text = new SimpleStringProperty(this, "text", "[default text]");

	/**
	 * Constructs a new {@code ColoredDate}
	 * with the default color, date, and text.
	 */
	public ColoredDate() {
		super();
	}

	/**
	 * Constructs a new {@code ColoredDate} with the specified color, date, and
	 * text.
	 * 
	 * @param color the initial value for the {@link #colorProperty()}
	 * @param date  the initial value for the {@link #dateProperty()}
	 * @param text  the initial value for the {@link #textProperty()}
	 */
	public ColoredDate(Color color, LocalDate date, String text) {
		this();
		setColor(color);
		setDate(date);
		setText(text);
	}

	public final void setColor(Color value) {
		color.set(value);
	}

	public final Color getColor() {
		return color.get();
	}

	public final ObjectProperty<Color> colorProperty() {
		return color;
	}

	public final void setDate(LocalDate value) {
		date.set(value);
	}

	public final LocalDate getDate() {
		return date.get();
	}

	public final ObjectProperty<LocalDate> dateProperty() {
		return date;
	}

	public final void setText(String value) {
		text.set(value);
	}

	public final String getText() {
		return text.get();
	}

	public final StringProperty textProperty() {
		return text;
	}

	@Override
	public String toString() {
		return "ColoredDate [color=" + getColor() + ", date=" + getDate() + ", text=" + getText() + "]";
	}

}
