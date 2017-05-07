// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.player;

import com.netflix.mediaclient.media.AudioSubtitleDefaultOrderInfo;
import com.netflix.mediaclient.media.AudioSource;
import com.netflix.mediaclient.media.Subtitle;
import com.netflix.mediaclient.util.LanguageChoice;
import com.netflix.mediaclient.service.logging.error.ErrorLoggingManager;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.util.log.UIViewLogUtils;
import com.netflix.mediaclient.servicemgr.UIViewLogging$UIViewCommandName;
import com.netflix.mediaclient.service.player.subtitles.SubtitleScreen;
import com.netflix.mediaclient.service.logging.client.model.DeepErrorElement;
import java.util.List;
import java.io.Serializable;
import android.media.AudioManager;
import android.app.DialogFragment;
import android.view.MenuItem;
import com.netflix.mediaclient.util.NflxProtocolUtils;
import com.netflix.mediaclient.util.DeviceUtils;
import android.view.Surface;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import com.netflix.mediaclient.servicemgr.UserActionLogging$CommandName;
import com.netflix.mediaclient.servicemgr.IClientLogging$ModalView;
import android.content.res.Configuration;
import com.netflix.mediaclient.ui.verifyplay.PinVerifier;
import android.content.Intent;
import com.netflix.mediaclient.service.net.LogMobileType;
import android.view.Window;
import com.netflix.mediaclient.ui.mdx.MdxTargetSelection;
import android.util.Pair;
import com.netflix.mediaclient.ui.verifyplay.PlayVerifier;
import com.netflix.mediaclient.ui.verifyplay.PlayVerifierVault;
import com.netflix.mediaclient.ui.verifyplay.PlayVerifierVault$PlayInvokedFrom;
import android.annotation.SuppressLint;
import android.view.TextureView;
import android.content.IntentFilter;
import com.netflix.mediaclient.util.AndroidUtils;
import android.support.v7.widget.Toolbar;
import com.netflix.mediaclient.javabridge.ui.IMedia$SubtitleProfile;
import com.netflix.mediaclient.service.configuration.SubtitleConfiguration;
import com.netflix.mediaclient.media.PlayoutMetadata;
import com.netflix.mediaclient.service.configuration.drm.DrmManagerRegistry;
import com.netflix.mediaclient.ui.player.subtitles.SubtitleManager;
import com.netflix.mediaclient.ui.player.subtitles.SubtitleManagerFactory;
import android.os.Debug;
import com.netflix.mediaclient.util.AndroidManifestUtils;
import com.netflix.mediaclient.util.PreferenceUtils;
import android.app.Activity;
import android.os.SystemClock;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import com.netflix.mediaclient.ui.experience.BrowseExperience;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.service.logging.client.model.UIError;
import com.netflix.mediaclient.util.log.UserActionLogUtils;
import com.netflix.mediaclient.servicemgr.IClientLogging$CompletionReason;
import com.netflix.mediaclient.util.StatusUtils;
import com.netflix.mediaclient.util.log.ConsolidatedLoggingUtils;
import com.netflix.mediaclient.util.ThreadUtils;
import com.netflix.mediaclient.android.widget.AlertDialogFactory;
import com.netflix.mediaclient.media.PlayerType;
import com.netflix.mediaclient.util.ConnectivityUtils;
import com.netflix.mediaclient.event.nrdp.media.NccpActionId;
import android.view.View;
import android.view.KeyEvent;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcelable;
import com.netflix.mediaclient.service.configuration.PlayerTypeFactory;
import com.netflix.mediaclient.event.nrdp.media.Error;
import com.netflix.mediaclient.android.widget.AlertDialogFactory$AlertDialogDescriptor;
import com.netflix.mediaclient.service.error.ErrorDescriptor;
import com.netflix.mediaclient.service.logging.client.model.ActionOnUIError;
import com.netflix.mediaclient.service.logging.client.model.RootCause;
import com.netflix.mediaclient.event.nrdp.media.MediaEvent;
import com.netflix.mediaclient.event.nrdp.media.NccpError;
import com.netflix.mediaclient.android.widget.TappableSurfaceView$TapListener;
import com.netflix.mediaclient.android.widget.TappableSurfaceView$SurfaceMeasureListener;
import android.view.SurfaceHolder$Callback;
import com.netflix.mediaclient.media.JPlayer.SecondSurface;
import com.netflix.mediaclient.service.player.subtitles.SafeSubtitleManager;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import android.view.ViewGroup;
import android.view.Menu;
import com.netflix.mediaclient.servicemgr.IPlayer;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.os.Bundle;
import com.netflix.mediaclient.ui.details.AbsEpisodeView$EpisodeRowListener;
import com.netflix.mediaclient.android.fragment.NetflixDialogFrag$DialogCanceledListener;
import com.netflix.mediaclient.service.ServiceAgent$ConfigurationAgentInterface;
import com.netflix.mediaclient.media.Language;
import android.view.View$OnClickListener;
import android.widget.SeekBar$OnSeekBarChangeListener;
import com.netflix.mediaclient.ui.common.PlayContextProvider;
import com.netflix.mediaclient.servicemgr.IPlayer$PlayerListener;
import com.netflix.mediaclient.media.JPlayer.JPlayer$JplayerListener;
import com.netflix.mediaclient.android.widget.ErrorWrapper$Callback;
import com.netflix.mediaclient.android.fragment.NetflixDialogFrag$DialogCanceledListenerProvider;
import android.media.AudioManager$OnAudioFocusChangeListener;
import com.netflix.mediaclient.android.fragment.NetflixFrag;
import com.netflix.mediaclient.servicemgr.interface_.details.ShowDetails;
import com.netflix.mediaclient.servicemgr.interface_.details.MovieDetails;
import com.netflix.mediaclient.servicemgr.interface_.Playable;
import com.netflix.mediaclient.servicemgr.Asset;
import android.content.Context;
import android.widget.Toast;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.interface_.details.VideoDetails;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.servicemgr.LoggingManagerCallback;

