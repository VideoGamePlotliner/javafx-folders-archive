package vgp.fx.logging;

/**
 * A centralized class for beginSubroutine() and endSubroutine().
 * <p>
 * The {@link #beginSubroutine(String)} and {@link #endSubroutine(String)}
 * methods used to be private methods of many classes in the package
 * {@code vgp.fx.controls}. No more! It's better to have a single class to store
 * code for those methods, so this is that class.
 * 
 * @author (to be added)
 * @version 6.3
 * @since 2.8
 */
public final class SubroutinePrinter {

	private int indentCount; // for calls to "System.out.println()"
	private boolean enabled;

	public SubroutinePrinter() {
		super();
		indentCount = 0;
		enabled = true;
	}

	private void printTabsIfNeeded() {
		for (int i = 0; i < indentCount; i++) {
			System.out.print('\t');
		}
	}

	private void printParagraphBreakIfNeeded() {
		if (indentCount == 0) {
			System.out.println();
		}
	}

	private void plusOneIndent() {
		indentCount++;
	}

	private void minusOneIndent() {
		indentCount--;
	}

	public void beginSubroutine(String subroutineText) {
		if (!enabled) {
			return;
		}
		printTabsIfNeeded();
		System.out.println("Begin: " + subroutineText);
		plusOneIndent();
	}

	public void endSubroutine(String subroutineText) {
		if (!enabled) {
			return;
		}
		minusOneIndent();
		printTabsIfNeeded();
		System.out.println("End: " + subroutineText);
		printParagraphBreakIfNeeded();
	}

}
