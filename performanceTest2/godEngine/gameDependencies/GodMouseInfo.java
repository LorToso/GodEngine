package godEngine.gameDependencies;


import java.util.HashMap;
import java.util.Map;

public class GodMouseInfo 
{
	private int x = 0;
	private int y = 0;
	private HashMap<Integer, Boolean> buttonPressed = null;
	private double mouseWheelPosition = 0;
	private boolean mouseOnScreen = false;

	public GodMouseInfo()
	{
		buttonPressed = new HashMap<Integer, Boolean>();
	}
	public GodMouseInfo(GodMouseInfo copy)
	{
		this.x = copy.x;
		this.y = copy.y;
		this.mouseWheelPosition = copy.mouseWheelPosition;
		this.mouseOnScreen = copy.mouseOnScreen;
		
		
		
		HashMap<Integer, Boolean>oldHashMap = copy.getButtonsPressed();
		buttonPressed = new HashMap<Integer, Boolean>();
		
		// Deep copy of Hashmap
		
		for (Map.Entry<Integer, Boolean> entry : oldHashMap.entrySet()) {
		    int button = entry.getKey();
		    boolean isPressed = entry.getValue();
		    buttonPressed.put(button, isPressed);
		}
		
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public HashMap<Integer, Boolean> getButtonsPressed() {
		return buttonPressed;
	}
	public void setButtonPressed(int button, boolean isPressed) {
		this.buttonPressed.put(button, isPressed);
	}
	public double getMouseWheelPosition() {
		return mouseWheelPosition;
	}
	public void setMouseWheelPosition(double d) {
		this.mouseWheelPosition = d;
	}
	public void addMouseWheelPosition(double d) {
		this.mouseWheelPosition += d;
	}

	public boolean isMouseOnScreen() {
		return mouseOnScreen;
	}

	public void setMouseOnScreen(boolean mouseOnScreen) {
		this.mouseOnScreen = mouseOnScreen;
	}
	public boolean equals(GodMouseInfo comp)
	{
		if(comp==null) return false;
		boolean coordinates = this.x==comp.x && this.y==comp.y;
		boolean mouseAndWheel = this.mouseOnScreen==comp.mouseOnScreen && this.mouseWheelPosition==comp.mouseWheelPosition;
		boolean buttons = this.getButtonsPressed().equals(comp.getButtonsPressed());
		return coordinates && mouseAndWheel && buttons;
	}
}
