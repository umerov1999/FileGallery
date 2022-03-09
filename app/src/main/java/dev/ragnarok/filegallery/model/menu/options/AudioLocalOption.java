package dev.ragnarok.filegallery.model.menu.options;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({AudioLocalOption.play_item_audio,
        AudioLocalOption.play_item_after_current_audio,
        AudioLocalOption.bitrate_item_audio,
        AudioLocalOption.share_item,
        AudioLocalOption.open_with_item,
        AudioLocalOption.fix_dir_time_item,
        AudioLocalOption.update_file_time_item,
        AudioLocalOption.add_dir_tag_item,
})
@Retention(RetentionPolicy.SOURCE)
public @interface AudioLocalOption {
    int play_item_audio = 1;
    int play_item_after_current_audio = 2;
    int bitrate_item_audio = 3;
    int share_item = 4;
    int open_with_item = 5;
    int fix_dir_time_item = 6;
    int update_file_time_item = 7;
    int add_dir_tag_item = 8;
}

