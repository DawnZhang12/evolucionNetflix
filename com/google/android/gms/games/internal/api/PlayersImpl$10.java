// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.games.internal.api;

import com.google.android.gms.games.Players$LoadPlayersResult;
import com.google.android.gms.common.api.BaseImplementation$b;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.common.api.Api$a;

class PlayersImpl$10 extends PlayersImpl$LoadPlayersImpl
{
    final /* synthetic */ boolean XU;
    final /* synthetic */ String XX;
    final /* synthetic */ int Yl;
    
    @Override
    protected void a(final GamesClientImpl gamesClientImpl) {
        gamesClientImpl.a((BaseImplementation$b<Players$LoadPlayersResult>)this, "played_with", this.XX, this.Yl, false, this.XU);
    }
}