class PlayerFragment$FetchVideoDetailsForPlaybackCallback extends LoggingManagerCallback
{
    private final PlayContext playContext;
    final /* synthetic */ PlayerFragment this$0;
    
    public PlayerFragment$FetchVideoDetailsForPlaybackCallback(final PlayerFragment this$0, final PlayContext playContext) {
        this.this$0 = this$0;
        super("PlayerFragment");
        this.playContext = playContext;
    }
    
    private void handleResponse(final VideoDetails videoDetails, final Status status) {
        boolean b = false;
        this.this$0.mIsAssetReady = false;
        if (!this.this$0.isActivityValid()) {
            return;
        }
        if (status.isError() || videoDetails == null) {
            Log.w("PlayerFragment", "Error loading video details for video playback");
            Toast.makeText((Context)this.this$0.getActivity(), 2131165441, 1).show();
            return;
        }
        if (Log.isLoggable()) {
            Log.v("PlayerFragment", "Retrieved details: " + videoDetails.getTitle() + ", " + videoDetails);
        }
        final PlayerFragment this$0 = this.this$0;
        final Playable playable = videoDetails.getPlayable();
        final PlayContext playContext = this.playContext;
        if (!PlayerActivity.PIN_VERIFIED) {
            b = true;
        }
        this$0.mAsset = Asset.create(playable, playContext, b);
        this.this$0.mIsAssetReady = true;
        this.this$0.updateUI();
        this.this$0.completeInitIfReady();
    }
    
    @Override
    public void onMovieDetailsFetched(final MovieDetails movieDetails, final Status status) {
        super.onMovieDetailsFetched(movieDetails, status);
        this.handleResponse(movieDetails, status);
    }
    
    @Override
    public void onShowDetailsFetched(final ShowDetails showDetails, final Status status) {
        super.onShowDetailsFetched(showDetails, status);
        this.handleResponse(showDetails, status);
    }
}