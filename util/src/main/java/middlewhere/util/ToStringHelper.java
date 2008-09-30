package middlewhere.util;

public class ToStringHelper {
    
    private String prefix;
    
    public ToStringHelper(Class<?> clazz) {
        this.prefix = "[" + clazz.getSimpleName() + ": ";
    }
    
    public void start(StringBuffer buf) {
        buf.append(prefix);
    }
    
    public void addAttribute(StringBuffer buf, String name, Object value) {
        if(buf.length() > prefix.length()) {
            buf.append(", ");
        }
        buf.append(name);
        buf.append("=");
        buf.append(value);
    }
    
    public void end(StringBuffer buf) {
        buf.append("]");        
    }
}
