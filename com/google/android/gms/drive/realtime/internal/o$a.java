// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.drive.realtime.internal;

import com.google.android.gms.common.api.Status;
import android.os.Parcel;
import android.os.IInterface;
import android.os.IBinder;
import android.os.Binder;

public abstract class o$a extends Binder implements o
{
    public static o ak(final IBinder binder) {
        if (binder == null) {
            return null;
        }
        final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.drive.realtime.internal.ISuccessCallback");
        if (queryLocalInterface != null && queryLocalInterface instanceof o) {
            return (o)queryLocalInterface;
        }
        return new o$a$a(binder);
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
                parcel2.writeString("com.google.android.gms.drive.realtime.internal.ISuccessCallback");
                return true;
            }
            case 1: {
                parcel.enforceInterface("com.google.android.gms.drive.realtime.internal.ISuccessCallback");
                this.onSuccess();
                parcel2.writeNoException();
                return true;
            }
            case 2: {
                parcel.enforceInterface("com.google.android.gms.drive.realtime.internal.ISuccessCallback");
                Status fromParcel;
                if (parcel.readInt() != 0) {
                    fromParcel = Status.CREATOR.createFromParcel(parcel);
                }
                else {
                    fromParcel = null;
                }
                this.o(fromParcel);
                parcel2.writeNoException();
                return true;
            }
        }
    }
}