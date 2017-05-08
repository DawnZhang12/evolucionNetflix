// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.webclient.model.leafs;

import java.util.List;

public class AccountData
{
    private User user;
    private final List<UserProfile> userProfiles;
    
    public AccountData(final List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public List<UserProfile> getUserProfiles() {
        return this.userProfiles;
    }
    
    public void setUser(final User user) {
        this.user = user;
    }
}
