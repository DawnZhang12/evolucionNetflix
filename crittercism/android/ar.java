// 
// Decompiled by Procyon v0.5.30
// 

package crittercism.android;

public final class ar implements t
{
    int a;
    
    private ar(final int a) {
        this.a = a;
    }
    
    public final boolean a(final h h, final String s, final String s2) {
        synchronized (this) {
            h.a(s, s2, Integer.valueOf(this.a));
            return true;
        }
    }
    
    public static final class a
    {
    }
}