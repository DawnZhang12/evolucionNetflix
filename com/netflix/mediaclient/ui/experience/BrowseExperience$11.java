// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.experience;

import java.util.HashMap;
import com.netflix.mediaclient.util.gfx.ImageLoader$StaticImgConfig;
import com.netflix.mediaclient.ui.search.SearchUtils$SearchExperience;

final class BrowseExperience$11 extends BrowseExperience$ExperienceMap
{
    BrowseExperience$11() {
        super(null);
        ((HashMap<String, SearchUtils$SearchExperience>)this).put("SEARCH_EXPERIENCE_ENUM", SearchUtils$SearchExperience.PHONE);
        ((HashMap<String, Integer>)this).put("LOMO_FRAG_OFFSET_LEFT_DIMEN_INT", 2131362208);
        ((HashMap<String, Boolean>)this).put("SHOULD_LOAD_KUBRICK_LEAVES_IN_LOLOMO_BOOL", false);
        ((HashMap<String, Boolean>)this).put("SHOULD_LOAD_KUBRICK_LEAVES_IN_DETAILS_BOOL", true);
        ((HashMap<String, Boolean>)this).put("SHOULD_INCLUDE_CHARACTER_LEAVES_BOOL", false);
        ((HashMap<String, ImageLoader$StaticImgConfig>)this).put("IMAGE_LOADER_CONFIG_ENUM", ImageLoader$StaticImgConfig.DARK);
        ((HashMap<String, ImageLoader$StaticImgConfig>)this).put("IMAGE_LOADER_CONFIG_NO_PLACEHOLDER_ENUM", ImageLoader$StaticImgConfig.DARK_NO_PLACEHOLDER);
        ((HashMap<String, Boolean>)this).put("USE_LOLOMO_BOXART", false);
        ((HashMap<String, Boolean>)this).put("USE_KIDS_GENRES_LOMO", false);
    }
}
