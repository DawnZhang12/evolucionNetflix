// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.games.internal.api;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer$LeaveMatchResult;
import com.google.android.gms.games.Games$BaseGamesApiMethodImpl;

abstract class TurnBasedMultiplayerImpl$LeaveMatchImpl extends Games$BaseGamesApiMethodImpl<TurnBasedMultiplayer$LeaveMatchResult>
{
    public TurnBasedMultiplayer$LeaveMatchResult au(final Status status) {
        return new TurnBasedMultiplayerImpl$LeaveMatchImpl$1(this, status);
    }
}