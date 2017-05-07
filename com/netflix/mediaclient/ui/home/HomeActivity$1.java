// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.home;

import android.view.View;
import com.netflix.mediaclient.ui.search.SearchMenu;
import com.netflix.mediaclient.ui.mdx.MdxMenu;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout$DrawerListener;
import android.app.Activity;
import com.netflix.mediaclient.ui.kids.lolomo.KidsSlidingMenuAdapter;
import java.util.Collection;
import android.os.SystemClock;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.KeyEvent;
import com.netflix.mediaclient.ui.lolomo.LoLoMoFrag;
import com.netflix.mediaclient.android.fragment.NetflixFrag;
import com.netflix.mediaclient.android.widget.NetflixActionBar;
import com.netflix.mediaclient.android.widget.NetflixActionBar$LogoType;
import com.netflix.mediaclient.util.log.UIViewLogUtils;
import com.netflix.mediaclient.servicemgr.UIViewLogging$UIViewCommandName;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.android.app.CommonStatus;
import android.app.Fragment;
import android.os.Parcelable;
import com.netflix.mediaclient.util.SocialNotificationsUtils;
import android.widget.Toast;
import com.netflix.mediaclient.util.StringUtils;
import java.io.Serializable;
import android.content.Context;
import com.netflix.mediaclient.ui.kids.lolomo.KidsHomeActivity;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.ui.kids.KidsUtils;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.servicemgr.IClientLogging$ModalView;
import com.netflix.mediaclient.android.widget.ObjectRecycler$ViewRecycler;
import android.content.BroadcastReceiver;
import com.netflix.mediaclient.servicemgr.ManagerStatusListener;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import android.content.DialogInterface$OnClickListener;
import com.netflix.mediaclient.servicemgr.model.genre.GenreList;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import java.util.LinkedList;
import com.netflix.mediaclient.android.widget.ObjectRecycler$ViewRecyclerProvider;
import com.netflix.mediaclient.android.activity.FragmentHostActivity;
import com.netflix.mediaclient.servicemgr.IBrowseManager;
import android.view.MenuItem;
import android.view.MenuItem$OnMenuItemClickListener;

class HomeActivity$1 implements MenuItem$OnMenuItemClickListener
{
    final /* synthetic */ HomeActivity this$0;
    
    HomeActivity$1(final HomeActivity this$0) {
        this.this$0 = this$0;
    }
    
    public boolean onMenuItemClick(final MenuItem menuItem) {
        final IBrowseManager browse = this.this$0.manager.getBrowse();
        final String access$000 = this.this$0.genreId;
        String title;
        if (this.this$0.genre == null) {
            title = "lolomo";
        }
        else {
            title = this.this$0.genre.getTitle();
        }
        browse.dumpHomeLoLoMosAndVideos(access$000, title);
        return false;
    }
}