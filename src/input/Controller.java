package input;

/**
 * An interface that handles certain inputs.
 * @author Elliott Vince
 */

public interface Controller {
	
	public boolean isRequestingUp();
	public boolean isRequestingLeft();
	public boolean isRequestingDown();
	public boolean isRequestingRight();
	
	public boolean isRequestingLeftClick();

}
