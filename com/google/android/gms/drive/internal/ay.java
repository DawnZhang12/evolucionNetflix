// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.drive.internal;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.common.internal.safeparcel.a;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.b;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class ay implements Parcelable$Creator<RemoveEventListenerRequest>
{
    static void a(final RemoveEventListenerRequest removeEventListenerRequest, final Parcel parcel, final int n) {
        final int d = b.D(parcel);
        b.c(parcel, 1, removeEventListenerRequest.BR);
        b.a(parcel, 2, (Parcelable)removeEventListenerRequest.MO, n, false);
        b.c(parcel, 3, removeEventListenerRequest.NS);
        b.H(parcel, d);
    }
    
    public RemoveEventListenerRequest az(final Parcel parcel) {
        int g = 0;
        final int c = a.C(parcel);
        DriveId driveId = null;
        int g2 = 0;
        while (parcel.dataPosition() < c) {
            final int b = a.B(parcel);
            switch (a.aD(b)) {
                default: {
                    a.b(parcel, b);
                    continue;
                }
                case 1: {
                    g2 = a.g(parcel, b);
                    continue;
                }
                case 2: {
                    driveId = a.a(parcel, b, DriveId.CREATOR);
                    continue;
                }
                case 3: {
                    g = a.g(parcel, b);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != c) {
            throw new a.a("Overread allowed size end=" + c, parcel);
        }
        return new RemoveEventListenerRequest(g2, driveId, g);
    }
    
    public RemoveEventListenerRequest[] bL(final int n) {
        return new RemoveEventListenerRequest[n];
    }
}