// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import com.google.android.gms.fitness.result.SessionStopResult;
import android.os.Parcel;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;

public abstract class kr$a extends Binder implements kr
{
    public kr$a() {
        this.attachInterface((IInterface)this, "com.google.android.gms.fitness.internal.ISessionStopCallback");
    }
    
    public static kr av(final IBinder binder) {
        if (binder == null) {
            return null;
        }
        final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.fitness.internal.ISessionStopCallback");
        if (queryLocalInterface != null && queryLocalInterface instanceof kr) {
            return (kr)queryLocalInterface;
        }
        return new kr$a$a(binder);
    }
    
    public IBinder asBinder() {
        return (IBinder)this;
    }
    
    public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) {
        switch (n) {
            default: {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            case 1598968902: {
                parcel2.writeString("com.google.android.gms.fitness.internal.ISessionStopCallback");
                return true;
            }
            case 1: {
                parcel.enforceInterface("com.google.android.gms.fitness.internal.ISessionStopCallback");
                SessionStopResult sessionStopResult;
                if (parcel.readInt() != 0) {
                    sessionStopResult = (SessionStopResult)SessionStopResult.CREATOR.createFromParcel(parcel);
                }
                else {
                    sessionStopResult = null;
                }
                this.a(sessionStopResult);
                parcel2.writeNoException();
                return true;
            }
        }
    }
}