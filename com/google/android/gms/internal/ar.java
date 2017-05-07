// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface ar extends IInterface
{
    void onAppEvent(final String p0, final String p1) throws RemoteException;
    
    public abstract static class a extends Binder implements ar
    {
        public a() {
            this.attachInterface((IInterface)this, "com.google.android.gms.ads.internal.client.IAppEventListener");
        }
        
        public static ar h(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.ads.internal.client.IAppEventListener");
            if (queryLocalInterface != null && queryLocalInterface instanceof ar) {
                return (ar)queryLocalInterface;
            }
            return new ar.a.a(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.ads.internal.client.IAppEventListener");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.ads.internal.client.IAppEventListener");
                    this.onAppEvent(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class a implements ar
        {
            private IBinder kn;
            
            a(final IBinder kn) {
                this.kn = kn;
            }
            
            public IBinder asBinder() {
                return this.kn;
            }
            
            @Override
            public void onAppEvent(final String s, final String s2) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAppEventListener");
                    obtain.writeString(s);
                    obtain.writeString(s2);
                    this.kn.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}