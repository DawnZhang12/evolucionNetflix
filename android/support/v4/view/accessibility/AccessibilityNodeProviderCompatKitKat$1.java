// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.os.Bundle;
import java.util.List;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

final class AccessibilityNodeProviderCompatKitKat$1 extends AccessibilityNodeProvider
{
    final /* synthetic */ AccessibilityNodeProviderCompatKitKat$AccessibilityNodeInfoBridge val$bridge;
    
    AccessibilityNodeProviderCompatKitKat$1(final AccessibilityNodeProviderCompatKitKat$AccessibilityNodeInfoBridge val$bridge) {
        this.val$bridge = val$bridge;
    }
    
    public AccessibilityNodeInfo createAccessibilityNodeInfo(final int n) {
        return (AccessibilityNodeInfo)this.val$bridge.createAccessibilityNodeInfo(n);
    }
    
    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(final String s, final int n) {
        return (List<AccessibilityNodeInfo>)this.val$bridge.findAccessibilityNodeInfosByText(s, n);
    }
    
    public AccessibilityNodeInfo findFocus(final int n) {
        return (AccessibilityNodeInfo)this.val$bridge.findFocus(n);
    }
    
    public boolean performAction(final int n, final int n2, final Bundle bundle) {
        return this.val$bridge.performAction(n, n2, bundle);
    }
}
