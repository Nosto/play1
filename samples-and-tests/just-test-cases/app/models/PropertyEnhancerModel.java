package models;

public class PropertyEnhancerModel {
    
    public String text;

    public final String finalText = "test";

    public static String staticText;

    private String privateText;
   
    public String getText() {
        return text;
    }

    public class PublicClass {
	public String publicField = "publicField";
    }

    class DefaultClass {
	public String publicField = "publicField";
    }

    protected class ProtectedClass {
	public String publicField = "publicField";
    }

    private class PrivateClass {
	public String publicField = "publicField";
    }
}

