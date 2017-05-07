// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import java.util.Collection;
import com.google.android.gms.games.Game;
import com.google.android.gms.internal.fe;
import com.google.android.gms.internal.fo;
import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.internal.fq;
import java.util.ArrayList;
import com.google.android.gms.games.GameEntity;
import android.os.Parcelable$Creator;
import com.google.android.gms.games.internal.GamesDowngradeableSafeParcel;

public final class InvitationEntity extends GamesDowngradeableSafeParcel implements Invitation
{
    public static final Parcelable$Creator<InvitationEntity> CREATOR;
    private final String IV;
    private final GameEntity Lt;
    private final long Mu;
    private final int Mv;
    private final ParticipantEntity Mw;
    private final ArrayList<ParticipantEntity> Mx;
    private final int My;
    private final int Mz;
    private final int xH;
    
    static {
        CREATOR = (Parcelable$Creator)new InvitationEntityCreatorCompat();
    }
    
    InvitationEntity(final int xh, final GameEntity lt, final String iv, final long mu, final int mv, final ParticipantEntity mw, final ArrayList<ParticipantEntity> mx, final int my, final int mz) {
        this.xH = xh;
        this.Lt = lt;
        this.IV = iv;
        this.Mu = mu;
        this.Mv = mv;
        this.Mw = mw;
        this.Mx = mx;
        this.My = my;
        this.Mz = mz;
    }
    
    InvitationEntity(final Invitation invitation) {
        this.xH = 2;
        this.Lt = new GameEntity(invitation.getGame());
        this.IV = invitation.getInvitationId();
        this.Mu = invitation.getCreationTimestamp();
        this.Mv = invitation.getInvitationType();
        this.My = invitation.getVariant();
        this.Mz = invitation.getAvailableAutoMatchSlots();
        final String participantId = invitation.getInviter().getParticipantId();
        final Freezable<ParticipantEntity> freezable = null;
        final ArrayList<Participant> participants = invitation.getParticipants();
        final int size = participants.size();
        this.Mx = new ArrayList<ParticipantEntity>(size);
        int i = 0;
        Object o = freezable;
        while (i < size) {
            final Participant participant = participants.get(i);
            if (participant.getParticipantId().equals(participantId)) {
                o = participant;
            }
            this.Mx.add(((Freezable<ParticipantEntity>)participant).freeze());
            ++i;
        }
        fq.b(o, "Must have a valid inviter!");
        this.Mw = ((Freezable<ParticipantEntity>)o).freeze();
    }
    
    static int a(final Invitation invitation) {
        return fo.hashCode(invitation.getGame(), invitation.getInvitationId(), invitation.getCreationTimestamp(), invitation.getInvitationType(), invitation.getInviter(), invitation.getParticipants(), invitation.getVariant(), invitation.getAvailableAutoMatchSlots());
    }
    
    static boolean a(final Invitation invitation, final Object o) {
        final boolean b = true;
        boolean b2;
        if (!(o instanceof Invitation)) {
            b2 = false;
        }
        else {
            b2 = b;
            if (invitation != o) {
                final Invitation invitation2 = (Invitation)o;
                if (fo.equal(invitation2.getGame(), invitation.getGame()) && fo.equal(invitation2.getInvitationId(), invitation.getInvitationId()) && fo.equal(invitation2.getCreationTimestamp(), invitation.getCreationTimestamp()) && fo.equal(invitation2.getInvitationType(), invitation.getInvitationType()) && fo.equal(invitation2.getInviter(), invitation.getInviter()) && fo.equal(invitation2.getParticipants(), invitation.getParticipants()) && fo.equal(invitation2.getVariant(), invitation.getVariant())) {
                    b2 = b;
                    if (fo.equal(invitation2.getAvailableAutoMatchSlots(), invitation.getAvailableAutoMatchSlots())) {
                        return b2;
                    }
                }
                return false;
            }
        }
        return b2;
    }
    
    static String b(final Invitation invitation) {
        return fo.e(invitation).a("Game", invitation.getGame()).a("InvitationId", invitation.getInvitationId()).a("CreationTimestamp", invitation.getCreationTimestamp()).a("InvitationType", invitation.getInvitationType()).a("Inviter", invitation.getInviter()).a("Participants", invitation.getParticipants()).a("Variant", invitation.getVariant()).a("AvailableAutoMatchSlots", invitation.getAvailableAutoMatchSlots()).toString();
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return a(this, o);
    }
    
    public Invitation freeze() {
        return this;
    }
    
    @Override
    public int getAvailableAutoMatchSlots() {
        return this.Mz;
    }
    
    @Override
    public long getCreationTimestamp() {
        return this.Mu;
    }
    
    @Override
    public Game getGame() {
        return this.Lt;
    }
    
    @Override
    public String getInvitationId() {
        return this.IV;
    }
    
    @Override
    public int getInvitationType() {
        return this.Mv;
    }
    
    @Override
    public Participant getInviter() {
        return this.Mw;
    }
    
    public ArrayList<Participant> getParticipants() {
        return new ArrayList<Participant>(this.Mx);
    }
    
    @Override
    public int getVariant() {
        return this.My;
    }
    
    public int getVersionCode() {
        return this.xH;
    }
    
    @Override
    public int hashCode() {
        return a(this);
    }
    
    public boolean isDataValid() {
        return true;
    }
    
    @Override
    public String toString() {
        return b(this);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        if (!this.eK()) {
            InvitationEntityCreator.a(this, parcel, n);
        }
        else {
            this.Lt.writeToParcel(parcel, n);
            parcel.writeString(this.IV);
            parcel.writeLong(this.Mu);
            parcel.writeInt(this.Mv);
            this.Mw.writeToParcel(parcel, n);
            final int size = this.Mx.size();
            parcel.writeInt(size);
            for (int i = 0; i < size; ++i) {
                this.Mx.get(i).writeToParcel(parcel, n);
            }
        }
    }
    
    static final class InvitationEntityCreatorCompat extends InvitationEntityCreator
    {
        @Override
        public InvitationEntity au(final Parcel parcel) {
            if (GamesDowngradeableSafeParcel.c(fe.eJ()) || fe.al(InvitationEntity.class.getCanonicalName())) {
                return super.au(parcel);
            }
            final GameEntity gameEntity = (GameEntity)GameEntity.CREATOR.createFromParcel(parcel);
            final String string = parcel.readString();
            final long long1 = parcel.readLong();
            final int int1 = parcel.readInt();
            final ParticipantEntity participantEntity = (ParticipantEntity)ParticipantEntity.CREATOR.createFromParcel(parcel);
            final int int2 = parcel.readInt();
            final ArrayList list = new ArrayList<ParticipantEntity>(int2);
            for (int i = 0; i < int2; ++i) {
                list.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
            }
            return new InvitationEntity(2, gameEntity, string, long1, int1, participantEntity, (ArrayList<ParticipantEntity>)list, -1, 0);
        }
    }
}