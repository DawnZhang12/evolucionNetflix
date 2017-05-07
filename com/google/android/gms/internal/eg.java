// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.Parcelable$Creator;
import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;
import android.os.IInterface;

public interface eg extends IInterface
{
    void a(final Map p0, final long p1, final String p2, final List<ef> p3) throws RemoteException;
    
    void bR() throws RemoteException;
    
    String getVersion() throws RemoteException;
    
    public abstract static class a extends Binder implements eg
    {
        public static eg t(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.analytics.internal.IAnalyticsService");
            if (queryLocalInterface != null && queryLocalInterface instanceof eg) {
                return (eg)queryLocalInterface;
            }
            return new eg.a.a(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.analytics.internal.IAnalyticsService");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.analytics.internal.IAnalyticsService");
                    this.a(parcel.readHashMap(this.getClass().getClassLoader()), parcel.readLong(), parcel.readString(), parcel.createTypedArrayList((Parcelable$Creator)ef.CREATOR));
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.analytics.internal.IAnalyticsService");
                    this.bR();
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.analytics.internal.IAnalyticsService");
                    final String version = this.getVersion();
                    parcel2.writeNoException();
                    parcel2.writeString(version);
                    return true;
                }
            }
        }
        
        private static class a implements eg
        {
            private IBinder kn;
            
            a(final IBinder kn) {
                this.kn = kn;
            }
            
            @Override
            public void a(final Map map, final long n, final String s, final List<ef> list) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.analytics.internal.IAnalyticsService");
                    obtain.writeMap(map);
                    obtain.writeLong(n);
                    obtain.writeString(s);
                    obtain.writeTypedList((List)list);
                    this.kn.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public IBinder asBinder() {
                return this.kn;
            }
            
            @Override
            public void bR() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.analytics.internal.IAnalyticsService");
                    this.kn.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public String getVersion() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.analytics.internal.IAnalyticsService");
                    this.kn.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}