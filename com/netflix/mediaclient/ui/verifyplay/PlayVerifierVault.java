// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.verifyplay;

import com.netflix.mediaclient.util.ParcelUtils;
import android.os.Parcel;
import com.netflix.mediaclient.servicemgr.Asset;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class PlayVerifierVault implements Parcelable
{
    public static final Parcelable$Creator<PlayVerifierVault> CREATOR;
    public static final String NAME;
    private Asset mAsset;
    private String mInvokeLocation;
    private boolean mRemotePlayback;
    private String mUuid;
    
    static {
        NAME = PlayVerifierVault.class.getSimpleName();
        CREATOR = (Parcelable$Creator)new PlayVerifierVault$1();
    }
    
    PlayVerifierVault() {
    }
    
    PlayVerifierVault(final Parcel parcel) {
        this.mInvokeLocation = ParcelUtils.readString(parcel);
        this.mUuid = ParcelUtils.readString(parcel);
        this.mRemotePlayback = ParcelUtils.readBoolean(parcel);
        this.mAsset = (Asset)parcel.readParcelable(Asset.class.getClassLoader());
    }
    
    public PlayVerifierVault(final String s, final Asset asset) {
        this(s, asset, false, null);
    }
    
    public PlayVerifierVault(final String s, final Asset asset, final boolean b) {
        this(s, asset, b, null);
    }
    
    PlayVerifierVault(final String mInvokeLocation, final Asset mAsset, final boolean mRemotePlayback, final String mUuid) {
        this.mInvokeLocation = mInvokeLocation;
        this.mAsset = mAsset;
        this.mRemotePlayback = mRemotePlayback;
        this.mUuid = mUuid;
    }
    
    public PlayVerifierVault(final String s, final String s2) {
        this(s, null, false, s2);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Asset getAsset() {
        return this.mAsset;
    }
    
    public String getInvokeLocation() {
        return this.mInvokeLocation;
    }
    
    public String getUuid() {
        return this.mUuid;
    }
    
    public boolean isRemotePlayback() {
        return this.mRemotePlayback;
    }
    
    @Override
    public String toString() {
        return "PinDialogVault [mInvokeLocation=" + this.mInvokeLocation + ", mAsset=" + this.mAsset + ", mRemotePlayback=" + this.mRemotePlayback + ", mUuid=" + this.mUuid + "]";
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        ParcelUtils.writeString(parcel, this.mInvokeLocation);
        ParcelUtils.writeString(parcel, this.mUuid);
        ParcelUtils.writeBoolean(parcel, this.mRemotePlayback);
        parcel.writeParcelable((Parcelable)this.mAsset, n);
    }
}