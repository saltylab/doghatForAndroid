package saltyLab.otosan;

public enum ActionID {
	BEKKEN(0),
	AMBITIOUS(1),
	KATSU(2),
	KITA(3),
	MITEIRUKA(4),
	MADAHAYAI(5),
	RIYUHAARU(6),
	SUMIMASEN(7),
	YABAIYABAI(8),
	KIMINOSHIRANAIMONOGATARI(9),
	SECRETBASE(10),
	HACKIINGTOTHEGATE(11),
	ACTION_NUM(12);
	
	private final int id;
	
	
	private ActionID(final int id)
	{
		this.id = id;
	}
	
	public int getID()
	{
		return id;
	}
}
