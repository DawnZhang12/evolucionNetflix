// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.falkor;

import com.netflix.falkor.BranchNode;
import com.netflix.falkor.ModelProxy;
import com.netflix.model.branches.FalkorPerson;
import com.netflix.falkor.Func;

final class Falkor$Creator$17 implements Func<FalkorPerson>
{
    final /* synthetic */ ModelProxy val$proxy;
    
    Falkor$Creator$17(final ModelProxy val$proxy) {
        this.val$proxy = val$proxy;
    }
    
    @Override
    public FalkorPerson call() {
        return new FalkorPerson(this.val$proxy);
    }
}