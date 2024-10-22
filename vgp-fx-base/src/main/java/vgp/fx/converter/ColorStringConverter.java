package vgp.fx.converter;

import javafx.scene.paint.Color;

/**
 * A string converter for instances of {@link Color}.
 * <p>
 * This class's implementation of {@link #fromString} tolerates {@code null}
 * parameters and relies on {@link Color#valueOf(String)} when dealing with
 * non-null parameters.
 * <p>
 * Also noteworthy is that this class also implements {@code CloneableBase} from
 * the module {@code vgp.base}. Subclasses should redefine its {@code clone}
 * method in order to ensure proper cloneability according to that interface's
 * specifications.
 * 
 * @author [...]
 * @version 6.1
 * @since 1.8
 */
public class ColorStringConverter extends StringConverterBase<Color> {

	/*
	 * Constructors
	 */

	/**
	 * Constructs a new {@code ColorStringConverter} with the default
	 * characteristics.
	 */
	public ColorStringConverter() {
		super();
	}

	/*
	 * Overriding methods
	 */

	/**
	 * @return {@code null} if {@code string} is {@code null} or merely whitespace
	 *         characters; otherwise, the corresponding color value
	 * 
	 * @throws IllegalArgumentException if the parameter does not correspond to any
	 *                                  valid color value (as determined by
	 *                                  {@link Color#valueOf(String)})
	 */
	@Override
	public Color fromString(String string) {
		if (string == null)
			return null;
		String trim = string.trim();
		return (trim.length() == 0) ? null : Color.valueOf(string);
	}

	@Override
	public ColorStringConverter clone() {
		return new ColorStringConverter();
	}

	/**
	 * The entry point for this class to be run from the command line.
	 * 
	 * @param args the string array passed from the command line
	 */
	public static void main(String[] args) {
		Color color = Color.valueOf("#01234567");
		System.out.println(new ColorStringConverter().toString(color));
	}

}
